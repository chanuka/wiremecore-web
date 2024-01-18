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

    @NotBlank(message = "{validation.partner.name.empty}")
    private String name;
    @NotBlank(message = "{validation.partner.address.empty}")
    private String address;
    @NotBlank(message = "{validation.partner.contact.empty}")
    private String contactNo;
    @NotBlank(message = "{validation.partner.email.empty}")
    @Email(message = "{validation.partner.email.invalid}")
    private String email;
    @NotBlank(message = "{validation.partner.status.empty}")
    private String status;

}
