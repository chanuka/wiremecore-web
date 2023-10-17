package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TerminalResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String terminalId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer merchantId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer deviceId;
}
