package com.crm.sofia.controllers.table;

import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.table.RemoveForeignKeyConstrainDTO;
import com.crm.sofia.dto.table.TableDTO;
import com.crm.sofia.dto.table.TableFieldDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.services.table.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Validated
@RequestMapping("/table")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    List<TableDTO> getObject() {
        return this.tableService.getObjectTable();
    }



    @GetMapping(path = "/tag")
    List<TagDTO> getTag() {
        return this.tableService.getTag();
    }

    @PostMapping
    public TableDTO postObject(@RequestBody TableDTO dto) {
        TableDTO customComponentDTO = this.tableService.save(dto);
        return customComponentDTO;
    }

    @PutMapping
    public TableDTO putObject(@RequestBody TableDTO dto) {
        TableDTO customComponentDTO = this.tableService.save(dto);
        return customComponentDTO;
    }

    @DeleteMapping
    public void deleteObject(@RequestParam("id") String id) {
        TableDTO dto = this.tableService.getObject(id);
        this.tableService.deleteObject(dto.getId());
    }

    @GetMapping(path = "/by-tag")
    List<TableDTO> getObjectByTag(@RequestParam("tag") String tag) {
        return this.tableService.getObjectByTag(tag);
    }

    @GetMapping(path = "/by-id")
    TableDTO getObject(@RequestParam("id") String id) {
        return this.tableService.getObject(id);
    }

    @GetMapping(path = "/table-exists")
    public Boolean tableExists(@RequestParam("name") String name) {
        return tableService.tableOnDatabase(name);
    }

    @GetMapping(path = "/generate-table-fields")
    List<TableFieldDTO> generateTableFields(@RequestParam("name") String name) {
        return this.tableService.generateTableFields(name);
    }

    @PutMapping(path="/drop_foreign_key_constrain")
    public ResponseEntity<Map<String,String>> DropForeignKeyConstrain(@RequestBody RemoveForeignKeyConstrainDTO dto) {
       return this.tableService.DropForeignKeyConstrain(dto);
    }


}
