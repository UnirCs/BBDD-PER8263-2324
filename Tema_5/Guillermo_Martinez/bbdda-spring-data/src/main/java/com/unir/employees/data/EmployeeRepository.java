package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	// Método para buscar empleados por apellido
	List<Employee> findByLastName(String lastName);

	// Método para buscar empleados por fecha de nacimiento posterior a una fecha específica
	List<Employee> findByBirthDateAfter(Date date);

	// Método para buscar empleados por nombre y apellido
	List<Employee> findByFirstNameAndLastName(String firstName, String lastName);

	// Método para contar empleados por departamento
	Long countByDeptEmpsDeptNo(String deptNo);
}

