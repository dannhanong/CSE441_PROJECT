package com.ktpm1.restaurant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Entity
@Table(name = "food_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    int price;
    @ManyToOne
    @JoinColumn(name = "food_id")
    @JsonIgnore
    Food food;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodOption that = (FoodOption) o;
        return id.equals(that.id); // So sánh dựa trên id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

