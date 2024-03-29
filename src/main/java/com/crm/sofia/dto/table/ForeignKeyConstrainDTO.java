package com.crm.sofia.dto.table;

import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ForeignKeyConstrainDTO extends BaseDTO {

    private String name;

    private TableDTO referredTable;

    private  TableFieldDTO  referredField;

    private String fieldName;

    private String onUpdate;

    private String onDelete;

}
