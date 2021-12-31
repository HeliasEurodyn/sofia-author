package com.crm.sofia.model.sofia.access_control;

import com.crm.sofia.model.common.BaseEntity;
import com.crm.sofia.model.sofia.menu.Menu;
import com.crm.sofia.model.sofia.user.Role;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@Entity(name = "AccessControl")
@Table(name = "access_control")
public class AccessControl extends BaseEntity implements Serializable {

    @Column
    private String type;

    @Column
    private Long entityId;

    @Column
    private Boolean createEntity;

    @Column
    private Boolean updateEntity;

    @Column
    private Boolean readEntity;

    @Column
    private Boolean deleteEntity;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
