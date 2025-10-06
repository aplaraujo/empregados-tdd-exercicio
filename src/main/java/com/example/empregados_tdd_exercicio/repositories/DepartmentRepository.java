package com.example.empregados_tdd_exercicio.repositories;

import com.example.empregados_tdd_exercicio.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Busca paginada
    @Query("SELECT obj FROM Department obj WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Department> searchByName(String name, Pageable pageable);
}
