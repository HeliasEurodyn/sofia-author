package com.crm.sofia.model.menu;

import com.crm.sofia.model.common.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Setter
@Entity(name = "Menu")
@Table(name = "menu")
@Accessors(chain = true)
@DynamicUpdate
public class Menu extends BaseEntity {

    @Column
    private String name;

//    @Column
//    private Integer shortOrder;

    @OneToMany(
            mappedBy = "menu",
            fetch = FetchType.LAZY,
            cascade = { CascadeType.ALL },
            orphanRemoval=true
    )
    private List<MenuField> menuFieldList;


}