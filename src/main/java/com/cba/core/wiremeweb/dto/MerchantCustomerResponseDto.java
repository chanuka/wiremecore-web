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
public class MerchantCustomerResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String address;
    private String contactNo;
    private String email;
    private String status;

}
