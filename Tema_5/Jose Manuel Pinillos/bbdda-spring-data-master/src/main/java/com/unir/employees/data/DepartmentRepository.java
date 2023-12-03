package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    //Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

    // Método para buscar un departamento por id
    List<Department> findByDeptNo(String deptNo);

    // Método para buscar un departamento por nombre
    List<Department> findByDeptName(String deptName);

    // Método para mostrar los departamentos ordenados por nombre
    List<Department> findAllByOrderByDeptNameAsc();

    // Método para mostrar los departamentos que tienen menos de una cantidad de empleados
    @Query("SELECT d FROM Department d WHERE SIZE(d.deptEmps) < :count")
    List<Department> findDepartmentsWithLessThanXEmployees(int count);
}
