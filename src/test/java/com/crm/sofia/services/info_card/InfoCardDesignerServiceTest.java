package com.crm.sofia.services.info_card;

import com.crm.sofia.dto.info_card.InfoCardDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.info_card.InfoCardMapper;
import com.crm.sofia.model.info_card.InfoCard;
import com.crm.sofia.repository.info_card.InfoCardRepository;
import com.crm.sofia.services.auth.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class InfoCardDesignerServiceTest {
    @Mock
    private InfoCardRepository infoCardRepository;

    @InjectMocks
    private InfoCardDesignerService infoCardDesignerService;

    @Mock
    private InfoCardJavascriptService infoCardJavascriptService;

    @Mock
    private JWTService jwtService;

    private List<InfoCard> infoCardList;

    private InfoCard infoCard;

    private InfoCardDTO infoCardDTO;

    @Mock
    private InfoCardMapper infoCardMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        infoCardList = new ArrayList<>();
        infoCard = new InfoCard().setTitle("dummyTitle").setQuery("dummyQuery");
        infoCardList.add(infoCard);
        infoCardDTO = new InfoCardDTO().setTitle("dummyTitleDTO").setQuery(Base64.getEncoder().encodeToString("dummyQueryDTO".getBytes(StandardCharsets.UTF_8)));
        infoCardDTO.setId("1");

    }

    @Test
    public void getObjectTest() {
        given(infoCardRepository.getObject()).willReturn(Collections.singletonList(infoCardDTO));
        List<InfoCardDTO> list = infoCardDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo("dummyTitleDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(infoCardRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(infoCard));
        given(infoCardMapper.map(ArgumentMatchers.any(InfoCard.class))).willReturn(infoCardDTO);
        InfoCardDTO dto = infoCardDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(infoCardRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            infoCardDesignerService.getObject("1");
        });

        String expectedMessage = "InfoCard does not exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(infoCardRepository.save(ArgumentMatchers.any(InfoCard.class))).willReturn(infoCard);
        given(infoCardMapper.map(ArgumentMatchers.any(InfoCardDTO.class))).willReturn(infoCard);
        given(infoCardMapper.map(ArgumentMatchers.any(InfoCard.class))).willReturn(infoCardDTO);
        InfoCardDTO dto = infoCardDesignerService.postObject(infoCardDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(infoCardRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            infoCardDesignerService.deleteObject("1");
        });
        String expectedMessage = "InfoCard does not exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
