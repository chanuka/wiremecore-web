package com.cba.core.wiremeweb.dto;

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
public class PermissionRequestDto implements Serializable {

    @Positive(message = "{validation.permission.role.positive}")
    private Integer roleId;
    @Positive(message = "{validation.permission.resource.positive}")
    private Integer resourceId;
    private Integer readd;
    private Integer created;
    private Integer updated;
    private Integer deleted;

    private static final long serialVersionUID = 1L;

}
