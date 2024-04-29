package com.katerynamykh.taskprofitsoft.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katerynamykh.taskprofitsoft.backend.config.BasePostgreContainerConfig;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainWithLocationsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.CreateChainRequestDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestorantChainControllerTest extends BasePostgreContainerConfig {
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
                    new ClassPathResource("/database/add-chains.sql"));
        }
    }

    @AfterAll
    static void tearDown(@Autowired DataSource data) throws SQLException {
        try (Connection connection = data.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("/database/delete-all.sql"));
        }
    }

    @Test
    void retrieveAll_WithoutParams_ShouldReturn5Chains() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/restorant-chains")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ChainWithLocationsDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), ChainWithLocationsDto[].class);
        assertEquals(5, actual.length);
    }

    @Test
    @Sql(scripts = "classpath:database/delete-chain-with-id=6.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void create_AddNewChain_ShouldReturnChainWithId6() throws Exception {
        CreateChainRequestDto newChain = new CreateChainRequestDto("Candies", "european",
                BigDecimal.ZERO);
        String jsonRequest = objectMapper.writeValueAsString(newChain);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restorant-chains").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        ChainResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ChainResponseDto.class);
        assertNotNull(actual);
        assertEquals(6, actual.id());
    }

    @Test
    void create_AddNotUniqueChain_ShouldReturnBadRequest() throws Exception {
        CreateChainRequestDto newChain = new CreateChainRequestDto("tasty bites", "european",
                BigDecimal.ZERO);
        String jsonRequest = objectMapper.writeValueAsString(newChain);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/restorant-chains").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertEquals("Faild to save duplicate chain tasty bites",
                result.getResolvedException().getMessage());
    }

    @Test
    @Sql(scripts = "classpath:database/reset-upadated-chain.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void updateById_ValidData_Ok() throws Exception {
        CreateChainRequestDto updateChain = new CreateChainRequestDto("Candies", "european",
                BigDecimal.ZERO);
        String jsonRequest = objectMapper.writeValueAsString(updateChain);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/restorant-chains/{id}", 1)
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ChainResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ChainResponseDto.class);
        assertNotNull(actual);
        assertEquals(1, actual.id());
        assertEquals("Candies", actual.name());
        assertEquals("european", actual.cuisine());
        assertEquals(BigDecimal.ZERO, actual.annualRevenue());
    }

    @Test
    void updateById_DuplicatedChainName_ShouldReturnBadRequest() throws Exception {
        CreateChainRequestDto updateChain = new CreateChainRequestDto("BARVY", "ukrainian",
                BigDecimal.ZERO);
        String jsonRequest = objectMapper.writeValueAsString(updateChain);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/restorant-chains/{id}", 1)
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertEquals("Faild to save duplicate chain BARVY",
                result.getResolvedException().getMessage());
    }

    @Test
    @Sql(scripts = "classpath:database/add-chain-to-delete.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/restorant-chains/{id}", 6)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
