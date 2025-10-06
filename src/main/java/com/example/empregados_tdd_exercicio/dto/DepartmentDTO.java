package com.example.empregados_tdd_exercicio.dto;

import com.example.empregados_tdd_exercicio.entities.Department;

import java.io.Serializable;

public class DepartmentDTO implements Serializable {
    private Long id;
    private String name;

    public DepartmentDTO() {}

    public DepartmentDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public DepartmentDTO(Department entity) {
        id = entity.getId();
        name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
