package com.ktpm1.restaurant.dtos.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogRequest {
    String name;
    String description;
    MultipartFile file;
    int quantityOfTables;
    int capacity;
}
