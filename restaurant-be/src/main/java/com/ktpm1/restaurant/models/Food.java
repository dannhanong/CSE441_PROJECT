package com.ktpm1.restaurant.models;

import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    int price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    String imageCode;
    List<String> imageList;
    SessionTime sessionTime;
}
