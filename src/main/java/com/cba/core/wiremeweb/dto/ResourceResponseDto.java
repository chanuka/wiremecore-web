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
public class ResourceResponseDto implements Serializable {

    private Integer id;
    private String name;

    private static final long serialVersionUID = 1L;

}
