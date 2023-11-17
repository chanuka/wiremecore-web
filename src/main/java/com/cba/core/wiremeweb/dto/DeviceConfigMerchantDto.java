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
public class DeviceConfigMerchantDto implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hostName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String terminalId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String currencyCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String countryCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nii;

    private static final long serialVersionUID = 1L;

}
