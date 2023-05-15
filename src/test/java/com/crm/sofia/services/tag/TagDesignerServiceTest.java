package com.crm.sofia.services.tag;

import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.tag.TagMapper;
import com.crm.sofia.model.tag.Tag;
import com.crm.sofia.repository.tag.TagRepository;
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
public class TagDesignerServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagDesignerService tagDesignerService;

    @Mock
    private JWTService jwtService;

    private List<Tag> tagList;

    private List<TagDTO> tagDTOList;

    private Tag tag;

    private TagDTO tagDTO;

    @Mock
    private TagMapper tagMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        tagList = new ArrayList<>();
        tagDTOList = new ArrayList<>();
        tag = new Tag().setTitle("dummyTitle").setDescription("dummyDescription");
        tagList.add(tag);
        tagDTO = new TagDTO().setTitle("dummyTitleDTO").setDescription("dummyDescriptionDTO");
        tagDTO.setId("1");
        tagDTOList.add(tagDTO);
    }

    @Test
    public void getObjectTest() {
        given(tagRepository.getObject()).willReturn(Collections.singletonList(tagDTO));
        List<TagDTO> list = tagDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo("dummyTitleDTO");
    }


    @Test
    public void getObjectByIdTest() {
        given(tagRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(tag));
        given(tagMapper.map(ArgumentMatchers.any(Tag.class))).willReturn(tagDTO);
        TagDTO dto = tagDesignerService.getObject("1");
        assertThat(tagDTO).isNotNull();
        assertThat(tagDTO.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(tagRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            tagDesignerService.getObject("1");
        });

        String expectedMessage = "Tag Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(tagMapper.map(ArgumentMatchers.any(TagDTO.class))).willReturn(tag);
        given(tagRepository.save(ArgumentMatchers.any(Tag.class))).willReturn(tag);
        tagDesignerService.postObject(tagDTO);
        assertThat(tagDTO).isNotNull();
        assertThat(tagDTO.getId()).isEqualTo("1");
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(tagRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            tagDesignerService.deleteObject("1");
        });
        String expectedMessage = "Tag Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

}
