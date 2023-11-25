package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    //Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

    // Método para buscar un departamento por nombre
    Optional<Department> findByDeptName(String deptName);
    @Query(value="Select d.* from departments d inner join dept_emp de on d.dept_no = de.dept_no where de.emp_no = :idEmp", nativeQuery=true)
    Optional<Department> findByEmpNo(String idEmp);
}
