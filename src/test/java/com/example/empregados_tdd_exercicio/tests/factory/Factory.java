package com.example.empregados_tdd_exercicio.tests.factory;

import com.example.empregados_tdd_exercicio.dto.EmployeeDTO;
import com.example.empregados_tdd_exercicio.entities.Department;
import com.example.empregados_tdd_exercicio.entities.Employee;

public class Factory {
    public static Employee createEmployee() {
        Employee employee = new Employee(15L, "Mariana", "mariana@gmail", createDepartment());
        return employee;
    }

    public static EmployeeDTO createEmployeeDTO() {
        Employee employee = createEmployee();
        return new EmployeeDTO(employee);
    }

    public static Department createDepartment() {
        return new Department(1L, "Financial");
    }
}
