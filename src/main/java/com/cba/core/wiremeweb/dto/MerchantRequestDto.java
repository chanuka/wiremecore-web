package com.cba.core.wiremeweb.dto;

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
public class MerchantRequestDto implements Serializable {


    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{validation.merchant.merchant_id.empty}")
    private String merchantId;
    @Positive(message = "{validation.merchant.partner.positive}")
    private Integer partnerId;
    @NotBlank(message = "{validation.merchant.name.empty}")
    private String name;
    @NotBlank(message = "{validation.merchant.email.empty}")
    @Email(message = "{validation.merchant.email.invalid}")
    private String email;
    @NotBlank(message = "{validation.merchant.province.empty}")
    private String province;
    @NotBlank(message = "{validation.merchant.address.empty}")
    private String address;
    @NotBlank(message = "{validation.merchant.mcc.empty}")
    private String mcc;
    @NotBlank(message = "{validation.merchant.district.empty}")
    private String district;
    @NotBlank(message = "{validation.merchant.status.empty}")
    private String status;
    @Positive(message = "{validation.merchant.lat.positive}")
    private Float lat;
    @Positive(message = "{validation.merchant.lon.positive}")
    private Float lon;
    @Positive(message = "{validation.merchant.radius.positive}")
    private Integer radius;

}
