package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, String> {
    final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

    @Query(value = "Select * from salaries where emp_no = :empNo AND to_date = '9999-01-01'", nativeQuery = true)
    Optional<Salary> findLastSalary(int empNo);

    @Modifying
    @Query(value = "UPDATE salaries Set " +
            "to_date = :fromDate "+
            "WHERE emp_no = :empNo AND to_date = '9999-01-01'", nativeQuery = true)
    int updateLastSalary(int empNo, String fromDate);

    @Modifying
    @Query(value = "INSERT INTO salaries (emp_no, from_date, to_date, salary) VALUES " +
            "( " +
            ":salary.empleado.emp_no ," +
            ":salary.fromDate ,"+
            "'9999-01-01' ,"+
            ":salary.salary "+
            ")", nativeQuery = true)
    int insertNewSalary(@Param("salary") Salary salary);

}
