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

    @ValidVariable(message = "{validation.model.name.empty }")
    private String name;
    @NotBlank(message = "{validation.model.img.empty}")
    private String img;
    @Positive(message = "{validation.model.vendor.positive}")
    private Integer deviceVendor;
    @NotBlank(message = "{validation.model.status.empty}")
    private String status;

    private static final long serialVersionUID = 1L;

}
