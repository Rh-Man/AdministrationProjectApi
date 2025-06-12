package com.example.adminapi.security;

import com.example.adminapi.enums.Role;

import lombok.*;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
}