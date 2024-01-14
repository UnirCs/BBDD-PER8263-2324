package com.unir.employees.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Title;

@Repository
public interface TitleRepository extends JpaRepository<Title, Integer> {

	List<Title> findByEmployee(Employee employee);

	@Query("SELECT t FROM Title t WHERE employee=:employee AND t.toDate=cast('9999-01-01' as date)")
	Title getEmployeeActualTitle(@Param("employee") Employee employee);
}
