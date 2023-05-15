package com.crm.sofia.controllers.view;

import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.dto.view.ViewDTO;
import com.crm.sofia.dto.view.ViewFieldDTO;
import com.crm.sofia.services.view.ViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/view")
public class ViewController {

    private final ViewService viewService;

    public ViewController(ViewService viewService) {
        this.viewService = viewService;
    }

    @GetMapping
    List<ViewDTO> getObject() {
        return this.viewService.getObjectView();
    }

    @GetMapping(path = "/tag")
    List<TagDTO> getTag(){
        return this.viewService.getTag();
    }

    @GetMapping(path = "/by-tag")
    List<ViewDTO> getObjectByTag(@RequestParam("tag") String tag){
        return this.viewService.getObjectByTag(tag);
    }

    @PostMapping(path = "/generate-view-fields")
    List<ViewFieldDTO> generateViewFields(@RequestBody Map<String, String> parameters) {
        return this.viewService.generateViewFields(parameters.get("query"));
    }

    @PostMapping
    public ViewDTO postObject(@RequestBody ViewDTO dto) {
        ViewDTO customComponentDTO = this.viewService.saveDTOAndCreate(dto);
        return customComponentDTO;
    }

    @PutMapping
    public ViewDTO putObject(@RequestBody ViewDTO dto) {
        ViewDTO customComponentDTO = this.viewService.saveDTOAndCreate(dto);
        return customComponentDTO;
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        ViewDTO customComponentDTO = this.viewService.getObject(id);
        this.viewService.deleteObject(id);
        this.viewService.dropView(customComponentDTO.getName());
    }

    @GetMapping(path = "/by-id")
    ViewDTO getObject(@RequestParam("id") String id) {
        return this.viewService.getObject(id);
    }

    @GetMapping(path = "/view-exists")
    public Boolean tableExists(@RequestParam("name") String tableName) {
        return viewService.viewOnDatabase(tableName);
    }

}
