package com.crm.sofia.services.calendar;

import com.crm.sofia.dto.calendar.CalendarDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.calendar.CalendarMapper;
import com.crm.sofia.model.calendar.Calendar;
import com.crm.sofia.repository.calendar.CalendarRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarDesignerService {


    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    CalendarMapper calendarMapper;

    @Autowired
    JWTService jwtService;

    public CalendarDesignerService() {
    }


    public List<CalendarDTO> getObject() {
        List<Calendar> entities = calendarRepository.findAll();
        return calendarMapper.mapEntitiesForList(entities);
    }

    public CalendarDTO getObject(String id) {
        Calendar calendar = calendarRepository.findById(id).orElseThrow(() ->new DoesNotExistException("Calendar Designer Does Not Exist"));
        CalendarDTO dto = calendarMapper.map(calendar);

        String encodedQuery = Base64.getEncoder().encodeToString(dto.getQuery().getBytes(StandardCharsets.UTF_8));
        dto.setQuery(encodedQuery);
        return dto;
    }

    public CalendarDTO postObject(CalendarDTO calendarDTO) {
        if (calendarDTO.getQuery() != null) {
            byte[] decodedQuery = Base64.getDecoder().decode(calendarDTO.getQuery());
            calendarDTO.setQuery(new String(decodedQuery));
        }



        Calendar calendar = calendarMapper.map(calendarDTO);
        if (calendar.getId() == null) {
            calendar.setCreatedOn(Instant.now());
            calendar.setCreatedBy(jwtService.getUserId());
        }
        calendar.setModifiedOn(Instant.now());
        calendar.setModifiedBy(jwtService.getUserId());
        Calendar savedData = calendarRepository.save(calendar);

        return calendarMapper.map(savedData);
    }

    public void deleteObject(String id) {
        Optional<Calendar> optionalEntity = Optional.ofNullable(calendarRepository.findById(id)).
                orElseThrow(()->new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Object does not exist"));

        calendarRepository.deleteById(optionalEntity.get().getId());
    }


}
