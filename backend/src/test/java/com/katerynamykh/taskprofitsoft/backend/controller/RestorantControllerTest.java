package com.katerynamykh.taskprofitsoft.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.CreatedRestorantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.DetaildRestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestorantResponseDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestorantControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void setUpBeforeClass(@Autowired DataSource data,
            @Autowired WebApplicationContext appContext) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(appContext).build();
        tearDown(data);
        try (Connection connection = data.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("/database/add-chains-and-restorants.sql"));
        }
    }

    @AfterAll
    static void tearDownAfterClass(@Autowired DataSource data) throws Exception {
        tearDown(data);
    }

    @SneakyThrows
    static void tearDown(DataSource data) {
        try (Connection connection = data.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("/database/delete-all.sql"));
        }
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:database/delete-created-restorant.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void create_AddNewRestorant_ShouldReturnRestorant() throws Exception {
        CreatedRestorantRequestDto newRestorant = new CreatedRestorantRequestDto(
                "161 Myrtle Street", "Paolo Rycraft", 15, 5, 5L,
                List.of("Apple Pie", "Cherry Cobbler", "Cherry Cheesecake"));
        String jsonRequest = objectMapper.writeValueAsString(newRestorant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restorants").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        RestorantResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), RestorantResponseDto.class);
        assertNotNull(actual);
        assertEquals("Cherry Cheesecake", actual.menuItems().get(2));
    }

    @Test
    void create_AddRestorantNotQniqueAdress_ShouldReturnBadRequest() throws Exception {
        CreatedRestorantRequestDto newRestorant = new CreatedRestorantRequestDto(
                "06 Crest Line Way", "Paolo Rycraft", 15, 5, 1L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(newRestorant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restorants").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertEquals("Faild to save duplicate restorant 06 Crest Line Way",
                result.getResolvedException().getMessage());
    }

    @Test
    void create_AddRestorantWithNotExistingChain_ShouldReturnNotFound() throws Exception {
        CreatedRestorantRequestDto newRestorant = new CreatedRestorantRequestDto(
                "161 Myrtle Street", "Paolo Rycraft", 15, 5, 100L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(newRestorant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restorants").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        assertEquals("Can't find chain by id: 100", result.getResolvedException().getMessage());
    }

    //TODO:: debug why db restricts to retrieve
    @Test
    void retrieveById_IdExist_ShouldReturnDetaildDtoAndStatusOk() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/restorants/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        DetaildRestorantResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), DetaildRestorantResponseDto.class);
        assertNotNull(actual);
        assertEquals(2, actual.id());
        assertEquals("07196 Blaine Court", actual.locationAddress());
        assertEquals("asian", actual.chainShortInfo().cuisine());
        assertEquals("Asian Fusion", actual.chainShortInfo().name());
    }

    @Test
    void retrieveById_IdNotExist_ShouldReturnNotFound() throws Exception {
        // TODO::
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/restorants/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        assertEquals("Can't find restorant by id 100", result.getResolvedException().getMessage());
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:database/add-restorant-to-delete.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-created-restorant.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void updateById_ValidRestorant_Ok() throws Exception {
        CreatedRestorantRequestDto updateRestorant = new CreatedRestorantRequestDto(
                "161 Myrtle Street", "Paolo Rycraft", 20, 4, 1L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(updateRestorant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/restorants/{id}", 6).content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        RestorantResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), RestorantResponseDto.class);
        assertNotNull(actual);
        assertEquals(6, actual.id());
        assertEquals(4, actual.employeesNumber());
        assertEquals(20, actual.seetsCapacity());
        assertEquals("Paolo Rycraft", actual.manager());
        assertEquals("161 Myrtle Street", actual.locationAddress());
    }

    @Test
    void updateById_ChainNotExist_ShouldReturnNotFound() throws Exception {
        CreatedRestorantRequestDto updateRestorant = new CreatedRestorantRequestDto(
                "161 Myrtle Street", "Paolo Rycraft", 20, 4, 100L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(updateRestorant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/restorants/{id}", 2).content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        assertEquals("Can't find chain by id: 100", result.getResolvedException().getMessage());
    }

    @Test
    void updateById_DuplicateLocation_ShouldReturnBadRequest() throws Exception {
        CreatedRestorantRequestDto updateRestorant = new CreatedRestorantRequestDto(
                "06 Crest Line Way", "Paolo Rycraft", 20, 4, 5L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(updateRestorant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/restorants/{id}", 5).content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        assertEquals("Faild to save duplicate restorant 06 Crest Line Way",
                result.getResolvedException().getMessage());

    }

    @Test
    @Sql(scripts = "classpath:database/add-restorant-to-delete.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/restorants/{id}", 6)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testUploadResorants() {
        // TODO:: later api/restorants/upload
    }

    @Test
    void testSearch() {
        // TODO:: later api/restorants/_list
    }

    @Test
    void testGenerateReport() {
        // TODO:: later api/restorants/_report
    }
}
