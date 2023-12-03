package com.unir.employees.data;

import com.unir.employees.model.db.DeptEmp;
import com.unir.employees.model.db.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeptEmptRepository extends JpaRepository<DeptEmp, String> {

    //Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
    @Query(value = "Select * from dept_emp where emp_no = :empNo AND to_date = '9999-01-01'", nativeQuery = true)
    Optional<List<DeptEmp>> findLastDeptEmp(int empNo);


    @Modifying
    @Query(value = "UPDATE dept_emp Set " +
            "to_date = :fromDate " +
            "WHERE emp_no = :empNo AND to_date = '9999-01-01'", nativeQuery = true)
    int updateLastDeptEmpt(int empNo, String fromDate);

    @Modifying
    @Query(value = "INSERT INTO dept_emp (emp_no, dept_no, from_Date, to_date) VALUES " +
            "( " +
            ":deptEmp.empleado.empNo ," +
            ":deptEmp.dept_no ," +
            ":deptEmp.fromDate ," +
            "'9999-01-01'" +
            ")", nativeQuery = true)
    int insertNewDeptEmpt(@Param("deptEmp") DeptEmp deptEmp);
}
