package com.ktpm1.restaurant.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryFood {
    Long id;
    String name; // Tên món ăn
    String date; // Ngày đặt
    String time; // Thời gian đặt
    String table; // Bàn đặt
    String place; // Vị trí đặt (ví dụ: phòng ăn, phòng làm việc)
    String status; // Trạng thái (ví dụ: "Xong", "Hủy")
}
