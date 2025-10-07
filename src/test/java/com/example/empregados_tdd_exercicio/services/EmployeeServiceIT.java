package com.example.empregados_tdd_exercicio.services;

import com.example.empregados_tdd_exercicio.dto.EmployeeDTO;
import com.example.empregados_tdd_exercicio.repositories.EmployeeRepository;
import com.example.empregados_tdd_exercicio.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class EmployeeServiceIT {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

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
    public void deleteShouldDeleteResourceWhenIdExists() {
        employeeService.delete(existingId);

        Assertions.assertEquals(countTotalEmployees - 1, employeeRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.delete(notExistingId);
        });
    }

    @Test
    public void findAllByPageShouldReturnPageWhenPage0Size10() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<EmployeeDTO> result = employeeService.findAllByPage("", pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalEmployees, result.getTotalElements());
    }

    @Test
    public void findAllByPageShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageRequest = PageRequest.of(50, 10);
        Page<EmployeeDTO> result = employeeService.findAllByPage("", pageRequest);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void findAllByPageShouldReturnOrderedPageWhenSortedByName() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        Page<EmployeeDTO> result = employeeService.findAllByPage("", pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Alex", result.getContent().get(0).getName());
        Assertions.assertEquals("Ana", result.getContent().get(1).getName());
        Assertions.assertEquals("Andressa", result.getContent().get(2).getName());
    }
}
