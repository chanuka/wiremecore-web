package com.cba.core.wiremeweb.dto;

import com.cba.core.wiremeweb.validator.ValidVariable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceModelRequestDto implements java.io.Serializable {

    @ValidVariable(message = "Name is required")
    private String name;
    @NotBlank(message = "Status is required")
    private String img;
    @Positive(message = "Device Vendor must be greater than 0")
    private Integer deviceVendor;
    @NotBlank(message = "Status is required")
    private String status;

    private static final long serialVersionUID = 1L;

}
