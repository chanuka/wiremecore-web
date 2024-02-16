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
public class TerminalEmailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String merchantId;
    private String terminalId;
    private String email;
    private String contactNo;
    private String province;
    private String district;
    private String address;
    private String mccDescription;
    private String mcc;
    private Float lat;
    private Float lng;
    private String merchantName;
    private String serialNo;
    private String emiNo;
    private String deviceType;
    private String uniqueId;
    private String deviceModelName;
    private Integer deviceModelId;
    private String venderName;
    private Integer venderId;
    private Boolean isVoidEnabled;
    private Boolean isPreauthEnabled;
    private Boolean isOfflineEnabled;
    private Boolean isMkeEnabled;
    private String contactPerson;
    private String merchantPassword;

}
