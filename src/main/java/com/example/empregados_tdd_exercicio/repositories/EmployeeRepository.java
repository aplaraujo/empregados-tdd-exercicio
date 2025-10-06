package com.example.empregados_tdd_exercicio.repositories;

import com.example.empregados_tdd_exercicio.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
