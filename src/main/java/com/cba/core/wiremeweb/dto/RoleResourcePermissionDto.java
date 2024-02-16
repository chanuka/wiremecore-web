package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleResourcePermissionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer roleId;
    private String roleName;
    private List<ResourcePermissionDto> resources;
}
