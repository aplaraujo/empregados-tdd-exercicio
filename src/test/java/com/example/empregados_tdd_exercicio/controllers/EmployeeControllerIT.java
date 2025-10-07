package com.example.empregados_tdd_exercicio.controllers;

import com.example.empregados_tdd_exercicio.dto.EmployeeDTO;
import com.example.empregados_tdd_exercicio.tests.factory.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long notExistingId;
    private long countTotalEmployees;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        notExistingId = 1000L;
        countTotalEmployees = 14L;
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/employees?page=0&size=12&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(countTotalEmployees))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].name").value("Alex"))
                .andExpect(jsonPath("$.content[1].name").value("Ana"))
                .andExpect(jsonPath("$.content[2].name").value("Andressa"));
    }

    @Test
    public void insertShouldCreateResource() throws Exception {
        EmployeeDTO dto = Factory.createEmployeeDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }
}
