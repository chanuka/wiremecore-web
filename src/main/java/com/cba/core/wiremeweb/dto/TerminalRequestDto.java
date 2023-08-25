package com.cba.core.wiremeweb.dto;

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
public class TerminalRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Terminal ID is required")
    private String terminalId;
//    @NotEmpty(message = "Merchant ID is required")
    private Integer merchantId;
//    @NotEmpty(message = "Device ID is required")
    private Integer deviceId;
    @NotBlank(message = "Status is required")
    private String status;
}
