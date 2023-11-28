package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleResponseDto implements java.io.Serializable {

    private Integer id;
    private Integer userId;
    private String userName;
    private Integer roleId;
    private String roleName;
    private String status;

    private static final long serialVersionUID = 1L;

}
