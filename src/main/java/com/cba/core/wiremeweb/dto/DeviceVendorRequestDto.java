package com.cba.core.wiremeweb.dto;

import com.cba.core.wiremeweb.validator.ValidVariable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceVendorRequestDto implements java.io.Serializable {

    @ValidVariable(message = "{validation.vendor.name.empty}")
    private String name;
    @NotBlank(message = "{validation.vendor.img.empty}")
    private String img;
    @NotBlank(message = "{validation.vendor.status.empty}")
    private String status;

    private static final long serialVersionUID = 1L;

}
