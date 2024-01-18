package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
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
public class UserRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{validation.user.name.empty}")
    private String name;
    @NotBlank(message = "{validation.user.username.empty}")
    private String userName;
    @NotBlank(message = "{validation.user.contact.empty}")
    private String contactNo;
    @NotBlank(message = "{validation.user.email.empty}")
    @Email(message = "{validation.user.email.invalid}")
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Positive(message = "{validation.user.device.positive}")
    private Integer deviceId;
    @NotBlank(message = "{validation.user.status.empty}")
    private String status;
    @Positive(message = "{validation.user.usertype.positive}")
    private Integer userType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Positive(message = "{validation.user.merchant.positive}")
    private Integer merchantId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Positive(message = "{validation.user.merchant.partner}")
    private Integer partnerId;
}
