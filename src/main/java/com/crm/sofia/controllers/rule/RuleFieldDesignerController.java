package com.crm.sofia.controllers.rule;

import com.crm.sofia.dto.rule.RuleFieldDTO;
import com.crm.sofia.services.rule.RuleFieldDesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/rule-field-designer")
public class RuleFieldDesignerController {

    private final RuleFieldDesignerService ruleFieldDesignerService;

    public RuleFieldDesignerController(RuleFieldDesignerService ruleFieldDesignerService) {
        this.ruleFieldDesignerService = ruleFieldDesignerService;
    }

    @GetMapping
    List<RuleFieldDTO> getObject() {
        return this.ruleFieldDesignerService.getObject();
    }

    @GetMapping(path = "/by-id")
    RuleFieldDTO getObject(@RequestParam("id") String id) {
        return this.ruleFieldDesignerService.getObject(id);
    }

    @PostMapping
    public RuleFieldDTO postObject(@RequestBody RuleFieldDTO dto) {
        RuleFieldDTO createdDTO = this.ruleFieldDesignerService.postObject(dto);
        return createdDTO;
    }

    @PutMapping
    public RuleFieldDTO putObject(@RequestBody RuleFieldDTO dto) {
        RuleFieldDTO createdDTO = this.ruleFieldDesignerService.putObject(dto);
        return createdDTO;
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        this.ruleFieldDesignerService.deleteObject(id);
    }

}
