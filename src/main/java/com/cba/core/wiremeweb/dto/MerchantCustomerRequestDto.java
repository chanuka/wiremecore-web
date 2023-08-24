package com.cba.core.wiremeweb.dto;

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
public class MerchantCustomerRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "contact Number is required")
    private String contactNo;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is Invalid")
    private String email;
    @NotBlank(message = "Status is required")
    private String status;

}
