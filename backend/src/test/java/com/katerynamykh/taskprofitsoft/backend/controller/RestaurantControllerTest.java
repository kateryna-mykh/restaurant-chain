package com.katerynamykh.taskprofitsoft.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.CreatedRestaurantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.DetaildRestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantResponseDto;
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
class RestaurantControllerTest {
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
                    new ClassPathResource("/database/add-chains-and-restaurants.sql"));
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
    @Sql(scripts = "classpath:database/delete-created-restaurant.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void create_AddNewRestaurant_ShouldReturnRestaurant() throws Exception {
        CreatedRestaurantRequestDto newRestaurant = new CreatedRestaurantRequestDto(
                "161 Myrtle Street", "Paolo Rycraft", 15, 5, 5L,
                List.of("Apple Pie", "Cherry Cobbler", "Cherry Cheesecake"));
        String jsonRequest = objectMapper.writeValueAsString(newRestaurant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restaurants").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        RestaurantResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), RestaurantResponseDto.class);
        assertNotNull(actual);
        assertEquals("Cherry Cheesecake", actual.menuItems().get(2));
    }

    @Test
    void create_AddRestaurantNotQniqueAdress_ShouldReturnBadRequest() throws Exception {
        CreatedRestaurantRequestDto newRestaurant = new CreatedRestaurantRequestDto(
                "06 Crest Line Way", "Paolo Rycraft", 15, 5, 1L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(newRestaurant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restaurants").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertEquals("Faild to save duplicate restaurant 06 Crest Line Way",
                result.getResolvedException().getMessage());
    }

    @Test
    void create_AddRestaurantWithNotExistingChain_ShouldReturnNotFound() throws Exception {
        CreatedRestaurantRequestDto newRestaurant = new CreatedRestaurantRequestDto(
                "161 Myrtle Street", "Paolo Rycraft", 15, 5, 100L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(newRestaurant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restaurants").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        assertEquals("Can't find chain by id: 100", result.getResolvedException().getMessage());
    }

    @Test
    void retrieveById_IdExist_ShouldReturnDetaildDtoAndStatusOk() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/restaurants/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        DetaildRestaurantResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), DetaildRestaurantResponseDto.class);
        assertNotNull(actual);
        assertEquals(2, actual.id());
        assertEquals("07196 Blaine Court", actual.locationAddress());
        assertEquals("asian", actual.chainShortInfo().cuisine());
        assertEquals("Asian Fusion", actual.chainShortInfo().name());
    }

    @Test
    void retrieveById_IdNotExist_ShouldReturnNotFound() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/restaurants/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        assertEquals("Can't find restaurant by id 100", result.getResolvedException().getMessage());
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:database/reset-updated-restaurant.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void updateById_ValidRestaurant_Ok() throws Exception {
        CreatedRestaurantRequestDto updateRestaurant = new CreatedRestaurantRequestDto(
                "06 Crest Line Way", "Paolo Rycraft", 100, 20, 3L, List.of(""));
        String jsonRequest = objectMapper.writeValueAsString(updateRestaurant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/restaurants/{id}", 3).content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        RestaurantResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), RestaurantResponseDto.class);
        assertNotNull(actual);
        assertEquals(3, actual.id());
        assertEquals(20, actual.employeesNumber());
        assertEquals(100, actual.seetsCapacity());
        assertEquals("Paolo Rycraft", actual.manager());
        assertEquals("06 Crest Line Way", actual.locationAddress());
    }

    @Test
    void updateById_ChainNotExist_ShouldReturnNotFound() throws Exception {
        CreatedRestaurantRequestDto updateRestaurant = new CreatedRestaurantRequestDto(
                "161 Myrtle Street", "Paolo Rycraft", 20, 4, 100L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(updateRestaurant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/restaurants/{id}", 2).content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        assertEquals("Can't find chain by id: 100", result.getResolvedException().getMessage());
    }

    @Test
    void updateById_DuplicateLocation_ShouldReturnBadRequest() throws Exception {
        CreatedRestaurantRequestDto updateRestaurant = new CreatedRestaurantRequestDto(
                "06 Crest Line Way", "Paolo Rycraft", 20, 4, 5L, List.of());
        String jsonRequest = objectMapper.writeValueAsString(updateRestaurant);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/restaurants/{id}", 5).content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        assertEquals("Faild to save duplicate restaurant 06 Crest Line Way",
                result.getResolvedException().getMessage());

    }

    @Test
    @Sql(scripts = "classpath:database/add-restaurant-to-delete.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/restaurants/{id}", 6)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testUploadResorants() {
        // TODO:: later api/restaurants/upload
    }

    @Test
    void testSearch() {
        // TODO:: later api/restaurants/_list
    }

    @Test
    void testGenerateReport() {
        // TODO:: later api/restaurants/_report
    }
}
