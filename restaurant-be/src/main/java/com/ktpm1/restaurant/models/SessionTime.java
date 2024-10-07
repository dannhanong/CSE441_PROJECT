package com.ktpm1.restaurant.models;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SessionTime {
    MORNING,
    LUNCH,
    EVENING,
    NIGHT
}
