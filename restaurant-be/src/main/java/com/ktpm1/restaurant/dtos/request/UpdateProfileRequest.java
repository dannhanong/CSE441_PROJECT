package com.ktpm1.restaurant.dtos.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    private String name;
    private String phoneNumber;
    private MultipartFile avatar;
}
