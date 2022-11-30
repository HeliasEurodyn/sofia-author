package com.crm.sofia.services.appview;

import com.crm.sofia.dto.appview.AppViewDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.appview.AppViewMapper;
import com.crm.sofia.model.persistEntity.PersistEntity;
import com.crm.sofia.repository.persistEntity.PersistEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AppViewServiceTest {
    List<PersistEntity> persistEntityList;
    List<Object[]> data;
    @Mock
    private PersistEntityRepository appViewRepository;
    @InjectMocks
    private AppViewService appViewService;
    private AppViewDTO appViewDTO;
    private List<AppViewDTO> appViewDTOlist;
    @Mock
    private AppViewMapper appViewMapper;
    private PersistEntity persistEntity;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query query;

    public AppViewServiceTest() {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        appViewDTOlist = new ArrayList<>();
        persistEntityList = new ArrayList<>();
        appViewDTO = new AppViewDTO();
        appViewDTO.setDescription("Demo");
        appViewDTO.setName("Frodo Baggins");
        appViewDTO.setQuery("Test");
        appViewDTOlist.add(appViewDTO);
        persistEntity = new PersistEntity();
        persistEntity.setDescription("Demo");
        persistEntityList.add(persistEntity);

        data = new ArrayList<>();
        Object[] oj = new Object[2];
        oj[0] = 1;
        oj[1] = "2(2))";
        data.add(oj);
    }

//    @Test
//    public void postObjectTest() {
//        given(appViewRepository.save(ArgumentMatchers.any(PersistEntity.class))).willReturn(persistEntity);
//        given(appViewMapper.map(ArgumentMatchers.any(AppViewDTO.class))).willReturn(persistEntity);
//        AppViewDTO appView = appViewService.postObject(appViewDTO);
//    }

    @Test
    public void getObjectTest() {
        given(appViewRepository.getObjectAppView(ArgumentMatchers.anyString())).willReturn(Collections.singletonList(appViewDTO));
        List<AppViewDTO> list = appViewService.getObjectAppView();
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("Frodo Baggins");
    }

    @Test
    public void getObjectByIdTest() {
        given(appViewRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(persistEntity));
        AppViewDTO dto = appViewService.getObject("6L");

    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(appViewRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            appViewService.getObject("6L");
        });

        String expectedMessage = "View Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void getDeleteByIdTest() {
        given(appViewRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(persistEntity));
        appViewService.deleteObject("6L");

    }

    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(appViewRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            appViewService.deleteObject("6L");
        });
        String expectedMessage = "View Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }


    @Test
    public void getViewsTest() {
        Mockito.when(entityManager.createNativeQuery(ArgumentMatchers.anyString())).thenReturn(query);
        given(query.getResultList()).willReturn(anyList());
        appViewService.getViews();
    }

    @Test
    public void getViewFieldsTest() {
        Mockito.when(entityManager.createNativeQuery(ArgumentMatchers.anyString())).thenReturn(query);
        given(query.getResultList()).willReturn(anyList());
        appViewService.getViewFields("dummy");
    }

    @Test
    public void deteleDatabaseViewTest() {
        Mockito.when(entityManager.createNativeQuery(ArgumentMatchers.anyString())).thenReturn(query);
        given(query.executeUpdate()).willReturn(1);
        appViewService.deteleDatabaseView("dummy");
    }

    @Test
    public void viewOnDatabaseTest() {
        Mockito.when(entityManager.createNativeQuery(ArgumentMatchers.anyString())).thenReturn(query);
        given(query.getResultList()).willReturn(anyList());
        appViewService.viewOnDatabase("dummy");
    }

    @Test
    public void generateViewFieldsTest() {
        Mockito.when(entityManager.createNativeQuery(ArgumentMatchers.anyString())).thenReturn(query);
        given(query.getResultList()).willReturn(data);
        appViewService.generateViewFields("Select * from table");
    }

    @Test
    public void dropViewTest() {
        Mockito.when(entityManager.createNativeQuery(ArgumentMatchers.anyString())).thenReturn(query);
        appViewService.dropView("table");
    }

    @Test
    public void alterViewTest() {
        Mockito.when(entityManager.createNativeQuery(ArgumentMatchers.anyString())).thenReturn(query);
        appViewService.alterView("table", "query");
    }

    @Test
    public void createViewTest() {
        Mockito.when(entityManager.createNativeQuery(ArgumentMatchers.anyString())).thenReturn(query);
        appViewService.createView("table", "query");
    }
}
