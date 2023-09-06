package com.cba.core.wiremeweb.dto;

import com.cba.core.wiremeweb.validator.ValidVariable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceRequestDto implements java.io.Serializable {

    @ValidVariable(message = "serialNo is required")
    private String serialNo;
    @NotBlank(message = "IMEI is required")
    @Pattern(regexp = "\\d+", message = "Value must contain only numeric digits")
    private String emiNo;
    @NotBlank(message = "Device Type is required")
    private String deviceType;
    @NotBlank(message = "Status is required")
    private String status;
    private int deviceId;

    private static final long serialVersionUID = 1L;

}
