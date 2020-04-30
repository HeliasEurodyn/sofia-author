package com.crm.sofia.controllers.tableDesigner;

import com.crm.sofia.dto.component.CustomComponentDTO;
import com.crm.sofia.dto.component.CustomComponentFieldDTO;
import com.crm.sofia.services.component.CustomComponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@Validated
@RequestMapping("/component")
public class CustomComponentController {

    private final CustomComponentService componentService;

    public CustomComponentController(CustomComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping
    List<CustomComponentDTO> getObject() {
        return this.componentService.getObject();
    }


    @PostMapping
    public CustomComponentDTO postObject(@RequestBody CustomComponentDTO componentDTO) {
        CustomComponentDTO customComponentDTO = this.componentService.postObject(componentDTO);
        this.componentService.createDatabaseTable(customComponentDTO);
        return customComponentDTO;
    }

    @PutMapping
    public CustomComponentDTO putObject(@RequestBody CustomComponentDTO componentDTO) {
        CustomComponentDTO customComponentDTO = this.componentService.putObject(componentDTO);
        List<CustomComponentFieldDTO> fields = this.componentService.putNewObjectFields(componentDTO);

        customComponentDTO.setCustomComponentFieldList(fields);
        this.componentService.updateDatabaseTable(customComponentDTO);

        return customComponentDTO;
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") Long id) {
        CustomComponentDTO customComponentDTO=  this.componentService.getObject(id);
        this.componentService.deleteObject(id);
        this.componentService.deteleDatabaseTable(customComponentDTO.getName());
    }

    @GetMapping(path="/by-id")
    CustomComponentDTO getObject(@RequestParam("id") Long id) {
        return this.componentService.getObject(id);
    }


    @GetMapping(path = "/table-exists")
    public Boolean tableExists(@RequestParam("name") String tableName) {
        return componentService.tableOnDatabase(tableName);
    }

    @GetMapping(path = "/tables")
    public List<String> tables() {

        return componentService.getTables();
    }

    @GetMapping(path = "/fields")
    public List<String> fields() {

        return componentService.getTableFields("testtable");
    }


}
