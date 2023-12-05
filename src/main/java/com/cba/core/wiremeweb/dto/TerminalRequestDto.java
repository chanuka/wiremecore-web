package com.cba.core.wiremeweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "{validation.terminal.empty}")
    @Size(max = 8, message = "{validation.terminal.maxlength}")
    private String terminalId;
    @NotBlank(message = "{validation.merchant.empty}")
    @Size(max = 16, message = "{validation.merchant.maxlength}")
    private String merchantId;
//    @NotBlank(message = "{validation.device.empty}")
    private Integer deviceId;
    @NotBlank(message = "{validation.status.empty}")
    private String status;
}
