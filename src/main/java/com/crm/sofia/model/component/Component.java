package com.crm.sofia.model.component;

import com.crm.sofia.model.access_control.AccessControl;
import com.crm.sofia.model.common.MainEntity;
import com.crm.sofia.model.tag.Tag;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "Component")
@Table(name = "component")
public class Component extends MainEntity implements Serializable {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Boolean accessControlEnabled;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "component_id")
    private List<ComponentPersistEntity> componentPersistEntityList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "component_id")
    private List<AccessControl> accessControls;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "entity_tag",
            joinColumns = @JoinColumn(name = "component_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id")
            }
    )
    private List<Tag> tags;

    public Stream<ComponentPersistEntity> flatComponentPersistEntityTree() {
        return this.getComponentPersistEntityList().stream()
                .flatMap(ComponentPersistEntity::streamTree);
    }

}
