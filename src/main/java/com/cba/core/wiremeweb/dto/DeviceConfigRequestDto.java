package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int deviceId;
    @NotBlank(message = "Config Type is required")
    private String configType;
    @NotBlank(message = "Base URL is required")
    private String baseUrl;
    @NotBlank(message = "Idle Timeout is required")
    private String idleTimeout;
    private List<DeviceHostDto> hosts;
    private List<DeviceConfigMerchantDto> merchants;
    @NotBlank(message = "Status is required")
    private String status;

    private static final long serialVersionUID = 1L;
}

