package com.example.empregados_tdd_exercicio.repositories;

import com.example.empregados_tdd_exercicio.entities.Department;
import com.example.empregados_tdd_exercicio.entities.Employee;
import com.example.empregados_tdd_exercicio.tests.factory.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {
    private long existingId;
    private long notExistingId;
    private long countTotalEmployees;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        notExistingId = 1000L;
        countTotalEmployees = 14L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Department department = departmentRepository.save(Factory.createDepartment());
        Employee employee = Factory.createEmployee();
        employee.setId(null);
        employee.setDepartment(department);
        employee = employeeRepository.save(employee);

        Assertions.assertNotNull(employee.getId());
        Assertions.assertEquals(countTotalEmployees + 1, employee.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        employeeRepository.deleteById(existingId);
        Optional<Employee> result = employeeRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void getShouldReturnNotEmptyOptionalProductWhenIdExists() {

        Optional<Employee> result = employeeRepository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void getShouldReturnEmptyOptionalProductWhenIdDoesNotExist() {
        Optional<Employee> result = employeeRepository.findById(notExistingId);
        Assertions.assertTrue(result.isEmpty());
    }
}
