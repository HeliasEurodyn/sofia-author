package com.crm.sofia.services.language;


import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.language.LanguageMapper;
import com.crm.sofia.model.language.Language;
import com.crm.sofia.repository.language.LanguageRepository;
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
public class LanguageDesignerServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageDesignerService languageDesignerService;

    @Mock
    private JWTService jwtService;

    private List<Language> languageList;

    private Language language;

    private LanguageDTO languageDTO;

    @Mock
    private LanguageMapper languageMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        languageList = new ArrayList<>();
        language = new Language().setName("dummyName");
        languageList.add(language);
        languageDTO = new LanguageDTO().setName("dummyNameDTO");
        languageDTO.setId("1");

    }

    @Test
    public void getObjectTest() {
        given(languageRepository.getObject()).willReturn(Collections.singletonList(languageDTO));
        List<LanguageDTO> list = languageDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(languageRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(language));
        given(languageMapper.map(ArgumentMatchers.any(Language.class))).willReturn(languageDTO);
        LanguageDTO dto = languageDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(languageRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            languageDesignerService.getObject("1");
        });

        String expectedMessage = "Language Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(languageRepository.save(ArgumentMatchers.any(Language.class))).willReturn(language);
        given(languageMapper.map(ArgumentMatchers.any(LanguageDTO.class))).willReturn(language);
        given(languageMapper.map(ArgumentMatchers.any(Language.class))).willReturn(languageDTO);
        LanguageDTO dto = languageDesignerService.postObject(languageDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(languageRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            languageDesignerService.deleteObject("1");
        });
        String expectedMessage = "Language Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
