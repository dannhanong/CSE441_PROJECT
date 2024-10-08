package com.ktpm1.restaurant.models;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
@Entity
@Table(name = "files")
@Getter
@Setter
@Builder
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
}