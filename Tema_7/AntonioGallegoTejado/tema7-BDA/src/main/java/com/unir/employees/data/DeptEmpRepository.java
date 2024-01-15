package com.unir.employees.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.employees.model.db.DeptEmp;
import com.unir.employees.model.db.Employee;

@Repository
public interface DeptEmpRepository extends JpaRepository<DeptEmp, String> {

	// Documentacion sobre Derivacion de consultas:
	// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	// Documentacion sobre consultas nativas:
	// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

	@Query("SELECT d FROM DeptEmp d WHERE employee=:employee AND d.toDate=cast('9999-01-01' as date)")
	DeptEmp getEmployeeActualDept(@Param("employee") Employee employee);
}
