package com.cba.core.wiremeweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleRequestDto implements Serializable {

    private Integer userId;
    private Integer roleId;
    @NotBlank(message = "Status is required")
    private String status;

    private static final long serialVersionUID = 1L;
}
