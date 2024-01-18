package com.cba.core.wiremeweb.dto;

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
public class SelectionScopeDto implements Serializable {

    @NotBlank(message = "{validation.selection_scope.partner.empty}")
    private String partner;
    @NotBlank(message = "{validation.selection_scope.merchant.empty}")
    private String merchant;
    @NotBlank(message = "{validation.selection_scope.province.empty}")
    private String province;
    @NotBlank(message = "{validation.selection_scope.district.empty}")
    private String district;

    private static final long serialVersionUID = 1L;

}
