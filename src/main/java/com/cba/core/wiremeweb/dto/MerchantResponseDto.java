package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer partnerId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String province;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String district;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
}
