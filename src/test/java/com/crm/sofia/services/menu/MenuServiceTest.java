package com.crm.sofia.services.menu;

import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.dto.language.LanguageIdDTO;
import com.crm.sofia.dto.menu.MenuDTO;
import com.crm.sofia.dto.menu.MenuTranslationDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.menu.MenuMapper;
import com.crm.sofia.model.language.Language;
import com.crm.sofia.model.menu.Menu;
import com.crm.sofia.model.menu.MenuTranslation;
import com.crm.sofia.repository.menu.MenuRepository;
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
//@SpringBootTest(classes = {MenuMapper.class})
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Mock
    private JWTService jwtService;

    private List<Menu> menuList;

    private Menu menu;


    private List<MenuTranslation> menuTranslationList;

    private MenuTranslation menuTranslation;

    private MenuTranslationDTO menuTranslationDTO;

    private List<MenuTranslationDTO> menuTranslationDTOList;

    private Language language;

    private LanguageIdDTO languageIdDTO;

    private LanguageDTO languageDTO;

    private MenuDTO menuDTO;

    @Mock
    private MenuMapper menuMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        menuList = new ArrayList<>();
        menuTranslationList = new ArrayList<>();
        language = new Language().setName("english");
        menuTranslation = new MenuTranslation().setLanguage(language);
        menuTranslation.getLanguage().setId("1");
        menuTranslationList.add(menuTranslation);
        menu = new Menu().setName("dummyName").setTranslations(menuTranslationList);
        menu.setId("1");
        menuList.add(menu);
        menuDTO = new MenuDTO().setName("dummyName").setMenuFieldList(Collections.emptyList());
        menuDTO.setId("1");
    }

    @Test
    public void getObjectTest() {
        given(menuRepository.findAll()).willReturn(menuList);
        given(menuMapper.map(ArgumentMatchers.any(List.class))).willReturn(List.of(menuDTO));
        List<MenuDTO> list = menuService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyName");
    }

    @Test
    public void getObjectByIdTest() {
        given(menuRepository.findTreeById(ArgumentMatchers.anyString())).willReturn(Optional.of(menu));
        given(menuMapper.mapMenu(ArgumentMatchers.any(Menu.class), ArgumentMatchers.anyString())).willReturn(menuDTO);
        MenuDTO dto = menuService.getObject("1", "1");
        assertThat(menuDTO).isNotNull();
        assertThat(menuDTO.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(menuRepository.findTreeById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            menuService.getObject("1", "1");
        });

        String expectedMessage = "Menu Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(menuMapper.mapDTO(ArgumentMatchers.any(MenuDTO.class))).willReturn(menu); // 76
        given(menuRepository.save(ArgumentMatchers.any(Menu.class))).willReturn(menu);  // 82
        given(menuMapper.map(ArgumentMatchers.any(Menu.class))).willReturn(menuDTO); //83
        MenuDTO dto = menuService.postObject(menuDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(menu.getId());
    }

    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(menuRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            menuService.deleteObject("1");
        });
        String expectedMessage = "Menu Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
