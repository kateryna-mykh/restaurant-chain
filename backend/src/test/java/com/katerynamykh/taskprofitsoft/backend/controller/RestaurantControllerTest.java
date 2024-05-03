package com.katerynamykh.taskprofitsoft.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.CreatedRestaurantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.DetaildRestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.FilteredRestaurantsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantShortResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.SearchRestaurantDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.UploadResultDto;
import java.io.File;
import java.nio.file.Files;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
    @Sql(scripts = "classpath:database/delete-upload-restaurants.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void uploadResorants_TestFile_Ok() throws Exception {
        File file = new File("src/test/resources/database/test-data-set-10-entities.json");
        MockMultipartFile jsonData = new MockMultipartFile("file", "test-data-set-10-entities.json",
                MediaType.APPLICATION_JSON_VALUE, Files.readAllBytes(file.toPath()));

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.multipart("/api/restaurants/upload").file(jsonData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        UploadResultDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                UploadResultDto.class);
        assertEquals(7, actual.uploaded());
        assertEquals(3, actual.skipped());
        assertEquals(10, actual.all());
    }

    @Test
    void search_EmptyRequest_Ok() throws Exception {
        SearchRestaurantDto emptySearch = new SearchRestaurantDto(null, null, null, null);
        String jsonRequest = objectMapper.writeValueAsString(emptySearch);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restaurants/_list").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        FilteredRestaurantsDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), FilteredRestaurantsDto.class);
        assertNotNull(actual);
        assertEquals(5, actual.restaurants().size());
        assertEquals(1, actual.totalPages());

    }

    @Test
    void search_SetAddress_Ok() throws Exception {
        SearchRestaurantDto addressSearch = new SearchRestaurantDto("7", null, null, null);
        String jsonRequest = objectMapper.writeValueAsString(addressSearch);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restaurants/_list").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        FilteredRestaurantsDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), FilteredRestaurantsDto.class);
        assertNotNull(actual);
        assertEquals(2, actual.restaurants().size());
        assertEquals(1, actual.totalPages());
    }

    @Test
    void search_SetAddressAndChainId_Ok() throws Exception {
        SearchRestaurantDto addressAndChainSearch = new SearchRestaurantDto("7", 1L, null, null);
        String jsonRequest = objectMapper.writeValueAsString(addressAndChainSearch);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restaurants/_list").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        FilteredRestaurantsDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), FilteredRestaurantsDto.class);

        RestaurantShortResponseDto found = actual.restaurants().get(0);
        assertNotNull(actual);
        assertEquals(1, actual.restaurants().size());
        assertEquals(1, actual.totalPages());
        assertEquals("5767 Anderson Place", found.locationAddress());
        assertEquals(50, found.seetsCapacity());
        assertEquals(List.of("Burger", "Pizza", "Salad", "French Fries", "Soft Drink"),
                found.menuItems());
        assertEquals("Delicious Eats", found.chainName());
    }

    @Test
    void search_SetSize_Ok() throws Exception {
        SearchRestaurantDto sizeSearch = new SearchRestaurantDto(null, null, null, 2);
        String jsonRequest = objectMapper.writeValueAsString(sizeSearch);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restaurants/_list").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        FilteredRestaurantsDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), FilteredRestaurantsDto.class);
        assertNotNull(actual);
        assertEquals(2, actual.restaurants().size());
        assertEquals(3, actual.totalPages());
    }

    @Test
    void search_SetPageAndSize_Ok() throws Exception {
        SearchRestaurantDto pageAndSizeSearch = new SearchRestaurantDto(null, null, 1, 3);
        String jsonRequest = objectMapper.writeValueAsString(pageAndSizeSearch);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restaurants/_list").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        FilteredRestaurantsDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), FilteredRestaurantsDto.class);
        assertNotNull(actual);
        assertEquals(2, actual.restaurants().size());
        assertEquals(2, actual.totalPages());
    }

    @Test
    void generateReport_EmptyRequest_Ok() throws Exception {
        SearchRestaurantDto emptySearch = new SearchRestaurantDto(null, null, null, null);
        String jsonRequest = objectMapper.writeValueAsString(emptySearch);
        String expectedData = new StringBuilder()
                .append("id,chainName,locationAddress,seetsCapacity,menuItems")
                .append(System.lineSeparator())
                .append("1,Delicious Eats,5767 Anderson Place,50,[Burger, Pizza, Salad, French Fries, Soft Drink]")
                .append(System.lineSeparator())
                .append("2,Asian Fusion,07196 Blaine Court,30,[Curry, Noodles, Spring Rolls, Jasmine Rice, Thai Iced Tea]")
                .append(System.lineSeparator()).append("3,Tasty Bites,06 Crest Line Way,100,null")
                .append(System.lineSeparator())
                .append("4,Barvy,3655 Drewry Crossing,20,[Zrazy, Uzvar, Cheesecakes]")
                .append(System.lineSeparator())
                .append("5,BEEF Meat & Wine,4 Ronald Regan Way,50,null").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/restaurants/_report").content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedData));
    }

    @Test
    void generateReport_SetAddressAndChainId_Ok() throws Exception {
        SearchRestaurantDto addressAndChainSearch = new SearchRestaurantDto("7", 2L, null, null);
        String jsonRequest = objectMapper.writeValueAsString(addressAndChainSearch);
        String expectedData = new StringBuilder()
                .append("id,chainName,locationAddress,seetsCapacity,menuItems")
                .append(System.lineSeparator())
                .append("2,Asian Fusion,07196 Blaine Court,30,[Curry, Noodles, Spring Rolls, Jasmine Rice, Thai Iced Tea]")
                .toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/restaurants/_report").content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedData));
    }
}
