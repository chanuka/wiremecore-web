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
public class GraphRequestDto implements Serializable {

    private String configName;
    private String configType;
    private String configTitle;
    private String dateClustering;
    private String graphType;
    private String tag;
    private SelectionScope selectionScope;
    private String grouping;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String aggregator;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String xaxis;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String yaxis;
    private String status;
    private Integer priorityOrder;

    private static final long serialVersionUID = 1L;

}

