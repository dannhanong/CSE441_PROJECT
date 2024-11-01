package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/preview/{fileCode}")
    public ResponseEntity<byte[]> previewImage(@PathVariable("fileCode") String fileCode) throws IOException {
        Resource resource = null;
        String contentType = null;
        try {
            resource = fileUploadService.getFileAsResource(fileCode);
            contentType = resource.getURL().openConnection().getContentType();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageData = StreamUtils.copyToByteArray(resource.getInputStream());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageData);
    }
}
