package com.crm.sofia.services.form;

import com.crm.sofia.dto.component.ComponentPersistEntityDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityFieldDTO;
import com.crm.sofia.dto.form.*;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.form.FormMapper;
import com.crm.sofia.model.expression.ExprResponse;
import com.crm.sofia.model.form.FormEntity;
import com.crm.sofia.repository.form.FormRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.component.ComponentPersistEntityFieldAssignmentService;
import com.crm.sofia.services.language.LanguageDesignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormDesignerService {
    @Autowired
    CacheManager cacheManager;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FormMapper formMapper;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ComponentPersistEntityFieldAssignmentService componentPersistEntityFieldAssignmentService;
    @Autowired
    private FormJavascriptService formJavascriptService;
    @Autowired
    private LanguageDesignerService languageDesignerService;

    @Transactional
    public FormDTO postObject(FormDTO formDTO) throws Exception {
        FormEntity formEntity = this.formMapper.map(formDTO);
        formEntity.setCreatedOn(Instant.now());
        formEntity.setModifiedOn(Instant.now());
        formEntity.setCreatedBy(jwtService.getUserId());
        formEntity.setModifiedBy(jwtService.getUserId());

        Long instanceVersion = formEntity.getInstanceVersion();
        if (instanceVersion == null) {
            instanceVersion = 0L;
        } else {
            instanceVersion += 1L;
        }

        formEntity.setInstanceVersion(instanceVersion);

        FormEntity createdFormEntity = this.formRepository.save(formEntity);
        FormDTO createdFormDTO = this.formMapper.map(createdFormEntity);

        this.componentPersistEntityFieldAssignmentService
                .saveFieldAssignments(formDTO.getComponent().getComponentPersistEntityList(),
                        "form",
                        createdFormDTO.getId());

        String script = this.formJavascriptService.generateDynamicScript(createdFormDTO);
        String scriptMin = this.formJavascriptService.minify(script);
        this.formRepository.updateScripts(createdFormDTO.getId(), script, scriptMin);

        return createdFormDTO;
    }

    @Transactional
    public FormDTO putObject(FormDTO formDTO) throws Exception {
        FormEntity formEntity = this.formMapper.map(formDTO);
        formEntity.setModifiedOn(Instant.now());
        formEntity.setModifiedBy(jwtService.getUserId());
        Long instanceVersion = formEntity.getInstanceVersion();
        if (instanceVersion == null) {
            instanceVersion = 0L;
        } else {
            instanceVersion += 1L;
        }
        formEntity.setInstanceVersion(instanceVersion);

        String script = this.formJavascriptService.generateDynamicScript(formDTO);
        String scriptMin = this.formJavascriptService.minify(script);
        formEntity.setScript(script);
        formEntity.setScriptMin(scriptMin);

        FormEntity createdFormEntity = this.formRepository.save(formEntity);

        this.componentPersistEntityFieldAssignmentService
                .saveFieldAssignments(formDTO.getComponent().getComponentPersistEntityList(), "form",
                        createdFormEntity.getId());

        FormDTO createdFormDTO = this.formMapper.map(createdFormEntity);

        List<ComponentPersistEntityDTO> componentPersistEntityList =
                this.componentPersistEntityFieldAssignmentService.retrieveFieldAssignments(
                        createdFormDTO.getComponent().getComponentPersistEntityList(),
                        "form",
                        createdFormDTO.getId()
                );
        createdFormDTO.getComponent().setComponentPersistEntityList(componentPersistEntityList);

        this.redisCacheEvict(createdFormDTO);

        return createdFormDTO;
    }

    public List<FormDTO> getObject() {
        List<FormDTO> formList = this.formRepository.getObject();
        return formList;
    }

    public List<FormDTO> getObjectByTag(String tag) {
        return this.formRepository.getObjectByTag(tag);
    }

    public List<FormDTO> get10LatestObject() {
        List<FormDTO> formList = this.formRepository.get10LatestObject(PageRequest.of(0, 10));
        return formList;
    }

    public FormDTO getObject(String id) {

        /* Retrieve */
        FormEntity optionalFormEntity = this.formRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Form Does Not Exist"));


        /* Map */
        FormDTO formDTO = this.formMapper.map(optionalFormEntity);

        /* Retrieve Field Assignments */
        List<ComponentPersistEntityDTO> componentPersistEntityList =
                this.componentPersistEntityFieldAssignmentService.retrieveFieldAssignments(
                        formDTO.getComponent().getComponentPersistEntityList(),
                        "form",
                        formDTO.getId()
                );
        formDTO.getComponent().setComponentPersistEntityList(componentPersistEntityList);

        /* Shorting */
        formDTO.getFormTabs().sort(Comparator.comparingLong(FormTabDTO::getShortOrder));
        formDTO.getFormTabs().forEach(formTab -> {
            formTab.getFormAreas().sort(Comparator.comparingLong(FormAreaDTO::getShortOrder));
            formTab.getFormAreas().forEach(formArea -> {
                formArea.getFormControls().sort(Comparator.comparingLong(FormControlDTO::getShortOrder));
                formArea.getFormControls().forEach(formControl -> {
                    if (formControl.getType().equals("table")) {
                        formControl.getFormControlTable().getFormControls().sort(Comparator.comparingLong(FormControlTableControlDTO::getShortOrder));
                        formControl.getFormControlTable().getFormControlButtons().sort(Comparator.comparingLong(FormControlTableControlDTO::getShortOrder));
                    }
                });
            });
        });

        formDTO.getFormPopups().sort(Comparator.comparingLong(FormPopupDto::getShortOrder));
        formDTO.getFormPopups().forEach(formPopup -> {
            formPopup.getFormAreas().sort(Comparator.comparingLong(FormAreaDTO::getShortOrder));
            formPopup.getFormAreas().forEach(formArea -> {
                formArea.getFormControls().sort(Comparator.comparingLong(FormControlDTO::getShortOrder));
                formArea.getFormControls().forEach(formControl -> {
                    if (formControl.getType().equals("table")) {
                        formControl.getFormControlTable().getFormControls().sort(Comparator.comparingLong(FormControlTableControlDTO::getShortOrder));
                        formControl.getFormControlTable().getFormControlButtons().sort(Comparator.comparingLong(FormControlTableControlDTO::getShortOrder));
                    }
                });
            });
        });

        formDTO.getFormActionButtons().sort(Comparator.comparingLong(FormActionButtonDTO::getShortOrder));

        /* Shorting Component*/
        List<ComponentPersistEntityDTO> sorted = this.shortCPEList(formDTO.getComponent().getComponentPersistEntityList());
        formDTO.getComponent().setComponentPersistEntityList(sorted);

        /* Return */
        return formDTO;
    }

    public List<ComponentPersistEntityDTO> shortCPEList(List<ComponentPersistEntityDTO> componentPersistEntityList) {
        if (componentPersistEntityList == null) {
            return null;
        }

        componentPersistEntityList
                .stream()
                .filter(cpe -> cpe.getShortOrder() == null)
                .forEach(cpe -> cpe.setShortOrder(0L));

        List<ComponentPersistEntityDTO> sorted =
                componentPersistEntityList
                        .stream()
                        .sorted(Comparator.comparingLong(ComponentPersistEntityDTO::getShortOrder)).collect(Collectors.toList());

        sorted.stream().forEach(cpe -> {

            for (ComponentPersistEntityFieldDTO componentPersistEntityFieldDTO : cpe.getComponentPersistEntityFieldList()) {
                if (componentPersistEntityFieldDTO.getPersistEntityField().getShortOrder() == null) {
                    componentPersistEntityFieldDTO.getPersistEntityField().setShortOrder(0L);
                }
            }

            for (ComponentPersistEntityFieldDTO cpef : cpe.getComponentPersistEntityFieldList()) {
                cpef.setShortOrder(cpef.getPersistEntityField().getShortOrder());
            }

            List<ComponentPersistEntityFieldDTO> sortedCpefList =
                    cpe.getComponentPersistEntityFieldList()
                            .stream()
                            .sorted(Comparator.comparingLong(ComponentPersistEntityFieldDTO::getShortOrder)).collect(Collectors.toList());

            cpe.setComponentPersistEntityFieldList(sortedCpefList);
        });

        sorted.stream().forEach(cpe -> {
            List<ComponentPersistEntityDTO> sortedCPE = this.shortCPEList(cpe.getComponentPersistEntityList());
            cpe.setComponentPersistEntityList(sortedCPE);
        });

        return sorted;
    }

    @Transactional
    @Modifying
    public void deleteObject(String id) {
        FormEntity formEntity = this.formRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Form Does Not Exist"));

        this.componentPersistEntityFieldAssignmentService.deleteByIdAndEntityType(formEntity.getId(), "form");
        this.formRepository.deleteById(formEntity.getId());

        FormDTO formDTO = this.formMapper.map(formEntity);
        List<ComponentPersistEntityDTO> componentPersistEntityList =
                this.componentPersistEntityFieldAssignmentService.retrieveFieldAssignments(
                        formDTO.getComponent().getComponentPersistEntityList(),
                        "form",
                        formDTO.getId()
                );
        formDTO.getComponent().setComponentPersistEntityList(componentPersistEntityList);

        this.redisCacheEvict(formDTO);
    }

    public boolean clearCache() {
        this.formRepository.increaseInstanceVersions();
        List<FormEntity> entities = this.formRepository.findAll();
        entities.forEach(entity -> {

            FormDTO formDTO = this.formMapper.map(entity);
            List<ComponentPersistEntityDTO> componentPersistEntityList =
                    this.componentPersistEntityFieldAssignmentService.retrieveFieldAssignments(
                            formDTO.getComponent().getComponentPersistEntityList(),
                            "form",
                            formDTO.getId()
                    );
            formDTO.getComponent().setComponentPersistEntityList(componentPersistEntityList);
            this.redisCacheEvict(formDTO);

        });
        return true;
    }

    public List<TagDTO> getTag() {
        List<TagDTO> tag = formRepository.findTagDistinct();
        return tag;
    }

    private void redisCacheEvict(FormDTO formEntity) {
        cacheManager.getCache("form_db_cache").evict(formEntity.getId());
        cacheManager.getCache("form_uil_cache").evict(formEntity.getId());
        cacheManager.getCache("form_uil_cache").evict(new Object[]{formEntity.getId(), ""});
        languageDesignerService.getObject().forEach(language -> {
            cacheManager.getCache("form_uil_cache").evict(new Object[]{formEntity.getId(), language.getId()});
        });

        formEntity.getComponent().getComponentPersistEntityList().forEach(cpe -> {
            cpe.getComponentPersistEntityFieldList()
                    .stream()
                    .filter(cpef -> cpef.getAssignment() != null)
                    .forEach(cpef -> {
                        cacheManager.getCache("expression").evict(cpef.getAssignment().getId() + "-s");
                        cacheManager.getCache("expression").evict(cpef.getAssignment().getId()+ "-l");
                        cacheManager.getCache("expression").evict(cpef.getAssignment().getId());
                    });

            cpe.getComponentPersistEntityFieldList()
                    .stream()
                    .forEach(cpef -> {
                        cacheManager.getCache("expression").evict(cpef.getPersistEntityField().getId());
                    });
        });
    }

}
