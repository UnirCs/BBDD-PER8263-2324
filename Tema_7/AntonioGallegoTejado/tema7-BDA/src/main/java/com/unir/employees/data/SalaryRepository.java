package com.unir.employees.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {

	List<Salary> findByEmployee(Employee employee);

	@Query("SELECT s FROM Salary s WHERE employee=:employee AND s.toDate=cast('9999-01-01' as date)")
	Salary getEmployeeActualSalary(@Param("employee") Employee employee);
}
