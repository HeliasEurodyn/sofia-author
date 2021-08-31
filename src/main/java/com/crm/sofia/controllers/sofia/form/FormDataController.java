package com.crm.sofia.controllers.sofia.form;

import com.crm.sofia.services.sofia.form.FormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/dataset")
public class FormDataController {

    private final FormService formService;

    public FormDataController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping(path = "/{jsonUrl}/{id}")
    public Map getVendorProducts(@PathVariable("jsonUrl") String jsonUrl, @PathVariable("id") String selectionId) {
        return this.formService.retrieveJsonData(jsonUrl, selectionId);
    }

}
