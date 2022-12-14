package com.crm.sofia.services.component;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityFieldDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.component.ComponentMapper;
import com.crm.sofia.mapper.component.ComponentPersistEntityMapper;
import com.crm.sofia.model.common.MainEntity;
import com.crm.sofia.model.component.Component;
import com.crm.sofia.model.component.ComponentPersistEntity;
import com.crm.sofia.model.component.ComponentPersistEntityField;
import com.crm.sofia.model.persistEntity.PersistEntity;
import com.crm.sofia.repository.component.ComponentPersistEntityRepository;
import com.crm.sofia.repository.component.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComponentDesignerService {
    @Autowired
    private  ComponentMapper componentMapper;
    @Autowired
    private  ComponentRepository componentRepository;
    @Autowired
    private  ComponentPersistEntityRepository componentPersistEntityRepository;
    @Autowired
    private  ComponentPersistEntityMapper componentPersistEntityMapper;



    public List<ComponentDTO> getObject() {
        List<ComponentDTO> componentList = this.componentRepository.getObject();

        return componentList;
    }

    public ComponentDTO getObject(String id) {
        Component optionalEntity = this.componentRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Component Does Not Exist"));


        Component entity = optionalEntity;
        ComponentDTO dto = this.componentMapper.map(entity);

        this.shortComponent(dto);

        return dto;
    }

    public void shortComponent(ComponentDTO dto) {
        List<ComponentPersistEntityDTO> sorted = this.shortCPEList(dto.getComponentPersistEntityList());
        dto.setComponentPersistEntityList(sorted);
    }

    public List<ComponentPersistEntityDTO> shortCPEList(List<ComponentPersistEntityDTO> componentPersistEntityList) {
        if(componentPersistEntityList == null){
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

            cpe.getComponentPersistEntityFieldList()
                    .stream()
                    .filter(cpef -> cpef.getShortOrder() == null)
                    .forEach(cpef -> cpef.setShortOrder(0L));

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
    public ComponentDTO postObject(ComponentDTO dto) {
        Component entity = this.componentMapper.mapWithPersistEntities(dto);
        Component createdEntity = this.componentRepository.save(entity);
        //return this.componentMapper.map(createdEntity);
        return null;
    }

    @Transactional
    public ComponentDTO putObject(ComponentDTO dto) {
        Component entity = this.componentMapper.map(dto);
        Component createdEntity = this.componentRepository.save(entity);
        return this.componentMapper.map(createdEntity);
    }

    public void deleteObject(String id) {
        Component optionalComponent = this.componentRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Component Does Not Exist"));

        this.componentRepository.deleteById(optionalComponent.getId());
    }

    public ComponentPersistEntityDTO getComponentPersistEntityDataById(String id, String selectionId) {
        ComponentPersistEntityDTO componentPersistEntityDTO = this.getComponentPersistEntityById(id);
        return componentPersistEntityDTO;
    }

    public ComponentPersistEntityDTO getComponentPersistEntityById(String id) {
        Optional<ComponentPersistEntity> optionalComponentPersistEntity = componentPersistEntityRepository.findById(id);

        if (!optionalComponentPersistEntity.isPresent()) {
            return null;
        }

        ComponentPersistEntity componentPersistEntity = optionalComponentPersistEntity.get();
        ComponentPersistEntityDTO componentPersistEntityDTO = this.componentPersistEntityMapper.map(componentPersistEntity);

        return componentPersistEntityDTO;
    }

    public void removeComponentTablesByTableId(String persistEntityId) {
        List<ComponentPersistEntity> componentPersistEntities =
                this.componentPersistEntityRepository.findComponentEntitiesOfTableId(persistEntityId);

        this.componentPersistEntityRepository.deleteAll(componentPersistEntities);
    }

    public void removeComponentTableFieldsByTable(String persistEntityId, List<String> tableFieldIds) {
        List<ComponentPersistEntity> componentPersistEntities = this.componentPersistEntityRepository.findComponentEntitiesOfTableId(persistEntityId);


        componentPersistEntities
                .stream()
                .filter(cpe -> cpe.getPersistEntity() != null)
                .filter(cpe -> cpe.getPersistEntity().getId().equals(persistEntityId))
                .forEach(cpe -> {

                    /* Remove Fields */
                    cpe.getComponentPersistEntityFieldList()
                            .removeIf(cpef -> !tableFieldIds.contains(cpef.getPersistEntityField().getId()));
                });

        this.componentPersistEntityRepository.saveAll(componentPersistEntities);
    }

    public void insertComponentTableFieldsByTable(PersistEntity persistEntity) {

        List<ComponentPersistEntity> componentPersistEntities = this.componentPersistEntityRepository.findComponentEntitiesOfTableId(persistEntity.getId());

        componentPersistEntities
                .stream()
                .filter(cpe -> cpe.getPersistEntity() != null)
                .filter(cpe -> cpe.getPersistEntity().getId() == persistEntity.getId())
                .forEach(cpe -> {

                    /* Add Fields */
                    List<String> currentTableFieldIds = cpe.getComponentPersistEntityFieldList()
                            .stream()
                            .map(cpef -> cpef.getPersistEntityField().getId())
                            .collect(Collectors.toList());

                    persistEntity.getPersistEntityFieldList()
                            .stream()
                            .filter(pf -> !currentTableFieldIds.contains(pf.getId()))
                            .forEach(pf -> {
                                ComponentPersistEntityField cpef = new ComponentPersistEntityField();
                                cpef.setPersistEntityField(pf);
                                cpe.getComponentPersistEntityFieldList().add(cpef);
                            });
                });

        this.componentPersistEntityRepository.saveAll(componentPersistEntities);
    }
}
