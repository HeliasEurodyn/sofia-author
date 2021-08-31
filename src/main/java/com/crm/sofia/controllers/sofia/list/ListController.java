package com.crm.sofia.controllers.sofia.list;

import com.crm.sofia.dto.sofia.list.GroupEntryDTO;
import com.crm.sofia.dto.sofia.list.ListDTO;
import com.crm.sofia.dto.sofia.list.ListResultsDataDTO;
import com.crm.sofia.services.sofia.list.ListService;
import com.crm.sofia.utils.ExcelGenerator;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping(path = "by-id")
    ListDTO getObjectData(@RequestParam("id") Long id) {
        return this.listService.getObjectData(id);
    }

    @GetMapping
    List<ListDTO> getObject() {
        return this.listService.getObject();
    }

    @GetMapping(path = "/results")
    ListResultsDataDTO getObjectData(@RequestParam Map<String, String> parameters, @RequestParam("id") Long id) {
        return this.listService.getObjectDataByParameters(parameters, id);
    }

    @GetMapping(path = "/left-grouping/results")
    List<GroupEntryDTO> getObjectLeftGroupingData(@RequestParam Map<String, String> parameters, @RequestParam("id") Long id) {
        return this.listService.getObjectLeftGroupingDataByParameters(parameters, id);
    }

    @PostMapping(path = "/data-excel")
    public ResponseEntity<InputStreamResource> getObjectExcelData(@RequestBody ListDTO dto) throws IOException, JRException {
        ListResultsDataDTO resultsDataDTO = this.listService.getObjectData(dto);
        ByteArrayInputStream in = ExcelGenerator.listToExcel(dto, resultsDataDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=list-data.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @GetMapping(path = "version")
    String getVersion(@RequestParam("id") Long formId) {
        return this.listService.getVersion(formId);
    }

//    @GetMapping(path = "/jasper-test-pdf")
//    void doJasperTest() throws FileNotFoundException, JRException {
//        this.listService.doJasperPdfTest();
//    }
//
//    @GetMapping(path = "/jasper-test-pdf-n")
//    void doJasperTestNew(HttpServletResponse response) throws IOException, JRException, SQLException {
//        this.listService.doJasperPdfTestN(response);
//    }
//
//    @GetMapping(path = "/jasper-test-excel")
//    void doJasperTestExcel() throws FileNotFoundException, JRException {
//        this.listService.doJasperExcelTestExcel();
//    }

}