package com.crm.sofia.mapper.tag;

import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.mapper.common.BaseMapper;
import com.crm.sofia.model.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class TagMapper extends BaseMapper<TagDTO, Tag> {
}
