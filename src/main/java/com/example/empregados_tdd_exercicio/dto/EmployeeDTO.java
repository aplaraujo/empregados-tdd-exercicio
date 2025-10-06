package com.example.empregados_tdd_exercicio.dto;
import com.example.empregados_tdd_exercicio.entities.Employee;

import java.io.Serial;
import java.io.Serializable;

public class EmployeeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String email;
    private Long departmentId;

    public EmployeeDTO() {}

    public EmployeeDTO(Long id, String name, String email, Long departmentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.departmentId = departmentId;
    }

    public EmployeeDTO(Employee entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.departmentId = entity.getDepartment().getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }
}
