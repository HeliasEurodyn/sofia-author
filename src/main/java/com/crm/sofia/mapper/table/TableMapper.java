package com.crm.sofia.mapper.table;

import com.crm.sofia.dto.table.TableDTO;
import com.crm.sofia.mapper.common.BaseMapper;
import com.crm.sofia.mapper.persistEntity.ForeignKeyConstrainMapper;
import com.crm.sofia.model.persistEntity.PersistEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Named("TableMapper")
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS ,uses = ForeignKeyConstrainMapper.class)
public abstract class TableMapper extends BaseMapper<TableDTO, PersistEntity> {

    @Mapping(ignore = true, target = "modifiedBy")
    @Mapping(ignore = true, target = "modifiedOn")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(target = "persistEntityFieldList", source = "dto.tableFieldList")
    @Mapping(target = "foreignKeyConstrainList", source = "dto.foreignKeyConstrainList", qualifiedByName = "ignoreReferredTableFKDTOToEntity")
    public abstract PersistEntity map(TableDTO dto);

    @Mapping(target = "tableFieldList", source = "entity.persistEntityFieldList")
    @Mapping(target = "foreignKeyConstrainList", source = "entity.foreignKeyConstrainList",qualifiedByName = "ignoreReferredTableFKEntityToDTO")
    public abstract TableDTO map(PersistEntity entity);

    @Named("mapIgnoringForeignKeysEntityToDTO")
    @Mapping(target = "tableFieldList", source = "entity.persistEntityFieldList")
    @Mapping(ignore = true,target = "foreignKeyConstrainList")
    public abstract TableDTO mapIgnoringForeignKeysEntityToDTO(PersistEntity entity);


    @Named("mapIgnoringForeignKeysDTOToEntity")
    @Mapping(ignore = true, target = "modifiedBy")
    @Mapping(ignore = true, target = "modifiedOn")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(target = "persistEntityFieldList", source = "dto.tableFieldList")
    @Mapping(ignore = true, target = "foreignKeyConstrainList")
    public abstract PersistEntity mapIgnoringForeignKeysDTOToEntity(TableDTO dto);

    public List<TableDTO> map(List<PersistEntity> all) {
        return all.stream().map(this::map).collect(Collectors.toList());
    }
}
