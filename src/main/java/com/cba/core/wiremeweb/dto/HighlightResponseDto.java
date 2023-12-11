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
public class HighlightResponseDto implements Serializable {

    private String configName;
    private String configType;
    private String configTitle;
    private String dateClustering;
    private String tag;
    private SelectionScopeDto selectionScope;
    private String grouping;
    private String aggregator;
    private String status;
    private Integer priorityOrder;

    private static final long serialVersionUID = 1L;
}
