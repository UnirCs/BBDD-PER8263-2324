package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {


    //Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

    // Método para buscar un departamento por nombre
    Optional<Department> findByDeptName(String deptName);

    // Método para buscar departamentos por nombre que contengan una cierta palabra clave
    List<Department> findByDeptNameContaining(String keyword);

    // Método para buscar departamentos por nombre y ubicación
    Optional<Department> findByDeptNameAndLocation(String deptName, String location);


}
