package com.crm.sofia.model.menu;

import com.crm.sofia.model.access_control.AccessControl;
import com.crm.sofia.model.common.MainEntity;
import com.crm.sofia.model.tag.Tag;
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
@Entity(name = "Menu")
@Table(name = "menu")
public class Menu extends MainEntity {

    @Column
    private Boolean accessControlEnabled;

    @Column
    private String name;


    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.ALL },
            orphanRemoval=true
    )
    @JoinColumn(name = "menu_id")
    private List<MenuField> menuFieldList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "menu_id")
    private List<AccessControl> accessControls;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "menu_id")
    private List<MenuTranslation> translations;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "entity_tag",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id")
            }
    )
    private List<Tag> tags;
}
