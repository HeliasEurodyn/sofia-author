package com.crm.sofia.model.form;

import com.crm.sofia.model.common.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "FormArea")
@Table(name = "form_area")
public class FormArea extends BaseEntity {

    @Column
    public String code;

    @Column
    public String title;

    @Column
    private String description;

    @Column
    private String icon;

    @Column
    private String cssclass;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.ALL },
            orphanRemoval=true
    )
    @JoinColumn(name = "form_area_id")
    private List<FormControl> formControls;

    @Column(name = "short_order")
    private Long shortOrder;
}
