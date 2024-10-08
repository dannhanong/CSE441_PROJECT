package com.ktpm1.restaurant.controllers;

import java.time.LocalTime;
import java.util.Calendar;

public class test {
    public static void main(String[] args) {
        Calendar now = Calendar.getInstance();
        System.out.println(now.get(Calendar.HOUR_OF_DAY));
        LocalTime time = LocalTime.now();
        System.out.println(time);
    }
}
