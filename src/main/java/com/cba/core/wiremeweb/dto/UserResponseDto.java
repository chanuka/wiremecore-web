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
public class UserResponseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contactNo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer deviceId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String merchantName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String partnerName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer partnerId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer merchantId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
}
