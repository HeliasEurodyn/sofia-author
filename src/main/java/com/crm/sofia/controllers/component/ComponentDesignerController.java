package com.crm.sofia.controllers.component;

import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityFieldDTO;
import com.crm.sofia.services.component.ComponentDesignerService;
import com.crm.sofia.services.form.FormDynamicQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping("/component-designer")
public class ComponentDesignerController {
    @Autowired
    private  ComponentDesignerService componentDesignerService;
    @Autowired
    private  FormDynamicQueryService formDynamicQueryService;



    @GetMapping
    List<ComponentDTO> getObject() {
        return this.componentDesignerService.getObject();
    }

    @GetMapping(path = "/by-id")
    ComponentDTO getObject(@RequestParam("id") String id) {
        return this.componentDesignerService.getObject(id);
    }

    @PostMapping
    public ComponentDTO postObject(@RequestBody ComponentDTO dto) {
        ComponentDTO createdDTO = this.componentDesignerService.postObject(dto);
        return createdDTO;
    }

    @PutMapping
    public ComponentDTO putObject(@RequestBody ComponentDTO dto) {
        ComponentDTO createdDTO = this.componentDesignerService.putObject(dto);
        return createdDTO;
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        this.componentDesignerService.deleteObject(id);
    }

    @GetMapping(path = "/component-persist-entity/data/by-id")
    ComponentPersistEntityDTO getComponentPersistEntityDataById(@RequestParam("component-persist-entity-id") String id,
                                                                @RequestParam("selection-id") String selectionId) {
        ComponentPersistEntityDTO componentPersistEntityDTO = this.componentDesignerService.getComponentPersistEntityDataById(id, selectionId);

        List<ComponentPersistEntityFieldDTO> retrievalFieldList = componentPersistEntityDTO.getComponentPersistEntityFieldList()
                .stream()
                .filter(x -> (x.getPersistEntityField().getPrimaryKey() == null ? false : x.getPersistEntityField().getPrimaryKey()))
                .collect(Collectors.toList());

        retrievalFieldList
                .stream()
                .forEach(x -> x.setLocateStatement(selectionId));

        componentPersistEntityDTO = this.formDynamicQueryService.retrieveComponentPersistEntity(componentPersistEntityDTO,
                retrievalFieldList);

        return componentPersistEntityDTO;
    }

}
