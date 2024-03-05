package com.cba.core.wiremeweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "{validation.highlights.config_name.empty}")
    private String configName;
    @NotBlank(message = "{validation.highlights.config_type.empty}")
    private String configType;
    @NotBlank(message = "{validation.highlights.config_title.empty}")
    private String configTitle;
    @NotBlank(message = "{validation.highlights.clustering.empty}")
    private String dateClustering;
    @NotBlank(message = "{validation.graph.type.empty}")
    private String graphType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tag;
    private SelectionScopeDto selectionScope;
    @NotBlank(message = "{validation.highlights.grouping.empty}")
    private String grouping;
    @NotBlank(message = "{validation.highlights.aggregator.empty}")
    private String aggregator;
    @NotBlank(message = "{validation.graph.x.empty}")
    private String xaxis;
    @NotBlank(message = "{validation.graph.y.empty}")
    private String yaxis;
    @NotBlank(message = "{validation.highlights.status.empty}")
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fromDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String toDate;
    @NotBlank(message = "{validation.highlights.priority.empty}")
    private Integer priorityOrder;

    private static final long serialVersionUID = 1L;

}

