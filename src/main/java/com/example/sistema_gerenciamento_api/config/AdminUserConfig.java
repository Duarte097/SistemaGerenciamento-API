package com.example.sistema_gerenciamento_api.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminUserConfig {
    private static final String ADMIN = "admin";
    private static final String PASSWORD = "admin";

    public static String getAdmin() {
        return ADMIN;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
