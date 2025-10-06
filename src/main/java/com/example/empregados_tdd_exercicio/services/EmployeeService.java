package com.example.empregados_tdd_exercicio.services;

import com.example.empregados_tdd_exercicio.dto.DepartmentDTO;
import com.example.empregados_tdd_exercicio.dto.EmployeeDTO;
import com.example.empregados_tdd_exercicio.entities.Department;
import com.example.empregados_tdd_exercicio.entities.Employee;
import com.example.empregados_tdd_exercicio.repositories.DepartmentRepository;
import com.example.empregados_tdd_exercicio.repositories.EmployeeRepository;
import com.example.empregados_tdd_exercicio.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAllByPage(String name, Pageable pageable) {
        Page<Employee> employees = employeeRepository.searchByName(name, pageable);
        return employees.map(EmployeeDTO::new);
    }

    @Transactional(readOnly = true)
    public EmployeeDTO findById(Long id) {
        Optional<Employee> result = employeeRepository.findById(id);
        Employee employee = result.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
        EmployeeDTO dto = new EmployeeDTO(employee);
        return dto;
    }

    @Transactional
    public EmployeeDTO insert(EmployeeDTO dto) {
        Employee entity = new Employee();
        copyDtoToEntity(dto, entity);
        entity = employeeRepository.save(entity);
        return new EmployeeDTO(entity);
    }

    private void copyDtoToEntity(EmployeeDTO dto, Employee entity) {
        Department department = departmentRepository.getReferenceById(dto.getDepartment().getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setDepartment(department);
    }
}
