package com.example.empregados_tdd_exercicio.controllers;

import com.example.empregados_tdd_exercicio.dto.EmployeeDTO;
import com.example.empregados_tdd_exercicio.entities.Employee;
import com.example.empregados_tdd_exercicio.services.EmployeeService;
import com.example.empregados_tdd_exercicio.services.exceptions.DatabaseException;
import com.example.empregados_tdd_exercicio.services.exceptions.ResourceNotFoundException;
import com.example.empregados_tdd_exercicio.tests.factory.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("removal")
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<EmployeeDTO> page;
    private EmployeeDTO employeeDTO;
    private long existingId;
    private long notExistingId;
    private long dependentId;
    private Employee employee;

    @BeforeEach
    void setUp() throws Exception {
        employeeDTO = Factory.createEmployeeDTO();
        page = new PageImpl<>(List.of(employeeDTO));
        existingId = 1L;
        notExistingId = 1000L;
        dependentId = 2L;
        employee = Factory.createEmployee();

        Mockito.when(employeeService.findAllByPage(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(employeeService.findById(existingId)).thenReturn(employeeDTO);
        Mockito.when(employeeService.findById(notExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(employeeService).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(employeeService).delete(notExistingId);
        Mockito.doThrow(DatabaseException.class).when(employeeService).delete(dependentId);

        Mockito.when(employeeService.update(Mockito.eq(existingId), Mockito.any())).thenReturn(employeeDTO);
        Mockito.when(employeeService.update(Mockito.eq(notExistingId), Mockito.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.when(employeeService.insert(Mockito.any())).thenReturn(employeeDTO);
    }

    @Test
    public void findAllByPageShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/employees").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnEmployeeWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", notExistingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", existingId));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", notExistingId));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnEmployeeDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(employeeDTO);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    public void insertShouldReturnEmployeeDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(employeeDTO);
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
