package com.example.empregados_tdd_exercicio.services;

import com.example.empregados_tdd_exercicio.dto.EmployeeDTO;
import com.example.empregados_tdd_exercicio.entities.Department;
import com.example.empregados_tdd_exercicio.entities.Employee;
import com.example.empregados_tdd_exercicio.repositories.DepartmentRepository;
import com.example.empregados_tdd_exercicio.repositories.EmployeeRepository;
import com.example.empregados_tdd_exercicio.services.exceptions.DatabaseException;
import com.example.empregados_tdd_exercicio.services.exceptions.ResourceNotFoundException;
import com.example.empregados_tdd_exercicio.tests.factory.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTests {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    private long existingId;
    private long notExistingId;
    private long dependentId;
    private PageImpl<Employee> page;
    private Employee employee;
    private Department department;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        notExistingId = 1000L;
        dependentId = 3L;
        employee = Factory.createEmployee();
        page = new PageImpl<>(List.of(employee));
        department = Factory.createDepartment();
        employeeDTO = Factory.createEmployeeDTO();

        Mockito.when(employeeRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(employeeRepository.existsById(notExistingId)).thenReturn(false);
        Mockito.when(employeeRepository.existsById(dependentId)).thenReturn(true);

        Mockito.doThrow(DataIntegrityViolationException.class).when(employeeRepository).deleteById(dependentId);
        Mockito.when(employeeRepository.searchByName(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(page);
        Mockito.when(employeeRepository.save(ArgumentMatchers.any())).thenReturn(employee);
        Mockito.when(employeeRepository.findById(existingId)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.findById(notExistingId)).thenReturn(Optional.empty());

        Mockito.when(employeeRepository.getReferenceById(existingId)).thenReturn(employee);
        Mockito.when(employeeRepository.getReferenceById(notExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(departmentRepository.getReferenceById(existingId)).thenReturn(department);
        Mockito.when(departmentRepository.getReferenceById(notExistingId)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void updateShouldReturnEmployeeDTOWhenIdExists() {
        EmployeeDTO result = employeeService.update(existingId, employeeDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.update(notExistingId, employeeDTO);
        });
    }

    @Test
    public void findByIdShouldReturnEmployeeDTOWhenIdExists() {
        EmployeeDTO result = employeeService.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.findById(notExistingId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            employeeService.delete(existingId);
        });
        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.delete(notExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentIdExists() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            employeeService.delete(dependentId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EmployeeDTO> result = employeeService.findAllByPage(employee.getName(), pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(employeeRepository).searchByName(Mockito.anyString(), Mockito.any(Pageable.class));
    }
}
