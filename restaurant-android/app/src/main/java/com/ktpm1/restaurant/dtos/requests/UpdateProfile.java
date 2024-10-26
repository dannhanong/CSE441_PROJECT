package com.ktpm1.restaurant.dtos.requests;

import java.io.File;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfile {
    String name;
    String phone;
    File avatar;
}
