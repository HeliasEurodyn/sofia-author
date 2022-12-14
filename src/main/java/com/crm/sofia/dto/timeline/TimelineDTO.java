package com.crm.sofia.dto.timeline;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.list.ListComponentFieldDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class TimelineDTO extends BaseDTO {

    @NotNull(message = "Title Cannot Be Null")
    @NotBlank(message = "Title Cannot Be Blank")
    private String title;

    private String description;

    private String icon;

    @NotNull
    private String query;

    private boolean hasPagination;

    private Integer pageSize;

    private List<ListComponentFieldDTO> filterList;

    public TimelineDTO(String id, String title, Instant modifiedOn) {
        this.setId(id);
        this.title = title;
        this.setModifiedOn(modifiedOn);
    }
}
