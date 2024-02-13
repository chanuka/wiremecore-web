package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailRequestDto {
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private Boolean isHtml;
//    private List<Object> attachments;
//    private Map<String, Object> props;
}
