package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceConfigResponseDto implements Serializable {

    private Integer id;
    private String configType;
    private Integer deviceId;
    private String baseUrl;
    private String idleTimeout;
    private List<DeviceHostDto> hosts;
    private List<DeviceConfigMerchantDto> merchants;
    private String status;

    private static final long serialVersionUID = 1L;
}

