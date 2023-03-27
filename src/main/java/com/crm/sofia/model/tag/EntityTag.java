package com.crm.sofia.model.tag;

import com.crm.sofia.model.common.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Entity(name = "EntityTag")
@Table(name = "entity_tag")
public class EntityTag extends BaseEntity {

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String color;

}

