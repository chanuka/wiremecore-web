package com.cba.core.wiremeweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "{validation.user_role.user.positive}")
    private Integer userId;
    @Positive(message = "{validation.user_role.role.positive}")
    private Integer roleId;
    @NotBlank(message = "{validation.user_role.status.empty}")
    private String status;

    private static final long serialVersionUID = 1L;
}
