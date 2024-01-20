package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    // Método para buscar un departamento por nombre
    Optional<Department> findByDeptName(String deptName);

}
