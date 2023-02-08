package com.crm.sofia.mapper.access_control;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.mapper.common.BaseMapper;
import com.crm.sofia.model.access_control.AccessControl;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class PermissionMapper extends BaseMapper<AccessControlDTO, AccessControl> {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target="createEntity", source="createEntity")
    @Mapping(target="updateEntity", source="updateEntity")
    @Mapping(target="readEntity", source="readEntity")
    @Mapping(target="deleteEntity", source="deleteEntity")
    public abstract void map(AccessControlDTO dto, @MappingTarget AccessControl entity);

}
