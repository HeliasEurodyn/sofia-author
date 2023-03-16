package com.crm.sofia.integration.services.tag;

import com.crm.sofia.SofiaApplication;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.common.SofiaException;
import com.crm.sofia.mapper.tag.TagMapper;
import com.crm.sofia.model.tag.Tag;
import com.crm.sofia.repository.tag.TagRepository;
import com.crm.sofia.services.tag.TagDesignerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'test'}", loadContext = true)
@TestPropertySource("/application-test.properties")
@SpringBootTest(classes = {SofiaApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class TagServiceIntegrationTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.businessUnit}")
    private String sqlCreateBusiness;

    @Value("${sql.script.create.businessUnit2}")
    private String sqlCreateBusiness2;

    @Value("${sql.script.delete.businessUnit}")
    private String sqlDeleteBusiness;

    @Autowired
    private TagDesignerService tagDesignerService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;


    @BeforeEach
    void beforeEach() {
        jdbc.execute(sqlCreateBusiness);
        jdbc.execute(sqlCreateBusiness2);
    }

    @AfterEach
    public void AfterEach() {
        jdbc.execute(sqlDeleteBusiness);
    }

    @Test
    @DisplayName("Get BusinessUnit List")
    @Order(1)
    public void getObjectTest() {
        List<TagDTO> list = tagDesignerService.getObject();
        assertFalse(list.isEmpty(), "List should not empty");
        assertEquals(2, list.size());
        assertEquals("testTitle", list.get(0).getTitle());
        assertDoesNotThrow(() -> tagDesignerService.getObject());

    }

    @Test
    @DisplayName("Get BusinessUnit")
    @Order(2)
    public void getObjectByIdTest() {
        Optional<Tag> businessUnit = tagRepository.findById("1");
        Optional<Tag> businessUnit2 = tagRepository.findById("2");
        Optional<Tag> businessUnit3 = tagRepository.findById("3");

        assertTrue(businessUnit.isPresent());
        assertTrue(businessUnit2.isPresent());
        assertFalse(businessUnit3.isPresent());

        TagDTO tagDTO1 = tagMapper.map(businessUnit.get());
        TagDTO tagDTO2 = tagMapper.map(businessUnit.get());

        TagDTO tagDTO11 = tagDesignerService.getObject("1");
        TagDTO tagDTO22 = tagDesignerService.getObject("2");


        assertNotNull(tagDTO1);
        assertNotNull(tagDTO2);
        assertNotNull(tagDTO11);
        assertNotNull(tagDTO22);


        assertEquals(tagDTO1.getTitle(), tagDTO11.getTitle());
        assertEquals("testTitle", tagDTO1.getTitle());
        assertEquals("testTitle", tagDTO11.getTitle());

        assertDoesNotThrow(() -> tagDesignerService.getObject("1"));
        assertDoesNotThrow(() -> tagDesignerService.getObject("2"));
        Exception exception = assertThrows(SofiaException.class, () -> {
            tagDesignerService.getObject("3");
        });

        String expectedMessage = "BusinessUnit Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Delete BusinessUnit")
    @Order(3)
    public void deleteObjectByIdTest() {
        List<Tag> tagList = tagRepository.findAll();
        assertFalse(tagList.isEmpty());
        assertEquals(2, tagList.size());

        tagDesignerService.deleteObject("1");
        Optional<Tag> businessUnit = tagRepository.findById("1");
        assertFalse(businessUnit.isPresent());

        tagList = tagRepository.findAll();
        assertEquals(1, tagList.size());

        tagDesignerService.deleteObject("2");
        Optional<Tag> businessUnit2 = tagRepository.findById("2");
        assertFalse(businessUnit2.isPresent());

        tagList = tagRepository.findAll();
        assertEquals(0, tagList.size());
        assertTrue(businessUnit.isEmpty());

        Exception exception = assertThrows(SofiaException.class, () -> {
            tagDesignerService.getObject("1");
        });

        String expectedMessage = "BusinessUnit Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);

        Exception exception2 = assertThrows(SofiaException.class, () -> {
            tagDesignerService.deleteObject("1");
        });

        actualMessage = exception2.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }


    @Test
    @DisplayName("Post-Put BusinessUnit")
    @Order(4)
    public void postObjectTest() {

        TagDTO tagDTO1 = new TagDTO()
                .setTitle("testTitle3")
                .setDescription("testDescription3");

        TagDTO tagDTO2 = tagDesignerService.getObject("1");
        tagDTO2.setTitle("newTitle");


        List<Tag> tagList = tagRepository.findAll();
        assertFalse(tagList.isEmpty());
        assertEquals(2, tagList.size());

        TagDTO tagDTO3 = tagDesignerService.postObject(tagDTO1);
        tagList = tagRepository.findAll();
        assertEquals(3, tagList.size());
        assertEquals("testTitle3", tagDTO3.getTitle());

        TagDTO tagDTO4 = tagDesignerService.postObject(tagDTO2);
        tagList = tagRepository.findAll();
        assertEquals(3, tagList.size());
        assertEquals("newTitle", tagDTO4.getTitle());


    }
}
