package com.crm.sofia.mapper.calendar;


import com.crm.sofia.dto.calendar.CalendarDTO;
import com.crm.sofia.mapper.common.BaseMapper;
import com.crm.sofia.model.calendar.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class CalendarMapper extends BaseMapper<CalendarDTO, Calendar> {

    public List<CalendarDTO> mapEntitiesForList(List<Calendar> entities) {
        return entities.stream().map(this::mapEntityIgnoringQuery).collect(Collectors.toList());
    }

    @Mapping(ignore = true, target = "query")
    public abstract CalendarDTO mapEntityIgnoringQuery(Calendar entity);


}
