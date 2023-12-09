package com.unir.employees.data;

import com.unir.employees.model.db.DeptEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeptEmpRepository extends JpaRepository<DeptEmp, Integer> {

	//Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	//Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

    DeptEmp findTopByEmployeeEmpNoOrderByToDateDesc(Integer empNo);



}


