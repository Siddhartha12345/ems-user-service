package com.user.request;

import lombok.Data;

@Data
public class LoginUserRequest {

    private String email;   // username

    private String password;
}
