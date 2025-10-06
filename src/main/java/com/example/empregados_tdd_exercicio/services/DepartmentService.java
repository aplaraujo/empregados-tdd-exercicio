package com.example.empregados_tdd_exercicio.services;

import com.example.empregados_tdd_exercicio.dto.DepartmentDTO;
import com.example.empregados_tdd_exercicio.entities.Department;
import com.example.empregados_tdd_exercicio.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public Page<DepartmentDTO> findAllByPage(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        return departments.map(dep -> new DepartmentDTO(dep));
    }
}
