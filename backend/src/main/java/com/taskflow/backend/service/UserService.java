package com.taskflow.backend.service;

import com.taskflow.backend.dto.request.PasswordChangeRequest;
import com.taskflow.backend.dto.request.ProfileUpdateRequest;
import com.taskflow.backend.dto.response.UserResponse;
import com.taskflow.backend.entity.User;
import com.taskflow.backend.exception.BadRequestException;
import com.taskflow.backend.exception.ResourceNotFoundException;
import com.taskflow.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    public UserResponse me(Long userId) {
        return UserResponse.from(getById(userId));
    }

    public List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(UserResponse::from).collect(Collectors.toList());
    }

    public UserResponse updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = getById(userId);
        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        user.setFullName(request.getFullname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setLocation(request.getLocation());
        user.setBio(request.getBio());
        return UserResponse.from(userRepository.save(user));
    }

    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = getById(userId);
        if (!passwordEncoder.matches(request.getCurrent(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
