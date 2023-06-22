package com.crm.sofia.controllers.rule;


import com.crm.sofia.dto.rule.RuleOperatorDTO;
import com.crm.sofia.services.rule.RuleOperatorDesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/rule-operator-designer")
public class RuleOparetorDesignerController {

    private final RuleOperatorDesignerService ruleOperatorDesignerService;

    public RuleOparetorDesignerController(RuleOperatorDesignerService ruleOperatorDesignerService) {
        this.ruleOperatorDesignerService = ruleOperatorDesignerService;
    }

    @GetMapping
    List<RuleOperatorDTO> getObject() {
        return this.ruleOperatorDesignerService.getObject();
    }

    @GetMapping(path = "/by-id")
    RuleOperatorDTO getObject(@RequestParam("id") String id) {
        return this.ruleOperatorDesignerService.getObject(id);
    }

    @PostMapping
    public RuleOperatorDTO postObject(@RequestBody RuleOperatorDTO dto) {
        RuleOperatorDTO createdDTO = this.ruleOperatorDesignerService.postObject(dto);
        return createdDTO;
    }

    @PutMapping
    public RuleOperatorDTO putObject(@RequestBody RuleOperatorDTO dto) {
        RuleOperatorDTO createdDTO = this.ruleOperatorDesignerService.putObject(dto);
        return createdDTO;
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        this.ruleOperatorDesignerService.deleteObject(id);
    }

}
