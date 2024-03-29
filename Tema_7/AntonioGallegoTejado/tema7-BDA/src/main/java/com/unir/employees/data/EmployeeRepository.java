package com.unir.employees.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unir.employees.model.db.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	//Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	//Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

	// Método para buscar empleados por apellido
	List<Employee> findByLastName(String lastName);

	// Método para buscar empleados entre dos fechas de contratación
	List<Employee> findAllByHireDateBetween(Date hireDateStart, Date hireDateEns);
}
