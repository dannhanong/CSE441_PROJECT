package com.ktpm1.restaurant.models;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@jakarta.persistence.Table(name = "file_uploads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private String fileCode;
    private Long size;
    private boolean publicFile = false;
}