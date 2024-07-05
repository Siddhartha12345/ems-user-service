package com.user.controller;

import com.user.entity.User;
import com.user.response.UserFullName;
import com.user.service.AuthenticationService;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/load-user/{username}")
    public ResponseEntity<?> loadUserByUsername(@PathVariable String username,
                                                @RequestParam(name = "nameOnly", required = false) boolean nameOnly) {
        User user = authenticationService.loadUserByUsername(username);
        return nameOnly ? ResponseEntity.ok(UserFullName.builder().fullName(user.getFullName()).build()) :
                ResponseEntity.ok(user);
    }
}
