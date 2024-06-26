package com.user.service;

import com.user.entity.User;
import com.user.repository.UserRepository;
import com.user.request.LoginUserRequest;
import com.user.request.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User signup(RegisterUserRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        return userRepository.save(user);
    }

    public User authenticate(LoginUserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        if(authentication.isAuthenticated()) {
            return userRepository.findByEmail(request.getEmail()).orElseThrow();
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    public User loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        return user;
    }
}
