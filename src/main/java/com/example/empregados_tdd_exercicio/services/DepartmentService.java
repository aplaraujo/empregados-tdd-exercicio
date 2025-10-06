package com.example.empregados_tdd_exercicio.services;

import com.example.empregados_tdd_exercicio.dto.DepartmentDTO;
import com.example.empregados_tdd_exercicio.entities.Department;
import com.example.empregados_tdd_exercicio.repositories.DepartmentRepository;
import com.example.empregados_tdd_exercicio.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public Page<DepartmentDTO> findAllByPage(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        return departments.map(dep -> new DepartmentDTO(dep));
    }

    @Transactional(readOnly = true)
    public DepartmentDTO findById(Long id) {
        Optional<Department> result = departmentRepository.findById(id);
        Department department = result.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
        DepartmentDTO dto = new DepartmentDTO(department);
        return dto;
    }

    @Transactional
    public DepartmentDTO insert(DepartmentDTO dto) {
        Department entity = new Department();
        copyDtoToEntity(dto, entity);
        entity = departmentRepository.save(entity);
        return new DepartmentDTO(entity);
    }

    @Transactional
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        Department entity = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        copyDtoToEntity(dto, entity);
        entity = departmentRepository.save(entity);
        return new DepartmentDTO(entity);
    }

    private void copyDtoToEntity(DepartmentDTO dto, Department entity) {
        entity.setName(dto.getName());
    }
}
