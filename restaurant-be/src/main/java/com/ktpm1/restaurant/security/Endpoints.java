package com.ktpm1.restaurant.security;

public class Endpoints {
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/auth/login",
            "/auth/signup",
            "/auth/forgot-password",
    };

    public static final String[] PRIVATE_POST_ENDPOINTS = {
            "/auth/logout",
            "/orders/create",
    };

    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/categories/admin/create",
            "/foods/admin/create",
            "/tables/admin/create",
    };

    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/auth/**",
            "/categories/all",
            "/foods/**",
            "/tables/**",
            "/orders/create",
            "/files/**",
    };

    public static final String[] PRIVATE_GET_ENDPOINTS = {
            "/orders/my-orders",
            "/auth/get/profile"
    };

    public static final String[] PUBLIC_PUT_ENDPOINTS = {
            "/auth/reset-password",
    };

    public static final String[] PRIVATE_PUT_ENDPOINTS = {
            "/auth/change-password",
            "/auth/update-profile"
    };

    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/categories/admin/update/**",
            "/foods/admin/**",
            "/tables/admin/update/**",
    };

    public static final String[] PRIVATE_DELETE_ENDPOINTS = {
            "/auth/delete/hard",
    };

    public static final String[] ADMIN_DELETE_ENDPOINTS = {
            "/categories/admin/delete/**",
            "/foods/admin/delete/**",
            "/tables/admin/delete/**",
    };

    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/categories/admin/**",
            "/orders/admin/all"
    };
}
