package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long>{
    public void deleteByFileCode(String fileCode);
    public FileUpload findByFileCode(String fileCode);
}
