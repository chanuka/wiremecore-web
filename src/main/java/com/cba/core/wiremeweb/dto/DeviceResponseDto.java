package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serialNo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String emiNo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deviceType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer modelId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
//    private String statusDescription;
//    private String createdUser;
//    private String lastUpdatedUser;
//    private String uniqueId;
//    private String createdAt;
//    private String updatedAt;

    private static final long serialVersionUID = 1L;
}
