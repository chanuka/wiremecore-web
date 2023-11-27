package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DistrictDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private float lat;
    private float lon;

}
