package com.crm.sofia.services.appview;

import com.crm.sofia.dto.appview.AppViewDTO;
import com.crm.sofia.dto.appview.AppViewFieldDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.appview.AppViewMapper;
import com.crm.sofia.model.persistEntity.PersistEntity;
import com.crm.sofia.repository.persistEntity.PersistEntityRepository;
import com.crm.sofia.services.component.ComponentDesignerService;
import com.crm.sofia.utils.EncodingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AppViewService {

    @Value("${sofia.db.name}")
    private String sofiaDatabase;

    private PersistEntityRepository appViewRepository;
    private AppViewMapper appViewMapper;
    private EntityManager entityManager;
    private final ComponentDesignerService componentDesignerService;

    public AppViewService(PersistEntityRepository appViewRepository,
                          AppViewMapper appViewMapper,
                          EntityManager entityManager,
                          ComponentDesignerService componentDesignerService) {
        this.appViewRepository = appViewRepository;
        this.appViewMapper = appViewMapper;
        this.entityManager = entityManager;
        this.componentDesignerService = componentDesignerService;
    }

    public List<TagDTO> getTag(){
        List<TagDTO> tag = appViewRepository.findTagDistinct("appview");
        return tag;
    }

    public List<AppViewDTO> getObjectByTag(String tag){
        return this.appViewRepository.getObjectByTagAppView(tag);

    }

    public AppViewDTO postObject(AppViewDTO appViewDTO) {

        List<TagDTO> tags =
                appViewDTO.getTags().stream().collect(Collectors.toMap(TagDTO::getId, p -> p, (p, q) -> p))
                        .values()
                        .stream().collect(Collectors.toList());

        appViewDTO.setTags(tags);

        if (appViewDTO.getQuery() != null) {
            byte[] decodedQuery = Base64.getDecoder().decode(appViewDTO.getQuery());
            String query = EncodingUtil.decodeURIComponent(new String(decodedQuery));
            appViewDTO.setQuery(query);
        }

        /**
         * Remove deleted Fields From Components
         */
        this.componentDesignerService.removeComponentTableFieldsByTable(
                appViewDTO.getId(),
                appViewDTO.getAppViewFieldList()
                        .stream()
                        .map( x -> x.getId())
                        .collect(Collectors.toList())
        );

        /**
         * Map And Save DTO
         */
        PersistEntity persistEntity = this.appViewMapper.map(appViewDTO);


        PersistEntity createdPersistEntity = this.appViewRepository.save(persistEntity);

        /**
         * Add new Fields From Components
         */
        this.componentDesignerService.insertComponentTableFieldsByTable(createdPersistEntity);

        return this.appViewMapper.map(createdPersistEntity);
    }

    public List<AppViewDTO> getObjectAppView() {
        List<AppViewDTO> viewsList = this.appViewRepository.getObjectAppView("AppView");
        return viewsList;
    }

    public AppViewDTO getObject(String id) {
        PersistEntity optionalView = this.appViewRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("View Does Not Exist"));

       AppViewDTO dto = appViewMapper.map(optionalView);

        if (dto.getQuery() != null) {
            String uriEncoded = EncodingUtil.encodeURIComponent(dto.getQuery());
            String encodedQuery = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
            dto.setQuery(encodedQuery);
        }

        return dto;
    }

    public void deleteObject(String id) {
        PersistEntity optionalView = this.appViewRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("View Does Not Exist"));

        this.appViewRepository.deleteById(optionalView.getId());
    }

    @Transactional
    public List<String> getViews() {
        Query query = entityManager.createNativeQuery("SELECT view_name FROM information_schema.views WHERE view_schema='"+sofiaDatabase+"';");
        List<String> viewNames = query.getResultList();
        return viewNames;
    }


    @Transactional
    public List<String> getViewFields(String viewName) {
        Query query = entityManager.createNativeQuery("SHOW COLUMNS FROM " + viewName + " FROM "+sofiaDatabase+";");
        List<Object[]> fields = query.getResultList();
        List<String> fieldNames = fields.stream().map(f -> f[0].toString()).collect(Collectors.toList());

        return fieldNames;
    }

    @Transactional
    public void deteleDatabaseView(String viewName) {
        Query query = entityManager.createNativeQuery("DROP TABLE " + viewName.replace(" ", "") + ";");
        query.executeUpdate();
    }

    @Transactional
    public void updateDatabaseView(AppViewDTO customComponentDTO) {
    }

    @Transactional
    public void createDatabaseView(AppViewDTO customComponentDTO) {
    }

    public Boolean viewOnDatabase(String viewName) {
        List<String> views = this.getViews();
        if (views.contains(viewName)) return true;
        else return false;
    }

    @Transactional
    public List<AppViewFieldDTO> generateViewFields(String sql) {

        byte[] baseDecodedSql = Base64.getDecoder().decode(sql);
        String decodedSql = EncodingUtil.decodeURIComponent(new String(baseDecodedSql));
        sql = decodedSql;

        List<AppViewFieldDTO> dtos = new ArrayList<>();
        String uuid = UUID.randomUUID().toString().replace("-", "_");
        this.createView(uuid, sql);

        Query query = entityManager.createNativeQuery("SHOW COLUMNS FROM " + uuid + " FROM "+sofiaDatabase+";");
        List<Object[]> fields = query.getResultList();

        for (Object[] field : fields) {
            AppViewFieldDTO dto = new AppViewFieldDTO();
            dto.setName(field[0].toString());
            dto.setDescription("");
            dto.setType(field[1].toString());
            dto.setEntitytype("AppViewField");

            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(field[1].toString());
            while (m.find()) {
                dto.setSize(Integer.valueOf(m.group(1)));
            }

            int index = field[1].toString().indexOf("(");
            if (index > 0) {
                dto.setType(field[1].toString().substring(0, index));
            }

            dtos.add(dto);
        }

        this.dropView(uuid);

        return dtos;
    }


    public void dropView(String name) {
        String sql = "DROP VIEW IF EXISTS "+sofiaDatabase+"." + name;
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    public void alterView(String name, String queryStr) {
        String sql = "ALTER VIEW IF EXISTS "+sofiaDatabase+"." + name + " AS " + queryStr;
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }


    public void createView(String name, String queryStr) {
        String sql = "CREATE VIEW IF NOT EXISTS "+sofiaDatabase+"." + name + " AS " + queryStr;
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }


}
