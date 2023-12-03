package com.unir.employees.data;

import com.unir.employees.model.db.Salary;
import com.unir.employees.model.db.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> {

    //Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
    @Query(value = "Select * from titles where emp_no = :empNo AND to_date = '9999-01-01'", nativeQuery = true)
    Optional<Title> findLastTitle(int empNo);

    @Modifying
    @Query(value = "UPDATE titles Set " +
            "to_date = :fromDate " +
            "WHERE emp_no = :empNo AND to_date = '9999-01-01'", nativeQuery = true)
    int updateLastTitle(int empNo, String fromDate);

    @Modifying
    @Query(value = "INSERT INTO titles (emp_no, from_date, title, to_date) VALUES " +
            "( " +
            ":title.empleado.empNo ," +
            ":title.fromDate ," +
            ":title.title ," +
            "'9999-01-01'" +
            ")", nativeQuery = true)
    int insertNewTitle(@Param("title") Title title);

}
