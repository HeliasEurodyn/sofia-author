package com.crm.sofia.services.view;

import com.crm.sofia.dto.appview.AppViewDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.dto.view.ViewDTO;
import com.crm.sofia.dto.view.ViewFieldDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.view.ViewMapper;
import com.crm.sofia.model.persistEntity.PersistEntity;
import com.crm.sofia.repository.persistEntity.PersistEntityRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.component.ComponentDesignerService;
import com.crm.sofia.utils.EncodingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ViewService {

    @Value("${sofia.db.name}")
    private String sofiaDatabase;

    private final PersistEntityRepository persistEntityRepository;
    private final ViewMapper viewMapper;
    private final EntityManager entityManager;
    private final ComponentDesignerService componentDesignerService;
    private final JWTService jwtService;

    public ViewService(PersistEntityRepository persistEntityRepository,
                       ViewMapper viewMapper,
                       EntityManager entityManager,
                       ComponentDesignerService componentDesignerService,
                       JWTService jwtService) {
        this.persistEntityRepository = persistEntityRepository;
        this.viewMapper = viewMapper;
        this.entityManager = entityManager;
        this.componentDesignerService = componentDesignerService;
        this.jwtService = jwtService;
    }

    public List<TagDTO> getTag(){
        List<TagDTO> tag = persistEntityRepository.findTagDistinct("View");
        return tag;
    }

    public List<ViewDTO> getObjectByTag(String tag){
        return this.persistEntityRepository.getObjectByTagView(tag);
    }
    public ViewDTO postObject(ViewDTO viewDTO) {


        /**
         * Remove deleted Fields From Components
         */
        this.componentDesignerService.removeComponentTableFieldsByTable(
                viewDTO.getId(),
                viewDTO.getViewFieldList()
                        .stream()
                        .map( x -> x.getId())
                        .collect(Collectors.toList())
        );

        /**
         * Map And Save DTO
         */
        PersistEntity view = this.viewMapper.map(viewDTO);

        if ((view.getId() == null ? "" : view.getId()).equals("")) {
            view.setCreatedBy(jwtService.getUserId());
            view.setCreatedOn(Instant.now());
        }
        view.setModifiedBy(jwtService.getUserId());
        view.setModifiedOn(Instant.now());

        PersistEntity createdView = this.persistEntityRepository.save(view);

        return this.viewMapper.map(createdView);
    }

    public List<ViewDTO> getObjectView() {
        List<ViewDTO> viewsList = this.persistEntityRepository.getObjectView("View");
        return viewsList;
    }

    public ViewDTO getObject(String id) {
        PersistEntity optionalView = this.persistEntityRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("View Does Not Exist"));

        ViewDTO dto = this.viewMapper.map(optionalView);

        if (dto.getQuery() != null) {
            String uriEncoded = EncodingUtil.encodeURIComponent(dto.getQuery());
            String encodedQuery = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
            dto.setQuery(encodedQuery);
        }

        return dto;
    }

    public void deleteObject(String id) {
        PersistEntity optionalView = this.persistEntityRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("View Does Not Exist"));

        this.persistEntityRepository.deleteById(optionalView.getId());
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

    public Boolean viewOnDatabase(String viewName) {
        List<String> views = this.getViews();
        if (views.contains(viewName)) return true;
        else return false;
    }

    @Transactional
    public List<ViewFieldDTO> generateViewFields(String sql) {

        byte[] baseDecodedSql = Base64.getDecoder().decode(sql);
        String decodedSql = EncodingUtil.decodeURIComponent(new String(baseDecodedSql));
        sql = decodedSql;


        List<ViewFieldDTO> dtos = new ArrayList<>();
        String uuid = UUID.randomUUID().toString().replace("-", "_");
        this.createView(uuid, sql);

        Query query = entityManager.createNativeQuery("SHOW COLUMNS FROM " + uuid + " FROM "+sofiaDatabase+";");
        List<Object[]> fields = query.getResultList();

        for (Object[] field : fields) {
            ViewFieldDTO dto = new ViewFieldDTO();
            dto.setName(field[0].toString());
            dto.setDescription("");
            dto.setType(field[1].toString());
            dto.setEntitytype("ViewField");

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

    @Transactional
    @Modifying
    public void dropView(String name) {
        String sql = "DROP VIEW IF EXISTS "+sofiaDatabase+"." + name;
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    @Transactional
    @Modifying
    public void alterView(String name, String queryStr) {
        String sql = "ALTER VIEW IF EXISTS "+sofiaDatabase+"." + name + " AS " + queryStr;
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    @Transactional
    @Modifying
    public void createView(String name, String queryStr) {
        String sql = "CREATE VIEW IF NOT EXISTS "+sofiaDatabase+"." + name + " AS " + queryStr;
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    @Transactional
    @Modifying
    public ViewDTO saveDTOAndCreate(ViewDTO dto) {

        List<TagDTO> tags =
                dto.getTags().stream().collect(Collectors.toMap(TagDTO::getId, p -> p, (p, q) -> p))
                        .values()
                        .stream().collect(Collectors.toList());

        dto.setTags(tags);

        if (dto.getQuery() != null) {
            byte[] decodedQuery = Base64.getDecoder().decode(dto.getQuery());
            String query = EncodingUtil.decodeURIComponent(new String(decodedQuery));
            dto.setQuery(query);
        }

        ViewDTO customComponentDTO = this.postObject(dto);
        this.dropView(customComponentDTO.getName());
        this.createView(
                customComponentDTO.getName(),
                customComponentDTO.getQuery());

        return customComponentDTO;
    }
}
