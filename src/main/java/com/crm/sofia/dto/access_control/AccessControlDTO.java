package com.crm.sofia.dto.access_control;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.user.RoleDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class AccessControlDTO extends BaseDTO {

    private String type;

    private String entityId;

    private String entityName;

    private Boolean createEntity;

    private Boolean updateEntity;

    private Boolean readEntity;

    private Boolean deleteEntity;

    private RoleDTO role;

    private String roleId;

    private  String roleName;



    public AccessControlDTO(String id,String type, String entityId,String entityName ,Boolean createEntity,
                            Boolean updateEntity, Boolean readEntity, Boolean deleteEntity,
                            String roleId, String roleName) {
        this.setId(id);
        this.type = type;
        this.entityId = entityId;
        this.entityName =entityName;
        this.createEntity = createEntity;
        this.updateEntity = updateEntity;
        this.readEntity = readEntity;
        this.deleteEntity = deleteEntity;
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
