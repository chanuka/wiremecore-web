package com.cba.core.wiremeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailRequestDto {
    private String mailTo;
    private String subject;
    private String body;
    private List<Object> attachments;
    private Map<String, Object> props;
}
