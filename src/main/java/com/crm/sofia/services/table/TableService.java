package com.crm.sofia.services.table;

import com.crm.sofia.dto.table.ForeignKeyConstrainDTO;
import com.crm.sofia.dto.table.RemoveForeignKeyConstrainDTO;
import com.crm.sofia.dto.table.TableDTO;
import com.crm.sofia.dto.table.TableFieldDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.exception.table.ForeignKeyConstrainAlreadyExist;
import com.crm.sofia.mapper.persistEntity.ForeignKeyConstrainMapper;
import com.crm.sofia.mapper.table.TableMapper;
import com.crm.sofia.model.persistEntity.ForeignKeyConstrain;
import com.crm.sofia.model.persistEntity.PersistEntity;
import com.crm.sofia.repository.persistEntity.PersistEntityRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.component.ComponentDesignerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TableService {

    @Value("${sofia.db.name}")
    private String sofiaDatabase;

    private final PersistEntityRepository persistEntityRepository;
    private final TableMapper tableMapper;
    private final EntityManager entityManager;
    private final JWTService jwtService;
    private final ComponentDesignerService componentDesignerService;

    private final ForeignKeyConstrainMapper foreignKeyConstrainMapper;

    public TableService(PersistEntityRepository persistEntityRepository,
                        TableMapper tableMapper,
                        EntityManager entityManager, JWTService jwtService,
                        ComponentDesignerService componentDesignerService, ForeignKeyConstrainMapper foreignKeyConstrainMapper) {
        this.persistEntityRepository = persistEntityRepository;
        this.tableMapper = tableMapper;
        this.entityManager = entityManager;
        this.jwtService = jwtService;
        this.componentDesignerService = componentDesignerService;
        this.foreignKeyConstrainMapper = foreignKeyConstrainMapper;
    }

    public TableDTO postObject(TableDTO componentDTO) {
        PersistEntity persistEntity = this.tableMapper.map(componentDTO);

        if ((persistEntity.getId() == null ? "" : persistEntity.getId()).equals("")) {
            persistEntity.setCreatedBy(jwtService.getUserId());
            persistEntity.setCreatedOn(Instant.now());
        }
        persistEntity.setModifiedBy(jwtService.getUserId());
        persistEntity.setModifiedOn(Instant.now());

        PersistEntity entity = this.persistEntityRepository.save(persistEntity);

        return this.tableMapper.map(entity);
    }

    public List<TableDTO> getObjectTable() {
        List<TableDTO> tablesList = this.persistEntityRepository.getObjectTable("Table");
        return tablesList;
    }

    public TableDTO getObject(String id) {
        PersistEntity optionalComponent = this.persistEntityRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Component Does Not Exist"));

        TableDTO tableDTO = this.tableMapper.map(optionalComponent);
        this.shortTableFields(tableDTO);
        return tableDTO;
    }

    public void shortTableFields(TableDTO tableDTO) {
        tableDTO.getTableFieldList().stream()
                .filter(field -> field.getShortOrder() == null)
                .forEach(field -> field.setShortOrder(0L));

        List<TableFieldDTO> shortedFieldList = tableDTO.getTableFieldList()
                .stream()
                .sorted(Comparator.comparingLong(TableFieldDTO::getShortOrder)).collect(Collectors.toList());
        tableDTO.setTableFieldList(shortedFieldList);
    }

    public void deleteObject(String id) {
        PersistEntity optionalPersistEntity = this.persistEntityRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Persist Entity Does Not Exist"));

        this.componentDesignerService.removeComponentTablesByTableId(id);
        this.persistEntityRepository.deleteById(optionalPersistEntity.getId());
    }

    @Transactional
    public List<String> getTables() {
        Query query = entityManager.createNativeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='" + this.sofiaDatabase + "';");
        List<String> tableNames = query.getResultList();
        return tableNames;
    }

    public List<String> getExistingTableFields(String tableName) {
        Query query = entityManager.createNativeQuery("SHOW COLUMNS FROM " + tableName + " FROM " + this.sofiaDatabase + ";");
        List<Object[]> fields = query.getResultList();
        List<String> fieldNames = fields.stream().map(f -> f[0].toString()).collect(Collectors.toList());

        return fieldNames;
    }

    @Transactional
    public void deteleDatabaseTable(String tableName) {
        Query query = entityManager.createNativeQuery("DROP TABLE " + tableName.replace(" ", "") + ";");
        query.executeUpdate();
    }

    public void updateDatabaseTable(TableDTO tableDTO) {
        List<String> existingTableFields = this.getExistingTableFields(tableDTO.getName().replace(" ", ""));
        int fieldCounter = 0;
        String sql = "";
        sql += "ALTER TABLE " + tableDTO.getName().replace(" ", "");
        sql += " \n";
        for (TableFieldDTO tableFieldDTO : tableDTO.getTableFieldList()) {

            if (existingTableFields.contains(tableFieldDTO.getName().replace(" ", ""))) {
                continue;
            }

            if (fieldCounter > 0) {
                sql += ",";
            }
            sql += " ADD COLUMN ";
            sql += tableFieldDTO.getName().replace(" ", "") + " ";
            sql += " " + tableFieldDTO.getType()
                    .replace(" ", "")
                    .replace("datetime", "timestamp")
                    .replace("password", "varchar");

            if (Arrays.asList("varchar", "password").contains(tableFieldDTO.getType())) {
                sql += " (" + tableFieldDTO.getSize().toString().replace(" ", "") + ") ";
            }

            if (tableFieldDTO.getIsUnsigned()) {
                sql += " UNSIGNED ";
            }

            if (tableFieldDTO.getHasNotNull()) {
                sql += " NOT NULL ";
            } else {
                sql += " NULL ";
            }

            if (tableFieldDTO.getHasDefault()) {
                sql += " DEFAULT " + tableFieldDTO.getDefaultValue();
            }

            if (tableFieldDTO.getAutoIncrement()) {
                sql += " AUTO_INCREMENT ";
            }

            if (tableFieldDTO.getPrimaryKey()) {
                sql += " PRIMARY KEY ";
            }

            sql += "\n";

            fieldCounter++;
        }
        sql += " ; ";

        if (fieldCounter == 0) return;

        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    public void createDatabaseTableIfNotExist(TableDTO tableDTO) {
        if (tableDTO.getTableFieldList().size() == 0) return;

        int fieldCounter = 0;
        String sql = "";
        sql += "CREATE TABLE IF NOT EXISTS " + tableDTO.getName().replace(" ", "");
        sql += " ( ";
        for (TableFieldDTO tableFieldDTO : tableDTO.getTableFieldList()) {
            if (fieldCounter > 0) {
                sql += ",";
            }
            sql += tableFieldDTO.getName().replace(" ", "") + " ";
            sql += " " + tableFieldDTO.getType()
                    .replace(" ", "")
                    .replace("datetime", "timestamp")
                    .replace("password", "varchar");

            if (Arrays.asList("varchar", "password").contains(tableFieldDTO.getType())) {
                sql += " (" + tableFieldDTO.getSize().toString().replace(" ", "") + ") ";
            }

            if (tableFieldDTO.getIsUnsigned()) {
                sql += " UNSIGNED ";
            }

            if (tableFieldDTO.getHasNotNull()) {
                sql += " NOT NULL ";
            } else {
                sql += " NULL ";
            }

            if (tableFieldDTO.getHasDefault()) {
                sql += " DEFAULT " + tableFieldDTO.getDefaultValue();
            }

            if (tableFieldDTO.getAutoIncrement()) {
                sql += " AUTO_INCREMENT ";
            }

            if (tableFieldDTO.getPrimaryKey()) {
                sql += " PRIMARY KEY ";
            }

            sql += "\n";

            fieldCounter++;
        }
        sql += " ); ";
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    public Boolean tableOnDatabase(String tableName) {
        List<String> tables = this.getTables();
        if (tables.contains(tableName)) return true;
        else return false;
    }

//    @Transactional
//    public TableDTO save(TableDTO dto) {
//        TableDTO createdDTO = this.postObject(dto);
//        this.createDatabaseTableIfNotExist(createdDTO);
//        return createdDTO;
//    }

    @Transactional
    public TableDTO save(TableDTO dto) {

        /**
         * Remove deleted Fields From Components
         */
        this.componentDesignerService.removeComponentTableFieldsByTable(
                dto.getId(),
                dto.getTableFieldList()
                        .stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList())
        );

        /**
         * Save DTO
         */
        TableDTO createdDTO = this.postObject(dto);

        /**
         * Create Database Table If Not Exists
         */
        this.createDatabaseTableIfNotExist(createdDTO);

        /**
         * If Database Table was already existing, Create new table fields that were not there
         */
        this.updateDatabaseTable(createdDTO);

        /**
         * Add Foreign Key Constrain
         */
        this.addForeignKeyConstrain(createdDTO);

        /**
         * Add new Fields From Components
         */
        this.componentDesignerService.insertComponentTableFieldsByTable(this.tableMapper.map(createdDTO));

        return createdDTO;
    }

    public void addForeignKeyConstrain(TableDTO tableDTO) {

        List<ForeignKeyConstrainDTO> newForeignKeyConstrains = removeExistingForeignKeyConstrains(tableDTO);

        if (newForeignKeyConstrains != null && !newForeignKeyConstrains.isEmpty()) {


            int counter = 1;
            int iterations = newForeignKeyConstrains.size();
            String sql = "";
            sql += "ALTER TABLE " + tableDTO.getName().replace(" ", "");
            sql += " \n";

            for (ForeignKeyConstrainDTO foreignKeyConstrainDTO : newForeignKeyConstrains) {

                sql += " ADD CONSTRAINT ";
                sql += foreignKeyConstrainDTO.getName();
                sql += " FOREIGN KEY ";
                sql += "(" + foreignKeyConstrainDTO.getFieldName() + ")";
                sql += " REFERENCES ";
                sql += foreignKeyConstrainDTO.getReferredTable().getName() + "(" + foreignKeyConstrainDTO.getReferredField().getName() + ")";
                sql += " ON UPDATE " + foreignKeyConstrainDTO.getOnUpdate() + " ON DELETE " + foreignKeyConstrainDTO.getOnDelete();
                if (counter < iterations) {
                    sql += ",";
                }
                counter++;
                sql += "\n";
            }
            sql += " ; ";


            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
        }
    }

    public List<ForeignKeyConstrainDTO> removeExistingForeignKeyConstrains(TableDTO tableDTO) {
        List<String> existingForeignKeyConstrains = getAllExistingConstrains();
        List<String> existingForeignKeyConstrainsOfTheTable = getExistingConstrainsOfSpecifTable(tableDTO);
        List<ForeignKeyConstrainDTO> remainingForeignKeyConstrains = null;
        List<ForeignKeyConstrainDTO> addedForeignKeyConstrain = null;
        if (Optional.of(tableDTO).isPresent()) {

            remainingForeignKeyConstrains = tableDTO.getForeignKeyConstrainList()
                    .stream()
                    .filter(foreignKeyConstrain1 -> existingForeignKeyConstrainsOfTheTable
                            .stream()
                            .noneMatch(foreignKeyConstrainName -> foreignKeyConstrainName.equals(foreignKeyConstrain1.getName())))
                    .collect(Collectors.toList());

            boolean foreignKeyConstrainAlreadyExist = remainingForeignKeyConstrains
                    .stream()
                    .map(foreignKeyConstrainDTO -> foreignKeyConstrainDTO.getName()).anyMatch(existingForeignKeyConstrains::contains);

            if(foreignKeyConstrainAlreadyExist){
                throw new ForeignKeyConstrainAlreadyExist();
            }
        }

        return remainingForeignKeyConstrains;
    }

    public List<String> getAllExistingConstrains() {
        Query query = entityManager.createNativeQuery("select CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE;");
        List<String> existingConstrains = query.getResultList();
        return existingConstrains;
    }

    public List<String> getExistingConstrainsOfSpecifTable(TableDTO tableDTO) {
        if(tableDTO!=null){
            char QuotationMark  = '"';
            Query query = entityManager.createNativeQuery("select CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE WHERE TABLE_NAME=" + QuotationMark + tableDTO.getName() + QuotationMark + ";");
            List<String> existingConstrains = query.getResultList();
            return existingConstrains;
        }
        return Collections.emptyList();
    }

    public List<TableFieldDTO> generateTableFields(String name) {
        List<TableFieldDTO> dtos = new ArrayList<>();

        if (!this.tableOnDatabase(name)) {
            return dtos;
        }

        Query query = entityManager.createNativeQuery("SHOW COLUMNS FROM " + name + " FROM " + sofiaDatabase + ";");
        List<Object[]> fields = query.getResultList();

        for (Object[] field : fields) {
            TableFieldDTO dto = new TableFieldDTO();
            dto.setName(field[0].toString());
            dto.setDescription("");
            dto.setType(field[1].toString());
            dto.setEntitytype("TableField");

            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(field[1].toString());
            while (m.find()) {
                dto.setSize(Integer.valueOf(m.group(1)));
            }

            int index = field[1].toString().indexOf("(");
            if (index > 0) {
                String type = field[1].toString().substring(0, index);
                dto.setType(type);
            } else {
                String type = field[1].toString();
                if (type.equals("timestamp")) type = "datetime";
                if (type.equals("password")) type = "varchar";
                dto.setType(type);
            }

            String isNullField = field[2].toString();
            if (isNullField.equals("NO")) dto.setHasNotNull(true);
            else dto.setHasNotNull(false);

            String keyField = field[3].toString();
            if (keyField.equals("PRI")) dto.setPrimaryKey(true);
            else dto.setPrimaryKey(false);

            if (field[4] != null) {
                dto.setDefaultValue(field[4].toString());
                dto.setHasDefault(true);
            } else {
                dto.setHasDefault(false);
            }
            String extraField = field[5].toString();

            if (extraField.equals("auto_increment")) dto.setAutoIncrement(true);
            else dto.setAutoIncrement(false);

            dto.setIsUnsigned(false);

            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Map<String, String>> DropForeignKeyConstrain(RemoveForeignKeyConstrainDTO dto) {

        Map<String,String> response = new HashMap<>();

        if(dto!= null){
            if(dto.getForeignKeyConstrainDTO()!= null && dto.getTableDTO()!=null){

                ForeignKeyConstrainDTO foreignKeyConstrainDTO = dto.getForeignKeyConstrainDTO();
                TableDTO tableDTO = dto.getTableDTO();
                PersistEntity persistEntity = persistEntityRepository.findById(tableDTO.getId())
                        .orElseThrow(() -> new DoesNotExistException("Table Does Not Exist"));

                ForeignKeyConstrain foreignKeyConstrain = foreignKeyConstrainMapper.map(foreignKeyConstrainDTO);

                persistEntity.removeForeignKeyConstrain(foreignKeyConstrain);

                String sql = "";
                sql += "ALTER TABLE " + tableDTO.getName().replace(" ", "");
                sql += " \n";
                sql += " DROP CONSTRAINT ";
                sql += foreignKeyConstrainDTO.getName();
                sql += " ; ";

                Query query = entityManager.createNativeQuery(sql);
                query.executeUpdate();

                response.put("message","The foreign key constrain  has been removed successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);

            }
        }
        response.put("message","Table or Foreign Key are missing");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
