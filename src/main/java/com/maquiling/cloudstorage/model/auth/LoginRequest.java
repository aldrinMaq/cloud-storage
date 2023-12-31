package com.maquiling.cloudstorage.model.auth;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {
    }
}
