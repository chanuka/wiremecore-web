package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class DeviceConfigRequestDto implements Serializable {


    @Positive(message = "{validation.device_config.device.positive}")
    private Integer deviceId;
    @NotBlank(message = "{validation.device_config.type.empty}")
    private String configType;
    @NotBlank(message = "{validation.device_config.baseUrl.empty}")
    private String baseUrl;
    @NotBlank(message = "{validation.device_config.idle_timeout.empty}")
    private String idleTimeout;
    @NotBlank(message = "{validation.device_config.status.empty}")
    private String status;
    private List<DeviceHostDto> hosts;
    private List<DeviceConfigMerchantDto> merchants;

    private static final long serialVersionUID = 1L;
}

