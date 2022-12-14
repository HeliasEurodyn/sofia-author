package com.crm.sofia.controllers.language;

import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.services.language.LanguageDesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/language-designer")
public class LanguageDesignerController {

    @Autowired
    private LanguageDesignerService languageDesignerService;

    @GetMapping
    List<LanguageDTO> getObject() {
        return languageDesignerService.getObject();
    }

    @GetMapping(path = "/by-id")
    LanguageDTO getObject(@RequestParam("id") String id) {
        return languageDesignerService.getObject(id);
    }

    @PostMapping
    public LanguageDTO postObject(@RequestBody LanguageDTO languageDTO) throws IOException {
        return languageDesignerService.postObject(languageDTO);
    }

    @PutMapping
    public LanguageDTO putObject(@RequestBody LanguageDTO languageDTO) {
        return languageDesignerService.postObject(languageDTO);
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        languageDesignerService.deleteObject(id);
    }

}
