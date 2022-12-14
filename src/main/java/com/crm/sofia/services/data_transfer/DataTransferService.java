package com.crm.sofia.services.data_transfer;

import com.crm.sofia.dto.appview.AppViewDTO;
import com.crm.sofia.dto.chart.ChartDTO;
import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.dto.custom_query.CustomQueryDTO;
import com.crm.sofia.dto.dashboard.DashboardDTO;
import com.crm.sofia.dto.data_transfer.DataTransferDTO;
import com.crm.sofia.dto.form.FormDTO;
import com.crm.sofia.dto.html_dashboard.HtmlDashboardDTO;
import com.crm.sofia.dto.info_card.InfoCardDTO;
import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.menu.MenuDTO;
import com.crm.sofia.dto.search.SearchDTO;
import com.crm.sofia.dto.table.TableDTO;
import com.crm.sofia.dto.user.RoleDTO;
import com.crm.sofia.dto.user.UserDTO;
import com.crm.sofia.dto.view.ViewDTO;
import com.crm.sofia.dto.xls_import.XlsImportDTO;
import com.crm.sofia.exception.data_tranfer.DataImportException;
import com.crm.sofia.exception.data_tranfer.ImportedFileAlreadyExistsException;
import com.crm.sofia.exception.data_tranfer.WrongFileTypeException;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.data_transfer.DataTransferMapper;
import com.crm.sofia.model.data_transfer.DataTransfer;
import com.crm.sofia.repository.data_transfer.DataTransferRepository;
import com.crm.sofia.services.appview.AppViewService;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.chart.ChartDesignerService;
import com.crm.sofia.services.component.ComponentDesignerService;
import com.crm.sofia.services.custom_query.CustomQueryService;
import com.crm.sofia.services.dashboard.DashboardDesignerService;
import com.crm.sofia.services.form.FormDesignerService;
import com.crm.sofia.services.html_dashboard.HtmlDashboardDesignerService;
import com.crm.sofia.services.info_card.InfoCardDesignerService;
import com.crm.sofia.services.language.LanguageDesignerService;
import com.crm.sofia.services.list.ListDesignerService;
import com.crm.sofia.services.menu.MenuService;
import com.crm.sofia.services.report.ReportDesignerService;
import com.crm.sofia.services.search.SearchDesignerService;
import com.crm.sofia.services.table.TableService;
import com.crm.sofia.services.user.RoleService;
import com.crm.sofia.services.user.UserService;
import com.crm.sofia.services.view.ViewService;
import com.crm.sofia.services.xls_import.XlsImportDesignerService;
import com.crm.sofia.utils.ByteUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class DataTransferService {
    @Autowired
    private DataTransferMapper dataTransferMapper;

    @Autowired
    private DataTransferRepository dataTransferRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ListDesignerService listDesignerService;

    @Autowired
    private FormDesignerService formDesignerService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private AppViewService appViewService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ComponentDesignerService componentDesignerService;

    @Autowired
    private ChartDesignerService chartDesignerService;

    @Autowired
    private InfoCardDesignerService infoCardDesignerService;

    @Autowired
    private HtmlDashboardDesignerService htmlDashboardDesignerService;

    @Autowired
    private DashboardDesignerService dashboardDesignerService;

    @Autowired
    private ReportDesignerService reportDesignerService;

    @Autowired
    private XlsImportDesignerService xlsImportDesignerService;

    @Autowired
    private SearchDesignerService searchDesignerService;

    @Autowired
    private CustomQueryService customQueryService;

    @Autowired
    private LanguageDesignerService languageDesignerService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


    @Autowired
    private ByteUtils byteUtils;

    @Autowired
    private EntityManager entityManager;


    public List<DataTransferDTO> getObject() {
        List<DataTransferDTO> dataTransferList = dataTransferRepository.getObject();
        return dataTransferList;
    }

    public DataTransferDTO getObject(String id) {
        DataTransfer optionalEntity = dataTransferRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("DataTransfer Does Not Exist"));

        DataTransfer entity = optionalEntity;
        DataTransferDTO dto = dataTransferMapper.map(entity);


        return dto;
    }

    public DataTransferDTO postObject(DataTransferDTO dataTransferDto) {

        DataTransfer dataTransfer = dataTransferMapper.map(dataTransferDto);

        if (dataTransferDto.getId() == null) {
            dataTransfer.setCreatedOn(Instant.now());
            dataTransfer.setCreatedBy(jwtService.getUserId());
        }
        dataTransfer.setModifiedOn(Instant.now());
        dataTransfer.setModifiedBy(jwtService.getUserId());
        DataTransfer savedData = dataTransferRepository.save(dataTransfer);

        return dataTransferMapper.map(savedData);
    }

    public void deleteObject(String id) {

        DataTransfer entity = dataTransferRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("DataTransfer Does Not Exist"));

        dataTransferRepository.deleteById(entity.getId());
    }

    public byte[] export(String id) throws IOException, ClassNotFoundException {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        DataTransferDTO dataTransferDTO = this.getObject(id);

        dataTransferDTO.setTables(new ArrayList<>());
        dataTransferDTO.getTableIds().forEach(tableId -> {
            TableDTO tableDTO = this.tableService.getObject(tableId);
            dataTransferDTO.getTables().add(tableDTO);
        });

        dataTransferDTO.setViews(new ArrayList<>());
        dataTransferDTO.getViewIds().forEach(viewId -> {
            ViewDTO viewDTO = this.viewService.getObject(viewId);
            dataTransferDTO.getViews().add(viewDTO);
        });

        dataTransferDTO.setAppViews(new ArrayList<>());
        dataTransferDTO.getAppViewIds().forEach(appViewId -> {
            AppViewDTO appViewDTO = this.appViewService.getObject(appViewId);
            dataTransferDTO.getAppViews().add(appViewDTO);
        });

        dataTransferDTO.setLists(new ArrayList<>());
        dataTransferDTO.getListIds().forEach(listId -> {
            ListDTO listDTO = this.listDesignerService.getObject(listId);
            dataTransferDTO.getLists().add(listDTO);
        });

        dataTransferDTO.setForms(new ArrayList<>());
        dataTransferDTO.getFormIds().forEach(formId -> {
            FormDTO formDTO = this.formDesignerService.getObject(formId);
            dataTransferDTO.getForms().add(formDTO);
        });

        dataTransferDTO.setMenus(new ArrayList<>());
        dataTransferDTO.getMenuIds().forEach(menuId -> {
            MenuDTO dto = this.menuService.getObject(menuId, null);
            dataTransferDTO.getMenus().add(dto);
        });

        dataTransferDTO.setComponents(new ArrayList<>());
        dataTransferDTO.getComponentIds().forEach(componentId -> {
            ComponentDTO dto = this.componentDesignerService.getObject(componentId);
            dataTransferDTO.getComponents().add(dto);
        });

        dataTransferDTO.setCharts(new ArrayList<>());
        dataTransferDTO.getChartIds().forEach(chartId -> {
            ChartDTO dto = this.chartDesignerService.getObject(chartId);
            dataTransferDTO.getCharts().add(dto);
        });

        dataTransferDTO.setInfoCards(new ArrayList<>());
        dataTransferDTO.getInfoCardIds().forEach(cid -> {
            InfoCardDTO dto = this.infoCardDesignerService.getObject(cid);
            dataTransferDTO.getInfoCards().add(dto);
        });

        dataTransferDTO.setHtmlParts(new ArrayList<>());
        dataTransferDTO.getHtmlPartIds().forEach(cid -> {
            HtmlDashboardDTO dto = this.htmlDashboardDesignerService.getObject(cid);
            dataTransferDTO.getHtmlParts().add(dto);
        });

        dataTransferDTO.setDashboards(new ArrayList<>());
        dataTransferDTO.getDashboardIds().forEach(cid -> {
            DashboardDTO dto = this.dashboardDesignerService.getObject(cid);
            dataTransferDTO.getDashboards().add(dto);
        });

        dataTransferDTO.setReports(new ArrayList<>());
//        dataTransferDTO.getReportIds().forEach(cid -> {
//            ReportDTO dto = this.reportDesignerService.getObject(cid);
//            dataTransferDTO.getReports().add(dto);
//        });

        dataTransferDTO.setXlsImports(new ArrayList<>());
        dataTransferDTO.getXlsImportIds().forEach(cid -> {
            XlsImportDTO dto = this.xlsImportDesignerService.getObject(cid);
            dataTransferDTO.getXlsImports().add(dto);
        });

        dataTransferDTO.setSearchs(new ArrayList<>());
        dataTransferDTO.getSearchIds().forEach(cid -> {
            SearchDTO dto = this.searchDesignerService.getObject(cid);
            dataTransferDTO.getSearchs().add(dto);
        });

        dataTransferDTO.setCustomQuerys(new ArrayList<>());
        dataTransferDTO.getCustomQueryIds().forEach(cid -> {
            CustomQueryDTO dto = this.customQueryService.getObject(cid);
            dataTransferDTO.getCustomQuerys().add(dto);
        });

        dataTransferDTO.setLanguages(new ArrayList<>());
        dataTransferDTO.getLanguageIds().forEach(cid -> {
            LanguageDTO dto = this.languageDesignerService.getObject(cid);
            dataTransferDTO.getLanguages().add(dto);
        });

        dataTransferDTO.setRoles(new ArrayList<>());
        dataTransferDTO.getRoleIds().forEach(cid -> {
            RoleDTO dto = this.roleService.getObject(cid);
            dataTransferDTO.getRoles().add(dto);
        });

        dataTransferDTO.setUsers(new ArrayList<>());
        dataTransferDTO.getUserIds().forEach(cid -> {
            UserDTO dto = this.userService.getTransferUser(cid);
            dataTransferDTO.getUsers().add(dto);
        });

        String jsonDataTransferDto =ow.writeValueAsString(dataTransferDTO);
        return Base64.getEncoder().encode(jsonDataTransferDto.getBytes());
    }

    @Transactional(rollbackFor = Exception.class)
    public void importMultipartFile(MultipartFile multipartFile) throws Exception {

        /* Check file extension */
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        if (!extension.equals("sofia")) {
            throw new WrongFileTypeException();
        }

        this.importByteArray(multipartFile.getBytes());
    }

    public void importByteArray(byte[] bytes) throws Exception {

        /* Bytes to DataTransferDTO */
        byte[] decodedBytes = Base64.getDecoder().decode(bytes);
        String jsonDataTransferDto = new String(decodedBytes);
        ObjectMapper mapper = new ObjectMapper();
        DataTransferDTO dataTransferDTO = mapper.readValue(jsonDataTransferDto, DataTransferDTO.class);

        Integer currentVersion = this.dataTransferRepository.getCurrentVersion(dataTransferDTO.getId());

        if ((currentVersion == null ? 0 : currentVersion) < dataTransferDTO.getCurrentVersion()) {
            this.importDTO(dataTransferDTO);
        }else{
            throw new ImportedFileAlreadyExistsException();
        }
    }

    public void importDTO(DataTransferDTO dataTransferDTO) throws Exception {

        try{
            for (TableDTO dto : dataTransferDTO.getTables()) {
                //   version = this.getCurrentVersion("Menu", dto.getId());
                //   dto.setVersion(version);
                this.tableService.save(dto);
            }

            for (AppViewDTO dto : dataTransferDTO.getAppViews()) {
                this.appViewService.postObject(dto);
            }

            for (ViewDTO dto : dataTransferDTO.getViews()) {
                this.viewService.postObject(dto);
            }

            for (LanguageDTO dto : dataTransferDTO.getLanguages()) {
                this.languageDesignerService.postObject(dto);
            }

            for (RoleDTO dto : dataTransferDTO.getRoles()) {
                this.roleService.postObject(dto);
            }

            for (MenuDTO dto : dataTransferDTO.getMenus()) {
                this.menuService.postObject(dto);
            }

            for (ComponentDTO dto : dataTransferDTO.getComponents()) {
                this.componentDesignerService.postObject(dto);
            }

            for (UserDTO dto : dataTransferDTO.getUsers()) {
                this.userService.postTransferUser(dto);
            }

            for (ListDTO dto : dataTransferDTO.getLists()) {
                this.listDesignerService.postObject(dto);
            }

            for (FormDTO dto : dataTransferDTO.getForms()) {
                this.formDesignerService.postObject(dto);
            }

            for (ChartDTO dto : dataTransferDTO.getCharts()) {
                this.chartDesignerService.postObject(dto);
            }

            for (InfoCardDTO dto : dataTransferDTO.getInfoCards()) {
                this.infoCardDesignerService.postObject(dto);
            }

            for (HtmlDashboardDTO dto : dataTransferDTO.getHtmlParts()) {
                this.htmlDashboardDesignerService.postObject(dto);
            }

            for (DashboardDTO dto : dataTransferDTO.getDashboards()) {
                this.dashboardDesignerService.postObject(dto);
            }

//        for (ReportDTO dto : dataTransferDTO.getReports()) {
//            this.reportDesignerService.postObject(dto);
//        }

            for (XlsImportDTO dto : dataTransferDTO.getXlsImports()) {
                this.xlsImportDesignerService.postObject(dto);
            }

            for (SearchDTO dto : dataTransferDTO.getSearchs()) {
                this.searchDesignerService.postObject(dto);
            }

            for (CustomQueryDTO dto : dataTransferDTO.getCustomQuerys()) {
                this.customQueryService.postObject(dto);
            }

            this.postObject(dataTransferDTO);

//        tryRunQuery(dataTransferDTO);
        }catch (Exception e) {
            throw new DataImportException();
        }
    }

//    @Transactional
//    @Modifying
//    public void tryRunQuery(DataTransferDTO dataTransferDTO) {
//        String decQuery = new String(Base64.getDecoder().decode(dataTransferDTO.getQuery()));
//
//        if ((decQuery == null ? "" : decQuery).replace(" ", "").length() > 0) {
//            Query query = entityManager.createNativeQuery(decQuery);
//            query.executeUpdate();
//        }
//    }

    private Long getCurrentVersion(String entity, String id) {

        Query query = entityManager.
                createQuery("Select t.version from " + entity + " t WHERE t.id = :id");
        query.setParameter("id", id);
        List<Long> results = query.getResultList();

        if (results.size() == 0) {
            return 0L;
        } else {
            return results.get(0);
        }

    }

}
