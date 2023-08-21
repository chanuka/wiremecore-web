package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String userName;
    private String contactNo;
    private String email;
    private String deviceId;
}
