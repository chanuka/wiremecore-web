package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceVendorResponseDto implements java.io.Serializable {

    private Integer id;
    private String name;
    private String img;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DeviceModelResponseDto> deviceModels;

    private static final long serialVersionUID = 1L;
}
