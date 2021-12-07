package com.crm.sofia.model.sofia.search;

import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.sofia.access_control.AccessControl;
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
@Entity(name = "Search")
@Table(name = "search")
public class Search extends BaseEntity {

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
    @JoinColumn(name = "search_id")
    private List<AccessControl> accessControls;
}
