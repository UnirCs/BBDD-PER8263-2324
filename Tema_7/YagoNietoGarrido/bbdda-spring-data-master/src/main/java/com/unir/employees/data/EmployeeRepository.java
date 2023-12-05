package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	//Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	//Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

	// Método para buscar empleados por apellido
	List<Employee> findByLastName(String lastName);

	Optional<List<Employee>> findByFirstName(String firstName);
	Optional<List<Employee>> findEmployeesByHireDateBetween(Date date1,Date date2);

	@Query(value = "SELECT employees.* from ((employees INNER JOIN salaries ON salaries.emp_no = employees.emp_no) INNER JOIN dept_emp ON employees.emp_no = dept_emp.emp_no) WHERE dept_emp.dept_no = :deptNo ORDER BY salary DESC LIMIT 1;", nativeQuery=true)
	Optional<List<Employee>> findMaxSalaryEmployeeByDepartment(String deptNo);

	Optional<Employee> findByEmpNo(int empNo);

}
