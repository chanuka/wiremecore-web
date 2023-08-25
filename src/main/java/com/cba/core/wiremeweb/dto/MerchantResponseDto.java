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
public class MerchantResponseDto implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer id;
    private String merchantId;
    private Integer partnerId;
    private String name;
    private String email;
    private String province;
    private String district;
    private String status;
}
