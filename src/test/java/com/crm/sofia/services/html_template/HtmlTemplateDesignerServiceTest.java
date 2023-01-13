package com.crm.sofia.services.html_template;


import com.crm.sofia.dto.html_template.HtmlTemplateDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.html_template.HtmlTemplateMapper;
import com.crm.sofia.model.html_template.HtmlTemplate;
import com.crm.sofia.repository.html_template.HtmlTemplateRepository;
import com.crm.sofia.services.auth.JWTService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class HtmlTemplateDesignerServiceTest {
    @Mock
    private HtmlTemplateRepository htmlTemplateRepository;

    @InjectMocks
    private HtmlTemplateDesignerService htmlTemplateDesignerService;

    @Mock
    private JWTService jwtService;

    private List<HtmlTemplate> htmlTemplateList;

    private HtmlTemplate htmlTemplate;

    private HtmlTemplateDTO htmlTemplateDTO;

    private List<HtmlTemplateDTO> htmlTemplateDTOList;


    @Mock
    private HtmlTemplateMapper htmlTemplateMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        htmlTemplateList = new ArrayList<>();
        htmlTemplateDTOList = new ArrayList<>();
        htmlTemplate = new HtmlTemplate().setTitle("dummyTitle").setHtml("dummyHtml");
        htmlTemplateList.add(htmlTemplate);
        htmlTemplateDTO = new HtmlTemplateDTO().setTitle("dummyTitleDTO").setHtml(Base64.getEncoder().encodeToString("dummyHtmlDTO".getBytes(StandardCharsets.UTF_8)));
        htmlTemplateDTO.setId("1");
        htmlTemplateDTOList.add(htmlTemplateDTO);

    }
    @Test
    public void getObjectTest() {
        given(htmlTemplateRepository.getObject()).willReturn(Collections.singletonList(htmlTemplateDTO));
        List<HtmlTemplateDTO> list = htmlTemplateDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo("dummyTitleDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(htmlTemplateRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(htmlTemplate));
        given(htmlTemplateMapper.map(ArgumentMatchers.any(HtmlTemplate.class))).willReturn(htmlTemplateDTO);
        HtmlTemplateDTO dto = htmlTemplateDesignerService.getObject("1");
        assertThat(htmlTemplateDTO).isNotNull();
        assertThat(htmlTemplateDTO.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(htmlTemplateRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            htmlTemplateDesignerService.getObject("1");
        });

        String expectedMessage = "HtmlTemplate Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(htmlTemplateMapper.map(ArgumentMatchers.any(HtmlTemplateDTO.class))).willReturn(htmlTemplate);
        given(htmlTemplateRepository.save(ArgumentMatchers.any(HtmlTemplate.class))).willReturn(htmlTemplate);
        htmlTemplateDesignerService.postObject(htmlTemplateDTO);
        assertThat(htmlTemplateDTO).isNotNull();
        assertThat(htmlTemplateDTO.getId()).isEqualTo("1");
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(htmlTemplateRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            htmlTemplateDesignerService.deleteObject("1");
        });
        String expectedMessage = "HtmlTemplate Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }



    }

