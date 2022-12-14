package com.crm.sofia.dto.component;

import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ComponentPersistEntityDataLineDTO extends BaseDTO {

    private List<ComponentPersistEntityFieldDTO> componentPersistEntityFieldList;

    private List<ComponentPersistEntityDTO> componentPersistEntityList = new ArrayList<>();

}


