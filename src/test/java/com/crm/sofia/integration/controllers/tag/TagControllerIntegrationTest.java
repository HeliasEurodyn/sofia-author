package com.crm.sofia.integration.controllers.tag;

import com.crm.sofia.SofiaApplication;
import com.crm.sofia.controllers.tag.TagDesignerController;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.handler.RestResponseEntityExceptionHandler;
import com.crm.sofia.mapper.tag.TagMapper;
import com.crm.sofia.model.tag.Tag;
import com.crm.sofia.repository.tag.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'test'}", loadContext = true)
@TestPropertySource("/application-test.properties")
@SpringBootTest(classes = {SofiaApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Transactional

public class TagControllerIntegrationTest {

    private static TagDTO tagDTO;
    @Autowired
    private JdbcTemplate jdbc;
    @Value("${sql.script.create.businessUnit}")
    private String sqlCreateBusinessUnit;
    @Value("${sql.script.create.businessUnit2}")
    private String sqlCreateBusinessUnit2;
    @Value("${sql.script.delete.businessUnit}")
    private String sqlDeleteBusinessUnit;
    @Autowired
    private TagDesignerController tagDesignerController;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        tagDTO = new TagDTO()
                .setTitle("TestTitle3")
                .setDescription("testDescription3");
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagDesignerController).setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
        jdbc.execute(sqlCreateBusinessUnit);
        jdbc.execute(sqlCreateBusinessUnit2);
    }

    @AfterEach
    public void AfterEach() {
        jdbc.execute(sqlDeleteBusinessUnit);
    }

    @Test
    @DisplayName("Get BusinessUnit List Http Request ")
    @Order(1)
    public void getBusinessUnitListHttpRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/business-unit-designer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Create Calendar Http Request ")
    @Order(2)
    public void setSqlCreateBusinessUnitHttpRequest() throws Exception {

        mockMvc.perform(post("/business-unit-designer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDTO)))
                .andExpect(status().isOk());

        List<Tag> tagList = tagRepository.findAll();

        assertEquals(3, tagList.size());
        assertTrue(tagList.stream().anyMatch(businessUnit -> businessUnit.getTitle().equals("TestTitle3")));
        assertTrue(tagList.stream().anyMatch(businessUnit -> businessUnit.getDescription().equals("testDescription3")));

    }

    @Test
    @DisplayName("Update BusinessUnit Http Request ")
    @Order(3)
    public void updateBusinessUnitHttpRequest() throws Exception {

        Tag tag1 = tagRepository.findById("1").get();
        tag1.setTitle("newTestTitle");
        tag1.setDescription("newTestDescription");
        TagDTO tagDTO1 = tagMapper.map(tag1);

        mockMvc.perform(put("/business-unit-designer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDTO1)))
                .andExpect(status().isOk());

        List<Tag> tagList = tagRepository.findAll();

        assertEquals(2, tagList.size());
        assertTrue(tagList.stream().anyMatch(businessUnit -> businessUnit.getTitle().equals("newTestTitle")));
        assertTrue(tagList.stream().noneMatch(businessUnit -> businessUnit.getTitle().equals("testTitle")));
        assertTrue(tagList.stream().anyMatch(businessUnit -> businessUnit.getDescription().equals("newTestDescription")));
        assertTrue(tagList.stream().noneMatch(businessUnit -> businessUnit.getDescription().equals("testDescription")));

        Optional<Tag> businessUnit2 = tagRepository.findById("1");
        assertTrue(businessUnit2.isPresent());


    }

    @Test
    @DisplayName("Get BusinessUnit Http Request ")
    @Order(4)
    public void getBusinessUnitHttpRequest() throws Exception {

        Optional<Tag> businessUnit1 = tagRepository.findById("1");
        assertTrue(businessUnit1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/business-unit-designer/by-id?id={id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.title", is("testTitle")));

    }

    @Test
    @DisplayName("Get BusinessUnit Http Request With Empty Response")
    @Order(5)
    public void getBusinessUnitHttpRequestEmptyResponse() throws Exception {

        Optional<Tag> businessUnit1 = tagRepository.findById("0");
        assertFalse(businessUnit1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/business-unit-designer/by-id?id={id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", is("002-1")))
                .andExpect(jsonPath("$.category", is("OBJECT_NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("BusinessUnit Does Not Exist")));

    }

    @Test
    @DisplayName("BusinessUnit Calendar Http Request ")
    @Order(6)
    public void deleteBusinessUnitHttpRequest() throws Exception {

        Optional<Tag> businessUnit1 = tagRepository.findById("1");
        assertTrue(businessUnit1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/business-unit-designer?id={id}", 1))
                .andExpect(status().isOk());

        businessUnit1 = tagRepository.findById("1");
        assertFalse(businessUnit1.isPresent());

    }

    @Test
    @DisplayName("Delete BusinessUnit Http Request With Empty Response")
    @Order(7)
    public void deleteBusinessUnitHttpRequestEmptyResponse() throws Exception {

        Optional<Tag> businessUnit1 = tagRepository.findById("0");
        assertFalse(businessUnit1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/business-unit-designer?id={id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", is("002-1")))
                .andExpect(jsonPath("$.category", is("OBJECT_NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("BusinessUnit Does Not Exist")));

    }
}
