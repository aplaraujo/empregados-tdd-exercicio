package com.example.empregados_tdd_exercicio.controllers;

import com.example.empregados_tdd_exercicio.dto.DepartmentDTO;
import com.example.empregados_tdd_exercicio.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<Page<DepartmentDTO>> findAll(Pageable pageable) {
        Page<DepartmentDTO> dto = departmentService.findAllByPage(pageable);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DepartmentDTO> findById(@PathVariable Long id) {
        DepartmentDTO dto = departmentService.findById(id);
        return ResponseEntity.ok(dto);
    }
}
