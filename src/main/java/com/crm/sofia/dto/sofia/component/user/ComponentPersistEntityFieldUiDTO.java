package com.crm.sofia.dto.sofia.component.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ComponentPersistEntityFieldUiDTO {

    private Long id;

    private String code;

    private String defaultValue;

    private Object value;

    ComponentPersistEntityFieldAssignmentUiDTO assignment;

}
