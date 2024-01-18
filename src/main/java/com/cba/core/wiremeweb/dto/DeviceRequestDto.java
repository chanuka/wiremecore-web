package com.cba.core.wiremeweb.dto;

import com.cba.core.wiremeweb.validator.ValidVariable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceRequestDto implements java.io.Serializable {

    @ValidVariable(message = "{validation.device.serial_no.empty}")
    private String serialNo;
    @NotBlank(message = "{validation.device.emi_no.empty}")
    @Pattern(regexp = "\\d+", message = "Value must contain only numeric digits")
    private String emiNo;
    @NotBlank(message = "{validation.device.type.empty}")
    private String deviceType;
    @NotBlank(message = "{validation.device.status.empty}")
    private String status;
    @Positive(message = "{validation.device.model.positive}")
    private Integer modelId;
    private Integer deviceId;

    private static final long serialVersionUID = 1L;

}
