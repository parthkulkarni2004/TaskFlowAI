package com.taskflow.backend.controller;

import com.taskflow.backend.dto.request.PasswordChangeRequest;
import com.taskflow.backend.dto.request.ProfileUpdateRequest;
import com.taskflow.backend.dto.response.UserResponse;
import com.taskflow.backend.security.CurrentUser;
import com.taskflow.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CurrentUser currentUser;

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(userService.me(currentUser.id()));
    }

    @PutMapping("/users/me")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(userService.updateProfile(currentUser.id(), request));
    }

    @PutMapping("/users/me/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(currentUser.id(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(userService.listAll());
    }
}
