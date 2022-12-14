package com.crm.sofia.mapper.language;

import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.mapper.common.BaseMapper;
import com.crm.sofia.model.language.Language;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class LanguageMapper extends BaseMapper<LanguageDTO, Language> {
}
