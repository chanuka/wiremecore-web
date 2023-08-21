package com.cba.core.wiremeweb.dto;

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
public class RoleRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Role Name is required")
    private String roleName;
    @NotBlank(message = "Status is required")
    private String status;
}
