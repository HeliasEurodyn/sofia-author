package com.crm.sofia.controllers.search;

import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.search.SearchDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.services.search.SearchDesignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/search-designer")
public class SearchDesignerController {

    private final SearchDesignerService searchDesignerService;

    public SearchDesignerController(SearchDesignerService searchDesignerService) {
        this.searchDesignerService = searchDesignerService;
    }

    @GetMapping
    List<SearchDTO> getObject() {
        return this.searchDesignerService.getObject();
    }

    @GetMapping(path = "/tag")
    List<TagDTO> getTag() {
        return this.searchDesignerService.getTag();
    }


    @GetMapping(path = "/by-tag")
    List<SearchDTO> getObjectByTag(@RequestParam("tag") String tag) {
        return this.searchDesignerService.getObjectByTag(tag);
    }

    @GetMapping(path = "/by-id")
    SearchDTO getObject(@RequestParam("id") String id) {
        return this.searchDesignerService.getObject(id);
    }

    @PostMapping
    public SearchDTO postObject(@RequestBody SearchDTO dto) {
        return this.searchDesignerService.postObject(dto);
    }

    @PutMapping
    public SearchDTO putObject(@RequestBody SearchDTO dto) {
        return this.searchDesignerService.postObject(dto);
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        this.searchDesignerService.deleteObject(id);
    }

}
