package com.crm.sofia.services.report;

import com.crm.sofia.dto.report.ReportDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.report.ReportMapper;
import com.crm.sofia.model.report.Report;
import com.crm.sofia.repository.report.ReportRepository;
import com.crm.sofia.services.auth.JWTService;
import net.sf.jasperreports.parts.subreport.SubreportPartComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReportDesignerServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportDesignerService reportDesignerService;

    @Mock
    private JWTService jwtService;

    private Optional<Report> optionalEntity;

    private List<Report> reportList;

    private List<Report> subreports;


    private Report report;


    private ReportDTO reportDTO;

    @Mock
    private ReportMapper reportMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reportList = new ArrayList<>();
        report = new Report().setName("dummyName");
        reportList.add(report);
        reportDTO = new ReportDTO().setName("dummyNameDTO");
        reportDTO.setId("1");
        reportDTO.setSubreports(new ArrayList<>());







    }

    @Test
    public void getObjectTest() {
        given(reportRepository.getObject()).willReturn(Collections.singletonList(reportDTO));
        List<ReportDTO> list = reportDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(reportRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(report));
        given(reportMapper.map(ArgumentMatchers.any(Report.class))).willReturn(reportDTO);
        ReportDTO dto = reportDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(reportRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            reportDesignerService.getObject("1");
        });

        String expectedMessage = "Report Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

//    @Test
//    public void postObjectTest() throws IOException {
//        given(reportRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(report));
//        given(reportRepository.save(ArgumentMatchers.any(Report.class))).willReturn(report);
//        given(reportMapper.map(ArgumentMatchers.any(ReportDTO.class))).willReturn(report);
//        ReportDTO dto = reportDesignerService.postObject(reportDTO);
//        assertThat(dto).isNotNull();
//        assertThat(dto.getId()).isEqualTo("1");
//    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(reportRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            reportDesignerService.deleteObject("1");
        });
        String expectedMessage = "Report Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
