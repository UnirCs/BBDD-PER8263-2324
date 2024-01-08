package com.unir.employees.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;
import com.unir.employees.model.db.Title;

@Repository
public interface TitleRepository extends JpaRepository<Title, Integer> {

	List<Title> findByEmployee(Employee employee);
}
