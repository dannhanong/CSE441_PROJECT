package com.ktpm1.restaurant.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;
import okhttp3.MultipartBody;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfile {
    String name;
    String email;
    MultipartBody.Part avatar;
}
