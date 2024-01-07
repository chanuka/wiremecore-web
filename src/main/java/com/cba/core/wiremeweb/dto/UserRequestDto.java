package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UserRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "User Name is required")
    private String userName;
    @NotBlank(message = "Contact Number is required")
    private String contactNo;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is Invalid")
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer deviceId;
    private String status;
    private Integer userType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer merchantId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer partnerId;
}
