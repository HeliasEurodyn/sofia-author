package com.crm.sofia.dto.sofia.list.user;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.sofia.component.designer.ComponentDTO;
import com.crm.sofia.dto.sofia.component.user.ComponentUiDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"createdOn","createdBy","shortOrder","version", "name",
        "selector",
        "filterFieldStructure",
        "customFilterFieldStructure",
        "totalPages",
        "currentPage",
        "totalRows",
        "rowNavigation",
        "jsonUrl",
        "component"}
        )

@Accessors(chain = true)
public class ListUiDTO extends BaseDTO {

    private String code;
    private String name;
    private String headerTitle;
    private String HeaderDescription;
    private String headerIcon;
    private String title;
    private String description;
    private String groupingTitle;
    private String groupingDescription;
    private String icon;
    private String selector;
    private String filterFieldStructure;
    private Boolean customFilterFieldStructure;
    private Boolean exportExcel;
    private String defaultPage;
    private Boolean autoRun;
    private Boolean listVisible;
    private Boolean filterVisible;
    private Boolean hasPagination;
    private Long pageSize;
    private Long totalPages;
    private Long currentPage;
    private Long totalRows;
    private Boolean hasMaxSize;
    private Long maxSize;
    private Boolean HeaderFilters;
    private String rowNavigation;
    private String jsonUrl;
    private ComponentUiDTO component;
    private Long instanceVersion;

    private List<ListActionButtonUiDTO> listActionButtons;
    private List<ListComponentFieldUiDTO> listComponentColumnFieldList;
    private List<ListComponentFieldUiDTO> listComponentFilterFieldList;
    private List<ListComponentFieldUiDTO> listComponentLeftGroupFieldList;
    private List<ListComponentFieldUiDTO> listComponentTopGroupFieldList;
    private List<ListComponentFieldUiDTO> listComponentOrderByFieldList;
    private List<ListComponentFieldUiDTO> listComponentActionFieldList;
}
