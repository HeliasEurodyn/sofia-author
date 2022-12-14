package com.crm.sofia.dto.report;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ReportDTO extends BaseDTO {

    private String code;

    private String name;

    private String type;

    private List<ReportParameterDTO> reportParameterList;

    private String reportFilename;

    private String reportType;

    private List<ReportDTO> subreports;

    private Boolean accessControlEnabled;

    private List<AccessControlDTO> accessControls;

    public ReportDTO(String id, String code, String name, Instant modifiedOn) {
        this.setId(id);
        this.code = code;
        this.name = name;
        this.setModifiedOn(modifiedOn);
    }
}
