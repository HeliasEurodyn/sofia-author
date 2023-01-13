package com.crm.sofia.controllers.html_template;


import com.crm.sofia.dto.html_template.HtmlTemplateDTO;
import com.crm.sofia.services.html_template.HtmlTemplateDesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@Validated
@RequestMapping("/html-template-designer")
public class HtmlTemplateDesignerController {

    @Autowired
    HtmlTemplateDesignerService htmlTemplateDesignerService;

    @GetMapping
    List<HtmlTemplateDTO> getObject() {return htmlTemplateDesignerService.getObject();}



    @GetMapping(path = "/by-id")
    HtmlTemplateDTO getObject(@RequestParam("id") String id){return htmlTemplateDesignerService.getObject(id);}



    @PostMapping
    public HtmlTemplateDTO postObject(@RequestBody HtmlTemplateDTO htmlTemplateDTO) throws IOException {
        return htmlTemplateDesignerService.postObject(htmlTemplateDTO);
    }


    @PutMapping
    public HtmlTemplateDTO putObject(@RequestBody HtmlTemplateDTO htmlTemplateDTO){
        return htmlTemplateDesignerService.postObject(htmlTemplateDTO);
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {htmlTemplateDesignerService.deleteObject(id);}

}
