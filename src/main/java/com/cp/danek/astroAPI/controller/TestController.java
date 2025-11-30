package com.cp.danek.astroAPI.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String test() {
        return "Astronomical Observatory API is running!";
    }

    @GetMapping("/generate-password-hash")
    public String generatePasswordHash(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("=== ГЕНЕРАЦИЯ ХЕША ПАРОЛЯ ===");
        System.out.println("Исходный пароль: " + rawPassword);
        System.out.println("BCrypt хеш: " + encodedPassword);
        System.out.println("=============================");

        return encodedPassword;
    }
}