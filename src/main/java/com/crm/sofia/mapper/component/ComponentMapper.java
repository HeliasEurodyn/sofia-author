package com.crm.sofia.mapper.component;

import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.mapper.common.BaseMapper;
import com.crm.sofia.model.component.Component;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {ComponentPersistEntityFieldMapper.class})
@DecoratedWith(ComponentMapperDecorator.class)
public abstract class ComponentMapper extends BaseMapper<ComponentDTO, Component> {

    public abstract Component mapWithPersistEntities(ComponentDTO entity);

}
