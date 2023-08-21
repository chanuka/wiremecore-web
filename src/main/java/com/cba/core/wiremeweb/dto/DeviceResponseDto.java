package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceResponseDto implements java.io.Serializable {

    private Integer id;
    private String serialNo;
    private String emiNo;
    private String deviceType;
    private String status;
//    private String statusDescription;
//    private String createdUser;
//    private String lastUpdatedUser;
//    private String uniqueId;
//    private String createdAt;
//    private String updatedAt;

    private static final long serialVersionUID = 1L;
}
