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
            "/cart/**"
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
            "/auth/get/profile",
            "/cart/**",
    };

    public static final String[] PUBLIC_PUT_ENDPOINTS = {
            "/auth/reset-password",
            "/auth/update-verify-code",
    };

    public static final String[] PRIVATE_PUT_ENDPOINTS = {
            "/auth/change-password",
            "/cart/**",
    };

    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/categories/admin/update/**",
            "/foods/admin/update/**",
            "/tables/admin/update/**",
    };

    public static final String[] PRIVATE_DELETE_ENDPOINTS = {
            "/auth/delete/hard",
            "/cart/**",
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
