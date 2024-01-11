package com.unir.employees.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {

	List<Salary> findByEmployee(Employee employee);

}
