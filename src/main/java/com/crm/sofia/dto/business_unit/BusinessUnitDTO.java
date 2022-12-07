package com.crm.sofia.dto.business_unit;

import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class BusinessUnitDTO extends BaseDTO {

    @NotNull
    @NotBlank
    private String title;


    private String description;

    public BusinessUnitDTO(String id, String title, Instant modifiedOn) {
        this.setId(id);
        this.title = title;
        this.setModifiedOn(modifiedOn);
    }
}
