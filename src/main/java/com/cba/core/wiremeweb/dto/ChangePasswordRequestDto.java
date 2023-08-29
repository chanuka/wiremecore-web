package com.cba.core.wiremeweb.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangePasswordRequestDto {

    @NotBlank(message = "Current Password is required")
    private String currentPassword;
    @NotBlank(message = "New Password is required")
    private String newPassword;
}
