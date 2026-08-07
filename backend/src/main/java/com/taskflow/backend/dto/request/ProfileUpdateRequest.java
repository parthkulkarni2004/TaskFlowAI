package com.taskflow.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    @NotBlank
    private String fullname;
    @NotBlank @Email
    private String email;
    private String phone;
    private String location;
    private String bio;
}
