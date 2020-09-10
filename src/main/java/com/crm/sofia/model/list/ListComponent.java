package com.crm.sofia.model.list;

import com.crm.sofia.dto.list.ListComponentFieldDTO;
import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.component.Component;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "ListComponent")
@Table(name = "list_component")
public class ListComponent extends BaseEntity {


    @Column
    private String code;

    @Column
    private String selector;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = com.crm.sofia.model.component.Component.class)
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private Component component;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "column_list_component_id")
    private List<ListComponentField> listComponentColumnFieldList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "filter_list_component_id")
    private List<ListComponentField> listComponentFilterFieldList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "side_group_list_component_id")
    private List<ListComponentField> listComponentLeftGroupFieldList;


    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "top_group_list_component_id")
    private List<ListComponentField> listComponentTopGroupFieldList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "order_by_list_component_id")
    private List<ListComponentField> listComponentOrderByFieldList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "action_list_component_id")
    private List<ListComponentField> listComponentActionFieldList;

    @Column(columnDefinition = "TEXT")
    private String filterFieldStructure;

    @Column
    private Boolean customFilterFieldStructure;

    @Column
    private Boolean exportExcel;

    @Column
    private String defaultPage;

    @Column
    private Boolean hasPagination;

    @Column
    private Long totalPages;

    @Column
    private Long rowsLimit;

    @Column
    private Boolean HeaderFilters;

    @Column
    private String rowNavigation;

}
