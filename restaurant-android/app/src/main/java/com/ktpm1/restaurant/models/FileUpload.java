package com.ktpm1.restaurant.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUpload {
    private Long id;
    private String fileName;
    private String fileType;
    private String fileCode;
    private Long size;
}
