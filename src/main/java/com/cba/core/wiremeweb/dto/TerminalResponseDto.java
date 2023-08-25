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
public class TerminalResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String terminalId;
    private Integer merchantId;
    private String status;
    private Integer deviceId;
}
