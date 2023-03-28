package com.crm.sofia.model.custom_query;

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
@Entity(name = "CustomQuery")
@Table(name = "custom_query")
public class CustomQuery extends MainEntity {

    @Column
    private String code;

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String query;

    @Column
    private Boolean accessControlEnabled;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    @JoinColumn(name = "custom_query_id")
    private List<AccessControl> accessControls;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "entity_tag",
            joinColumns = @JoinColumn(name = "custom_query_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id")
            }
    )
    private List<Tag> tags;
}
