package com.crm.sofia.services.xls_import;

import com.crm.sofia.dto.component.ComponentPersistEntityDTO;
import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.dto.xls_import.XlsImportDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.xls_import.XlsImportMapper;
import com.crm.sofia.model.xls_import.XlsImport;
import com.crm.sofia.repository.xls_import.XlsImportRepository;
import com.crm.sofia.services.component.ComponentPersistEntityFieldAssignmentService;
import com.crm.sofia.utils.EncodingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class XlsImportDesignerService {

    private final XlsImportRepository xlsImportRepository;
    private final XlsImportMapper xlsImportMapper;
    private final ComponentPersistEntityFieldAssignmentService componentPersistEntityFieldAssignmentService;

    public XlsImportDesignerService(XlsImportRepository xlsImportRepository,
                                    XlsImportMapper xlsImportMapper, ComponentPersistEntityFieldAssignmentService componentPersistEntityFieldAssignmentService) {
        this.xlsImportRepository = xlsImportRepository;
        this.xlsImportMapper = xlsImportMapper;
        this.componentPersistEntityFieldAssignmentService = componentPersistEntityFieldAssignmentService;
    }

    public List<TagDTO> getTag() {
        List<TagDTO> tag = xlsImportRepository.findTagDistinct();
        return tag;
    }

    public List<XlsImportDTO> getObjectByTag(String tag) {
        return this.xlsImportRepository.getObjectByTag(tag);
    }

    public List<XlsImportDTO> getObject() {
        List<XlsImportDTO> chartsList = this.xlsImportRepository.getObject();
        return chartsList;
    }

    public XlsImportDTO getObject(String id) {
        XlsImport optionalChart = this.xlsImportRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("XlsImport Does Not Exist"));

        XlsImportDTO dto = this.xlsImportMapper.map(optionalChart);

        if (dto.getDescription() != null) {
            String uriEncoded = EncodingUtil.encodeURIComponent(dto.getDescription());
            String encodedQuery = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
            dto.setDescription(encodedQuery);
        }

        List<ComponentPersistEntityDTO> cpeList =
                this.treeToList(dto.getComponent().getComponentPersistEntityList());

        List<ComponentPersistEntityDTO> componentPersistEntityList =
                this.componentPersistEntityFieldAssignmentService.retrieveFieldAssignments(
                        dto.getComponent().getComponentPersistEntityList(),
                        "xls_import",
                        dto.getId()
                );

        dto.getComponent().setComponentPersistEntityList(componentPersistEntityList);

        cpeList.forEach(cpe -> {
            cpe.getComponentPersistEntityFieldList()
                    .stream()
                    .filter(cpef -> cpef.getAssignment() != null)
                    .filter(cpef -> cpef.getAssignment().getDefaultValue() != null)
                    .forEach(cpef -> {
                        String encDefaultValue = Base64.getEncoder().encodeToString(
                                cpef.getAssignment().getDefaultValue().getBytes(StandardCharsets.UTF_8));
                        cpef.getAssignment().setDefaultValue(encDefaultValue);
                    });
        });

        return dto;
    }

    private List<ComponentPersistEntityDTO> treeToList(List<ComponentPersistEntityDTO> cpeList) {
        if (cpeList == null) {
            return null;
        }

        List<ComponentPersistEntityDTO> newCpeList = new ArrayList<>();
        newCpeList.addAll(cpeList);

        List<ComponentPersistEntityDTO> newChildCpeList = new ArrayList<>();
        newCpeList.forEach(newCpe -> {
            List<ComponentPersistEntityDTO> childCpeList = this.treeToList(newCpe.getComponentPersistEntityList());
            newChildCpeList.addAll(childCpeList);
        });

        newCpeList.addAll(newChildCpeList);
        return newCpeList;
    }

    @Transactional
    public XlsImportDTO postObject(XlsImportDTO dto) {

        List<TagDTO> tags =
                dto.getTags().stream().collect(Collectors.toMap(TagDTO::getId, p -> p, (p, q) -> p))
                        .values()
                        .stream().collect(Collectors.toList());

        dto.setTags(tags);
        List<ComponentPersistEntityDTO> cpeList = this.treeToList(dto.getComponent().getComponentPersistEntityList());
        cpeList.forEach(cpe -> {
            cpe.getComponentPersistEntityFieldList()
                    .stream()
                    .filter(cpef -> cpef.getAssignment() != null)
                    .filter(cpef -> cpef.getAssignment().getDefaultValue() != null)
                    .forEach(cpef -> {
                        String decDefaultValue = new String(Base64.getDecoder().decode(cpef.getAssignment().getDefaultValue()));
                        cpef.getAssignment().setDefaultValue(decDefaultValue);
                    });
        });

        if (dto.getDescription() != null) {
            byte[] decodedDescription = Base64.getDecoder().decode(dto.getDescription());
            String Description = EncodingUtil.decodeURIComponent(new String(decodedDescription));
            dto.setDescription(Description);
        }

        XlsImport chart = this.xlsImportMapper.map(dto);
        XlsImport savedChart = this.xlsImportRepository.save(chart);

        this.componentPersistEntityFieldAssignmentService
                .saveFieldAssignments(dto.getComponent().getComponentPersistEntityList(), "xls_import",
                        savedChart.getId());

        return this.xlsImportMapper.map(savedChart);
    }

    @Transactional
    @Modifying
    public void deleteObject(String id) {
       XlsImport optionalChart = this.xlsImportRepository.findById(id)
               .orElseThrow(() -> new DoesNotExistException("XlsImport Does Not Exist"));

        this.componentPersistEntityFieldAssignmentService.deleteByIdAndEntityType(optionalChart.getId(), "xls_import");
        this.xlsImportRepository.deleteById(optionalChart.getId());
    }

}
