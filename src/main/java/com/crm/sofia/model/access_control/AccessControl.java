package com.crm.sofia.model.access_control;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.model.common.MainEntity;
import com.crm.sofia.model.user.Role;
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
@NamedNativeQuery(name = "AccessControl.getAccessControlList",query = "SELECT\n" +
        "a.id as id,\n" +
        "a.type AS type , \n" +
        "a.entity_id AS entity_id,\n" +
        "CASE \n" +
        "     WHEN a.type = 'list' THEN l.name \n" +
        "     WHEN a.type = 'form' THEN f.name\n" +
        "     WHEN a.type = 'menu' THEN m.name\n" +
        "     WHEN a.type = 'search' THEN s.name\n" +
        "     WHEN a.type = 'report' THEN rep.name\n" +
        "     WHEN a.type = 'xls_import' THEN xi.name\n" +
        "     ELSE ''\n" +
        "END AS entity_name,\n" +
        "a.role_id AS role_id, \n" +
        "r.name AS role_name,\n" +
        "a.read_entity AS _read,\n" +
        "a.create_entity AS _create, \n" +
        "a.update_entity AS _update, \n" +
        "a.delete_entity AS _delete\n" +
        "FROM `access_control` a\n" +
        "INNER JOIN role r ON r.id = a.role_id\n" +
        "LEFT JOIN list l ON l.id = a.list_id AND a.type = 'list'\n" +
        "LEFT OUTER JOIN form f ON f.id = a.form_id AND a.type = 'form'\n" +
        "LEFT OUTER JOIN menu m ON m.id = a.menu_id AND a.type = 'menu'\n" +
        "LEFT OUTER JOIN search s ON s.id = a.search_id AND a.type = 'search'\n" +
        "LEFT OUTER JOIN report rep ON rep.id = a.report_id AND a.type = 'report'\n" +
        "LEFT OUTER JOIN xls_import xi ON xi.id = a.xls_import_id AND a.type = 'xls_import' ORDER by type,entity_name",resultSetMapping = "Mapping.AccessControlDTO")
@SqlResultSetMapping(name ="Mapping.AccessControlDTO",classes = @ConstructorResult(targetClass = AccessControlDTO.class,columns = {
        @ColumnResult(name = "id"),
        @ColumnResult(name = "type"),
        @ColumnResult(name = "entity_id"),
        @ColumnResult(name = "entity_name"),
        @ColumnResult(name = "_create"),
        @ColumnResult(name = "_update"),
        @ColumnResult(name = "_read"),
        @ColumnResult(name = "_delete"),
        @ColumnResult(name = "role_id"),
        @ColumnResult(name = "role_name")
}))
@Entity(name = "AccessControl")
@Table(name = "access_control")
public class AccessControl extends MainEntity implements Serializable {

    @Column
    private String type;

    @Column
    private String entityId;

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
