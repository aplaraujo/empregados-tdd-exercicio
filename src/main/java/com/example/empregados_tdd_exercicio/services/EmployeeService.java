package com.example.empregados_tdd_exercicio.services;

import com.example.empregados_tdd_exercicio.dto.EmployeeDTO;
import com.example.empregados_tdd_exercicio.entities.Employee;
import com.example.empregados_tdd_exercicio.repositories.DepartmentRepository;
import com.example.empregados_tdd_exercicio.repositories.EmployeeRepository;
import com.example.empregados_tdd_exercicio.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
