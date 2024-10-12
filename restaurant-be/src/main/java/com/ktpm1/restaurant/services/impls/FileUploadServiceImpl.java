package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.models.FileUpload;
import com.ktpm1.restaurant.repositories.FileUploadRepository;
import com.ktpm1.restaurant.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    private FileUploadRepository fileUploadRepository;
    private Path foundFile;

    @Override
    public FileUpload uploadFile(MultipartFile multipartFile) throws IOException {
        Path uploadDirectory = Paths.get("Files-Upload");

        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        String fileCode = UUID.randomUUID().toString();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadDirectory.resolve(fileCode);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Error saving file: ", e);
        }

        FileUpload fileUpload = new FileUpload();

        // Extract file extension
        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        fileUpload.setFileName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        fileUpload.setFileType(fileExtension);
        fileUpload.setFileCode(fileCode);
        fileUpload.setSize(multipartFile.getSize());
        return fileUploadRepository.save(fileUpload);
    }

    @Override
    public Resource getFileAsResource(String fileCode) throws IOException {
        Path uploadDirectory = Paths.get("Files-Upload");
        foundFile = null; // Reset foundFile before searching

        Files.list(uploadDirectory).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
                return; // Break loop
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }
        throw new IOException("File not found with fileCode: " + fileCode);
    }

    @Override
    public void deleteFileByFileCode(String fileCode) throws IOException {
        FileUpload fileUpload = fileUploadRepository.findByFileCode(fileCode);
        fileUploadRepository.delete(fileUpload);
    }

    @Override
    public FileUpload getFileUploadByFileCode(String fileCode) {
        return fileUploadRepository.findByFileCode(fileCode);
    }
}
