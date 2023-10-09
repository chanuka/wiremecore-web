package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HighlightRequestDto implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String configName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String configType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String configTitle;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dateClustering;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tag;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SelectionScope selectionScope;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String grouping;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String aggregator;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fromDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String toDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer priorityOrder;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HashMap<String,String> filter;

    private static final long serialVersionUID = 1L;

}

