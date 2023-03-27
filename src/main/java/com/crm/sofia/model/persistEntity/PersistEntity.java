package com.crm.sofia.model.persistEntity;

import com.crm.sofia.model.common.MainEntity;
import com.crm.sofia.model.tag.EntityTag;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "persist_entity")
public class PersistEntity extends MainEntity implements Serializable {

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "entity_type", insertable = true, updatable = false, nullable = false )
    private String entitytype;

    @Column(columnDefinition = "TEXT")
    private String query;

    @Column
    private Integer creationVersion;

    @Column
    private String indexes;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            targetEntity=PersistEntityField.class
    )
    @JoinColumn(name = "persist_entity_id", referencedColumnName = "id")
    private List<PersistEntityField> persistEntityFieldList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            targetEntity= ForeignKeyConstrain.class
    )
    @JoinColumn(name = "base_table_id", referencedColumnName = "id")
    private List<ForeignKeyConstrain> foreignKeyConstrainList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "persist_entity_id")
    private List<EntityTag> tags;

    public void removeForeignKeyConstrain(ForeignKeyConstrain foreignKeyConstrain){
        foreignKeyConstrainList.remove(foreignKeyConstrain);
    }

}
