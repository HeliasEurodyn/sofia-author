package com.crm.sofia.controllers.tag;

import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.services.tag.TagDesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/tag-designer")
public class TagDesignerController {

    @Autowired
    private TagDesignerService tagDesignerService;

    @GetMapping
    List<TagDTO> getObject() {
        return tagDesignerService.getObject();
    }

    @GetMapping(path = "/by-id")
    TagDTO getObject(@RequestParam("id") String id) {
        return tagDesignerService.getObject(id);
    }

    @PostMapping
    public TagDTO postObject(@RequestBody TagDTO tagDTO) throws IOException {
        return tagDesignerService.postObject(tagDTO);
    }

    @PutMapping
    public TagDTO putObject(@RequestBody TagDTO tagDTO) {
        return tagDesignerService.postObject(tagDTO);
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        tagDesignerService.deleteObject(id);
    }

}
