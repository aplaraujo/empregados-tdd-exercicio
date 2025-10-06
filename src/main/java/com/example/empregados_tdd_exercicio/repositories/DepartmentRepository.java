package com.example.empregados_tdd_exercicio.repositories;

import com.example.empregados_tdd_exercicio.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
