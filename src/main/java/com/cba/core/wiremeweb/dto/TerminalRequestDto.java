package com.cba.core.wiremeweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "{validation.terminal.id.empty}")
    @Size(max = 8, message = "{validation.terminal.id.maxlength}")
    private String terminalId;
    @NotBlank(message = "{validation.terminal.merchant.empty}")
    @Size(max = 16, message = "{validation.terminal.merchant.maxlength}")
    private String merchantId;
    @Positive(message = "{validation.terminal.device.positive}")
    private Integer deviceId;
    @NotBlank(message = "{validation.terminal.status.empty}")
    private String status;
}
