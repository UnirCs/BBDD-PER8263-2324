package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	List<Employee> findByLastName(String lastName);

	// Método para buscar empleados por nombre y apellido
	List<Employee> findByFirstNameAndLastName(String firstName, String lastName);

	// Método para buscar empleados por fecha de contratación posterior a una fecha dada
	List<Employee> findByHireDateAfter(Date hireDate);

	// Método para contar el número de empleados por género
	<Gender> long countByGender(Gender gender);

	// Método para obtener todos los empleados ordenados por fecha de contratación de forma descendente
	List<Employee> findAllByOrderByHireDateDesc();
}
