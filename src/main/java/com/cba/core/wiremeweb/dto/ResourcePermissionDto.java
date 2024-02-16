package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResourcePermissionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer resourceId;
    private String resourceName;
    private Integer readd;
    private Integer created;
    private Integer updated;
    private Integer deleted;
}
