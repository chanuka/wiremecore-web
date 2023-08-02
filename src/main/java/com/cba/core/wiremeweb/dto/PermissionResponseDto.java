package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PermissionResponseDto implements java.io.Serializable{

    private String role;
    private String resource;
    private boolean readd;
    private boolean created;
    private boolean updated;
    private boolean deleted;

    private static final long serialVersionUID = 1L;

}
