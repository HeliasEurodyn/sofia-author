package com.crm.sofia.model.persistEntity;

import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.common.MainEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "foreign_key_constrain")
public class ForeignKeyConstrain extends BaseEntity {

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_table_id")
    private PersistEntity referredTable;
    @Column(name = "field_name")
    private String fieldName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_field_id")
    private PersistEntityField referredField;
    @Column(name = "on_update")
    private String onUpdate;

    @Column(name = "on_delete")
    private String onDelete;

    @Column(name = "short_order")
    private Long shortOrder;

}
