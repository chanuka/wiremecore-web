package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceVendorResponseDto implements java.io.Serializable {

    private Integer id;
    private String name;
    private String img;
    private String status;

    private static final long serialVersionUID = 1L;
}
