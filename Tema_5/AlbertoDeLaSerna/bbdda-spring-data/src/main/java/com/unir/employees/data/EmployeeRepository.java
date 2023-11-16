package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	//Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	//Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

	// Método para buscar empleados por apellido
	List<Employee> findByLastName(String lastName);

	// Método para buscar empleados por fecha de nacimiento
	List<Employee> findByBirthDateBetween(Date startDate, Date endDate);

	// Método para buscar empleados por nombre
	List<Employee> findByFirstNameContaining(String name);

	// Método para buscar empleados por género
	List<Employee> findByGender(Gender gender);

	// Método para buscar empleados después de una fecha de contratación en concreto
	List<Employee> findByHireDateAfter(Date date);

	// Método para buscar empleados por apellido y ordenarlos por fecha de contratación
	List<Employee> findByLastNameOrderByHireDateAsc(String lastName);






}
