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

    @NotBlank(message = "{validation.change_password.current.empty}")
    private String currentPassword;
    @NotBlank(message = "{validation.change_password.new.empty}")
    private String newPassword;
}
