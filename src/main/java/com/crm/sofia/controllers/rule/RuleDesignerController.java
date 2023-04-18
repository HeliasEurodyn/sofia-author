package com.crm.sofia.controllers.rule;

import com.crm.sofia.dto.rule.RuleDTO;
import com.crm.sofia.services.rule.RuleDesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/rule")
public class RuleDesignerController {

    private final RuleDesignerService ruleDesignerService;

    public RuleDesignerController(RuleDesignerService ruleDesignerService) {
        this.ruleDesignerService = ruleDesignerService;
    }

    @GetMapping
    List<RuleDTO> getObject() {
        return this.ruleDesignerService.getObject();
    }

    @GetMapping(path = "/by-id")
    RuleDTO getObject(@RequestParam("id") String id) {
        return this.ruleDesignerService.getObject(id);
    }

    @PostMapping
    public RuleDTO postObject(@RequestBody RuleDTO dto) {
        RuleDTO createdDTO = this.ruleDesignerService.postObject(dto);
        return createdDTO;
    }

    @PutMapping
    public RuleDTO putObject(@RequestBody RuleDTO dto) {
        RuleDTO createdDTO = this.ruleDesignerService.putObject(dto);
        return createdDTO;
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        this.ruleDesignerService.deleteObject(id);
    }

}
