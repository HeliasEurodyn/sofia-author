package com.crm.sofia.services.html_dashboard;


import com.crm.sofia.dto.html_dashboard.HtmlDashboardDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.html_dashboard.HtmlDashboardMapper;
import com.crm.sofia.model.html_dashboard.HtmlDashboard;
import com.crm.sofia.repository.html_dashboard.HtmlDashboardRepository;
import com.crm.sofia.services.auth.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class HtmlDashboardDesignerServiceTest {
    @Mock
    private HtmlDashboardRepository htmlDashboardRepository;

    @InjectMocks
    private HtmlDashboardDesignerService htmlDashboardDesignerService;

    @Mock
    private JWTService jwtService;

    private List<HtmlDashboard> htmlDashboardList;

    private HtmlDashboard htmlDashboard;

    private HtmlDashboardDTO htmlDashboardDTO;

    @Mock
    private HtmlDashboardMapper htmlDashboardMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        htmlDashboardList = new ArrayList<>();
        htmlDashboard = new HtmlDashboard().setName("dummyName");
        htmlDashboardList.add(htmlDashboard);
        htmlDashboardDTO = new HtmlDashboardDTO().setName("dummyNameDTO");
        htmlDashboardDTO.setId("1");

    }

    @Test
    public void getObjectTest() {
        given(htmlDashboardRepository.getObject()).willReturn(Collections.singletonList(htmlDashboardDTO));
        List<HtmlDashboardDTO> list = htmlDashboardDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(htmlDashboardRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(htmlDashboard));
        given(htmlDashboardMapper.map(ArgumentMatchers.any(HtmlDashboard.class))).willReturn(htmlDashboardDTO);
        HtmlDashboardDTO dto = htmlDashboardDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(htmlDashboardRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            htmlDashboardDesignerService.getObject("1");
        });

        String expectedMessage = "HtmlDashboard Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(htmlDashboardRepository.save(ArgumentMatchers.any(HtmlDashboard.class))).willReturn(htmlDashboard);
        given(htmlDashboardMapper.map(ArgumentMatchers.any(HtmlDashboardDTO.class))).willReturn(htmlDashboard);
        given(htmlDashboardMapper.map(ArgumentMatchers.any(HtmlDashboard.class))).willReturn(htmlDashboardDTO);
        HtmlDashboardDTO dto = htmlDashboardDesignerService.postObject(htmlDashboardDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(htmlDashboardRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            htmlDashboardDesignerService.deleteObject("1");
        });
        String expectedMessage = "HtmlDashboard Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
