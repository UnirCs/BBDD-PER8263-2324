package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    //Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

    // Método para buscar un departamento por nombre
    Optional<Department> findByDeptName(String deptName);

    // Listar todos los departamentos en orden alfabético por nombre
    List<Department> findAllByOrderByDeptNameAsc();

    // Método para obtener todos los departamentos ordenados por id de forma ascendente
    List<Department> findAllByOrderByDeptNo();

    //Método para buscar departamentos con más empleados de un numero dado
    @Query("SELECT d FROM Department d WHERE SIZE(d.deptEmps) > :limit")
    List<Department> findDepartmentsWithMoreThanXEmployees(int limit);
}
