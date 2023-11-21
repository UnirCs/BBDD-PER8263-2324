package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    Optional<Department> findByDeptName(String deptName);

    // Método para buscar un departamento por ubicación
    Optional<Department> findByLocation(String location);

    // Método para contar el número de empleados en un departamento
    long countEmployeesByDeptName(String deptName);

    // Método para obtener todos los departamentos ordenados por nombre de forma ascendente
    List<Department> findAllByOrderByIdAsc();

    // Método para buscar departamentos por nombre que contengan una cadena específica
    List<Department> findByDeptNameContaining(String keyword);
}