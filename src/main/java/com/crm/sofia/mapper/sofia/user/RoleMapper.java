package com.crm.sofia.mapper.sofia.user;

import com.crm.sofia.dto.sofia.user.RoleDTO;
import com.crm.sofia.mapper.common.BaseMapper;
import com.crm.sofia.model.sofia.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RoleMapper extends BaseMapper<RoleDTO, Role> {
}
