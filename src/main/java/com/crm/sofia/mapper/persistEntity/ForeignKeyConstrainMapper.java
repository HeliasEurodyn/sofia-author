package com.crm.sofia.mapper.persistEntity;

import com.crm.sofia.dto.table.ForeignKeyConstrainDTO;
import com.crm.sofia.mapper.table.TableMapper;
import com.crm.sofia.model.persistEntity.ForeignKeyConstrain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Named("ForeignKeyConstrainMapper")
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {TableMapper.class})
public abstract class ForeignKeyConstrainMapper {


    @Named("ignoreReferredTableFKDTOToEntity")
    @Mapping(target = "referredTable", qualifiedByName ="mapIgnoringForeignKeysDTOToEntity" )
    public abstract ForeignKeyConstrain map(ForeignKeyConstrainDTO dto);



    @Named("ignoreReferredTableFKEntityToDTO")
    @Mapping(target = "referredTable", qualifiedByName ="mapIgnoringForeignKeysEntityToDTO" )
    public abstract ForeignKeyConstrainDTO map(ForeignKeyConstrain entity);
}
