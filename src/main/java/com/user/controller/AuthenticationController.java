package com.user.controller;

import com.user.entity.User;
import com.user.request.LoginUserRequest;
import com.user.request.RefreshTokenRequest;
import com.user.request.RegisterUserRequest;
import com.user.response.LoginResponse;
import com.user.service.AuthenticationService;
import com.user.service.JwtService;
import com.user.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserRequest request) {
        User registeredUser = authenticationService.signup(request);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserRequest request) {
        LoginResponse response = refreshTokenService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        LoginResponse loginResponse = refreshTokenService.generateNewAccessToken(refreshTokenRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
