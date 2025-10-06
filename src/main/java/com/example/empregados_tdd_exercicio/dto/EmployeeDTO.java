package com.example.empregados_tdd_exercicio.dto;
import com.example.empregados_tdd_exercicio.entities.Department;
import com.example.empregados_tdd_exercicio.entities.Employee;

import java.io.Serial;
import java.io.Serializable;

public class EmployeeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String email;
    private DepartmentDTO department;

    public EmployeeDTO() {}

    public EmployeeDTO(Long id, String name, String email, DepartmentDTO department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public EmployeeDTO(Employee entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.department = new DepartmentDTO(entity.getDepartment());
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

    public DepartmentDTO getDepartment() {
        return department;
    }
}
