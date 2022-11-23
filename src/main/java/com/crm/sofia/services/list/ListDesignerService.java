package com.crm.sofia.services.list;

import com.crm.sofia.dto.component.ComponentPersistEntityDTO;
import com.crm.sofia.dto.list.ListComponentFieldDTO;
import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.list.ListMapper;
import com.crm.sofia.model.list.ListEntity;
import com.crm.sofia.repository.list.ListRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.language.LanguageDesignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class ListDesignerService {
    @Autowired
    private  ListRepository listRepository;
    @Autowired
    private  ListMapper listMapper;
    @Autowired
    private  JWTService jwtService;
    @Autowired
    private  ListJavascriptService listJavascriptService;
    @Autowired
    private  LanguageDesignerService languageDesignerService;


    @Autowired
    CacheManager cacheManager;



    @Transactional
    public ListDTO postObject(ListDTO listDTO) throws Exception {
        ListEntity listEntity = this.listMapper.mapListDTO(listDTO);
        listEntity.setCreatedOn(Instant.now());
        listEntity.setModifiedOn(Instant.now());
        listEntity.setCreatedBy(jwtService.getUserId());
        listEntity.setModifiedBy(jwtService.getUserId());
        Long instanceVersion = listEntity.getInstanceVersion();
        if (instanceVersion == null) {
            instanceVersion = 0L;
        } else {
            instanceVersion += 1L;
        }
        listEntity.setInstanceVersion(instanceVersion);

        ListEntity createdListEntity = this.listRepository.save(listEntity);
        ListDTO createdListDTO = this.listMapper.map(createdListEntity);

        String script = this.listJavascriptService.generateDynamicScript(createdListDTO);
        String scriptMin = this.listJavascriptService.minify(script);
        this.listRepository.updateScripts(createdListDTO.getId(), script, scriptMin);

        return createdListDTO;
    }

    @Transactional
    public ListDTO putObject(ListDTO listDTO) throws Exception {

        ListEntity listEntity = this.listMapper.mapListDTO(listDTO);
        listEntity.setModifiedOn(Instant.now());
        listEntity.setModifiedBy(jwtService.getUserId());
        Long instanceVersion = listEntity.getInstanceVersion();
        if (instanceVersion == null) {
            instanceVersion = 0L;
        } else {
            instanceVersion += 1L;
        }
        listEntity.setInstanceVersion(instanceVersion);

        ListEntity createdListEntity = this.listRepository.save(listEntity);
        ListDTO createdListDTO = this.listMapper.map(createdListEntity);

        String script = this.listJavascriptService.generateDynamicScript(createdListDTO);
        String scriptMin = this.listJavascriptService.minify(script);
        this.listRepository.updateScripts(createdListDTO.getId(), script, scriptMin);

        cacheManager.getCache("list_ui_cache").evict(createdListEntity.getId());
        cacheManager.getCache("list_ui_cache").evict(new Object[]{createdListEntity.getId(), 0});
        languageDesignerService.getObject().forEach(language -> {
            cacheManager.getCache("list_ui_cache").evict(new Object[]{createdListEntity.getId(), language.getId()});
        });

        return createdListDTO;
    }

    public List<ListDTO> getObject() {
        return this.listRepository.getObject();
    }

    public ListDTO getObject(String id) {
        ListEntity listEntity =
                this.listRepository.findById(id)
                        .orElseThrow(() ->
                                new DoesNotExistException("ListEntity Does Not Exist")
                        );

        ListDTO listDTO = this.listMapper.mapList(listEntity);
        listDTO.getComponent().getComponentPersistEntityList().sort(Comparator.comparingLong(ComponentPersistEntityDTO::getShortOrder));
        listDTO.getListComponentColumnFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentFilterFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentLeftGroupFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentOrderByFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentTopGroupFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentActionFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentActionFieldList().forEach(af -> {
            if (af.getListComponentActionFieldList() != null) {
                af.getListComponentActionFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
            }
        });

        return listDTO;
    }

    public ListDTO getObjectByName(String name) {
        ListEntity listEntity = this.listRepository.findFirstByName(name);
        if (listEntity == null) {
            throw new DoesNotExistException("ListEntity does not exist");
        }

        ListDTO listDTO = this.listMapper.mapList(listEntity);
        listDTO.getComponent().getComponentPersistEntityList().sort(Comparator.comparingLong(ComponentPersistEntityDTO::getShortOrder));
        listDTO.getListComponentColumnFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentFilterFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentLeftGroupFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentOrderByFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentTopGroupFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentActionFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
        listDTO.getListComponentActionFieldList().forEach(af -> {
            if (af.getListComponentActionFieldList() != null) {
                af.getListComponentActionFieldList().sort(Comparator.comparingLong(ListComponentFieldDTO::getShortOrder));
            }
        });

        return listDTO;
    }

    public void deleteObject(String id) {
        ListEntity lienEntity = this.listRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("List Does Not Exist"));

        cacheManager.getCache("list_ui_cache").evict(lienEntity.getId());
        cacheManager.getCache("list_ui_cache").evict(new Object[]{lienEntity.getId(), 0});
        languageDesignerService.getObject().forEach(language -> {
            cacheManager.getCache("list_ui_cache").evict(new Object[]{lienEntity.getId(), language.getId()});
        });

        this.listRepository.deleteById(lienEntity.getId());
    }

    public boolean clearCacheForUi() {
        this.listRepository.increaseInstanceVersions();
        return true;
    }

    public List<String> getBusinessUnits() {
        List<String> businessUnits = listRepository.findBusinessUnitsDistinct();
        return businessUnits;
    }
}
