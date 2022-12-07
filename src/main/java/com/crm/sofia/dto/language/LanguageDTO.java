package com.crm.sofia.dto.language;

import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class LanguageDTO extends BaseDTO {

    private String code;

    private String name;

    private String image;

    public LanguageDTO(String id, String code, String name, Instant modifiedOn) {
        this.setId(id);
        this.setCode(code);
        this.setName(name);
        this.setModifiedOn(modifiedOn);
    }
}
