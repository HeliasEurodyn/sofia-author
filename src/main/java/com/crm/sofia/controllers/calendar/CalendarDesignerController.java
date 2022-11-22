package com.crm.sofia.controllers.calendar;

import com.crm.sofia.dto.calendar.CalendarDTO;
import com.crm.sofia.model.calendar.Calendar;
import com.crm.sofia.services.calendar.CalendarDesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/calendar-designer")
public class CalendarDesignerController {


    @Autowired
    CalendarDesignerService calendarDesignerService;

    @GetMapping
    List<CalendarDTO> getObject() {return calendarDesignerService.getObject();}



    @GetMapping(path = "/by-id")
    CalendarDTO getObject(@RequestParam("id") String id){return calendarDesignerService.getObject(id);}



    @PostMapping
    public CalendarDTO postObject(@RequestBody CalendarDTO calendarDTO) throws IOException{
        return calendarDesignerService.postObject(calendarDTO);
    }


    @PutMapping
    public CalendarDTO putObject(@RequestBody CalendarDTO calendarDTO){
        return calendarDesignerService.postObject(calendarDTO);
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {calendarDesignerService.deleteObject(id);}





}
