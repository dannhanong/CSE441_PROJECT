package com.ktpm1.restaurant.dtos.request;

import com.ktpm1.restaurant.models.FileUpload;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodRequest {
    String name;
    String description;
    int price;
    Long categoryId;
    MultipartFile file;
}
