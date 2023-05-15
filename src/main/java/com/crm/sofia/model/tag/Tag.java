package com.crm.sofia.model.tag;

import com.crm.sofia.model.common.MainEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Entity(name = "Tag")
@Table(name = "tag")
public class Tag extends MainEntity {

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String color;

}
