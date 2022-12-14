package com.crm.sofia.controllers.appview;

import com.crm.sofia.dto.appview.AppViewDTO;
import com.crm.sofia.dto.appview.AppViewFieldDTO;
import com.crm.sofia.services.appview.AppViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/appview")
public class AppViewController {

    private final AppViewService appViewService;

    public AppViewController(AppViewService appViewService) {
        this.appViewService = appViewService;
    }

    @GetMapping
    List<AppViewDTO> getObject() {
        return this.appViewService.getObjectAppView();
    }

    @GetMapping(path = "/by-id")
    AppViewDTO getObject(@RequestParam("id") String id) {
        return this.appViewService.getObject(id);
    }

    @PostMapping
    public AppViewDTO postObject(@RequestBody AppViewDTO dto) {
        return this.appViewService.postObject(dto);
    }

    @PutMapping
    public AppViewDTO putObject(@RequestBody AppViewDTO viewDTO) {
        return this.appViewService.postObject(viewDTO);
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        this.appViewService.deleteObject(id);
    }

    @GetMapping(path = "/generate-view-fields")
    List<AppViewFieldDTO> generateViewFields(@RequestParam("query") String query) {
        return this.appViewService.generateViewFields(query);
    }

    @GetMapping(path = "/view-exists")
    public Boolean tableExists(@RequestParam("name") String tableName) {
        return appViewService.viewOnDatabase(tableName);
    }
}
