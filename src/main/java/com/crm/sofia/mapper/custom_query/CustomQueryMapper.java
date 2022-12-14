package com.crm.sofia.mapper.custom_query;

import com.crm.sofia.dto.custom_query.CustomQueryDTO;
import com.crm.sofia.mapper.common.BaseMapper;
import com.crm.sofia.model.custom_query.CustomQuery;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class CustomQueryMapper extends BaseMapper<CustomQueryDTO, CustomQuery> {
}
