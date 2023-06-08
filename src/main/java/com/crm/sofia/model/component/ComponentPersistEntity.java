package com.crm.sofia.model.component;

import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.persistEntity.PersistEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@Accessors(chain = true)
@Entity(name = "ComponentPersistEntity")
@Table(name = "component_persist_entity")
public class ComponentPersistEntity extends BaseEntity implements Serializable {

    @Column
    Boolean allowRetrieve;
    @Column
    Boolean allowSave;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = PersistEntity.class)
    @JoinColumn(name = "persist_entity_id", referencedColumnName = "id")
    private PersistEntity persistEntity;
    @Column
    private String code;
    @Column
    private String selector;
    @Column
    private String deleteType;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "component_persist_entity_id")
    private List<ComponentPersistEntityField> componentPersistEntityFieldList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "parent_id")
    private List<ComponentPersistEntity> componentPersistEntityList = new ArrayList<>();

    private Boolean multiDataLine;

    @Column(name = "short_order")
    private Long shortOrder;

    public Stream<ComponentPersistEntity> streamTree() {
        return Stream.concat(Stream.of(this), this.getComponentPersistEntityList().stream().flatMap(ComponentPersistEntity::streamTree));
    }
}
