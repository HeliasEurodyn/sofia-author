package com.crm.sofia.services.chart;


import com.crm.sofia.dto.chart.ChartDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.chart.ChartMapper;
import com.crm.sofia.mapper.chart.ChartMapperImpl;
import com.crm.sofia.model.chart.Chart;
import com.crm.sofia.repository.chart.ChartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChartDesignerServiceTest {

    @Mock
    private ChartRepository chartRepository;

    @InjectMocks
    private ChartDesignerService chartDesignerService;

    @Spy
    private final ChartMapper chartMapper = new ChartMapperImpl();

    private List<Chart> chartList;

    private Chart chart;

    private ChartDTO chartDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        chartList = new ArrayList<>();
        chart = new Chart().setTitle("dummyTitle").setQuery("dummyQuery");
        chartList.add(chart);
        chartDTO = new ChartDTO().setTitle("dummyTitleDTO").setQuery(Base64.getEncoder().encodeToString("dummyQueryDTO".getBytes(StandardCharsets.UTF_8)));
        chartDTO.setId("1");
    }

    @Test
    public void getObjectTest() {
        given(chartRepository.getObject()).willReturn(Collections.singletonList(chartDTO));
        List<ChartDTO> list = chartDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo("dummyTitleDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(chartRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(chart));
        ChartDTO dto = chartDesignerService.getObject("1");
        assertThat(chartDTO).isNotNull();
        assertThat(chartDTO.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(chartRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            chartDesignerService.getObject("1");
        });

        String expectedMessage = "Chart Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(chartRepository.save(ArgumentMatchers.any(Chart.class))).willReturn(chart);
        chartDesignerService.postObject(chartDTO);
        assertThat(chartDTO).isNotNull();
        assertThat(chartDTO.getId().equals("1"));
    }

    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(chartRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            chartDesignerService.deleteObject("1");
        });
        String expectedMessage = "Chart Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
