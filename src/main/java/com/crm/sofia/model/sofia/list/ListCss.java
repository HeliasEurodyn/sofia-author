package com.crm.sofia.model.sofia.list;

import com.crm.sofia.model.common.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "ListCss")
@Table(name = "list_css")
public class ListCss extends BaseEntity implements Serializable {

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String script;

}
