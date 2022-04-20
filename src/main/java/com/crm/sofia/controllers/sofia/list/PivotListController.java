package com.crm.sofia.controllers.sofia.list;

import com.crm.sofia.dto.sofia.list.base.GroupEntryDTO;
import com.crm.sofia.dto.sofia.list.base.ListDTO;
import com.crm.sofia.dto.sofia.list.base.ListResultsDataDTO;
import com.crm.sofia.dto.sofia.list.user.ListUiDTO;
import com.crm.sofia.services.sofia.list.ListService;
import com.crm.sofia.utils.ExcelGenerator;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/pivot-list")
public class PivotListController {

    private final ListService listService;

    public PivotListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping(path = "by-id")
    ListDTO getObject(@RequestParam("id") String id) {
        ListDTO listDTO = this.listService.getObjectWithDefaults(id);
        listDTO.setComponent(null);
        return listDTO;
    }

    @GetMapping(path = "ui")
    ListUiDTO getUiObject(@RequestParam("id") String id,
                          @RequestParam(defaultValue = "0", name = "language-id") String languageId) {
        return this.listService.getUiListObject(id, languageId);
    }

    @GetMapping
    List<ListDTO> getObject() {
        return this.listService.getObject();
    }

    @GetMapping(path = "/results")
    ListResultsDataDTO getObject(@RequestParam Map<String, String> parameters, @RequestParam("id") String id) {
        return this.listService.getPivotObjectDataByParameters(parameters, id);
    }

    @GetMapping(path = "instance-version", produces = "text/plain")
    String getInstanceVersion(@RequestParam("id") String formId) {
        return this.listService.getInstanceVersion(formId);
    }

    @RequestMapping(value = "/dynamic-javascript/{id}/min/script.js", method = RequestMethod.GET, produces = "text/javascript;")
    String getMinJavaScript(@PathVariable("id") String id) {
        return this.listService.getMinJavaScript(id);
    }

    @RequestMapping(value = "/dynamic-javascript/{id}/script.js", method = RequestMethod.GET, produces = "text/javascript;")
    String getJavaScript(@PathVariable("id") String id) {
        return this.listService.getJavaScript(id);
    }

    @Transactional
    @RequestMapping(value = "/dynamic-cssscript/{id}/script.css", method = RequestMethod.GET, produces = "text/css;")
    String getFormCssScript(@PathVariable("id") String id) {
        return this.listService.getCssScript(id);
    }

    @Transactional
    @RequestMapping(value = "/dynamic-javascripts/factory.js", method = RequestMethod.GET, produces = "text/javascript;")
    String getFormJavaScripty() {
        return this.listService.getJavaScriptFactory();
    }


}
