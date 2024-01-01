package com.maquiling.cloudstorage.model.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String username;
    private String email;
}
