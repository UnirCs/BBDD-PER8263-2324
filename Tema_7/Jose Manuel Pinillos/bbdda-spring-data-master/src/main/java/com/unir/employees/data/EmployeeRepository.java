package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	// Método para buscar empleados por nombre
	Employee findByEmpNo(int empNo);

	// Método para buscar empleados por nombre
	List<Employee> findByFirstName(String firstName);

	// Método para buscar empleados por apellido
	List<Employee> findByLastName(String lastName);

	// Método para buscar empleados por nombre y apellido
	List<Employee> findByFirstNameAndLastName(String firstName, String lastName);

	// Método para buscar empleados por genero
	List<Employee> findByGender(Gender gender);

	// Método para buscar empleados por nombre y ordenarlos por fecha de nacimiento
	List<Employee> findByFirstNameOrderByBirthDateDesc(String firstName);
}
