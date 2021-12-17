package com.crm.sofia.model.sofia.list;

import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.sofia.component.ComponentPersistEntity;
import com.crm.sofia.model.sofia.component.ComponentPersistEntityField;
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
@Entity(name = "ListComponentField")
@Table(name = "list_component_field")
public class ListComponentField extends BaseEntity {

    @Column
    private String code;

    @Column(columnDefinition = "TEXT")
    private String editor;

    @Column
    private String mask;

    @Column
    private String description;

    @Column
    private String type;

    @ManyToOne(fetch = FetchType.LAZY,
            targetEntity = ComponentPersistEntity.class)
    @JoinColumn(name = "component_persist_entity_id", referencedColumnName = "id")
    private ComponentPersistEntity componentPersistEntity;

    @ManyToOne(fetch = FetchType.LAZY,
            targetEntity = ComponentPersistEntityField.class)
    @JoinColumn(name = "component_persist_entity_field_id", referencedColumnName = "id")
    private ComponentPersistEntityField componentPersistEntityField;

    @Column
    private Boolean visible;

    @Column
    private Boolean editable;

    @Column
    private Boolean headerEditable;

    @Column
    private String editableRelFieldCode;

    @Column
    private Boolean headerFilter;

    @Column
    private Boolean required;

    @Column
    private String defaultValue;

    @Column
    private Integer decimals;

    @Column
    private String fieldtype;

    @Column
    private String shortLocation;

    @Column (columnDefinition = "TEXT")
    private String operator;

    @Column
    private String bclass;

    @Column(columnDefinition = "TEXT")
    private String css;

    @Column
    private String formulaType;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "parent_id")
    private List<ListComponentFieldChild> listComponentActionFieldList;

}
