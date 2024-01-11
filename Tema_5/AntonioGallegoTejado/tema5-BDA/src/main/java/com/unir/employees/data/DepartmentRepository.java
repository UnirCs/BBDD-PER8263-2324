package com.unir.employees.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.employees.model.db.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

	// Documentacion sobre Derivacion de consultas:
	// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	// Documentacion sobre consultas nativas:
	// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

	// Método para buscar un departamento por nombre
	Optional<Department> findByDeptName(String deptName);

	//Método que recupera los departamentos con un número de empleados según parámetros
	@Query("SELECT d FROM Department d WHERE SIZE(d.deptEmps) BETWEEN :from AND :to")
	List<Department> findAllByEmployeeNumber(@Param("from") Integer from, @Param("to") Integer to);

}
