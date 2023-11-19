package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    //Documentacion sobre Derivacion de consultas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //Documentacion sobre consultas nativas: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

    // Método para buscar un departamento por nombre
    Optional<Department> findByDeptName(String deptName);

    // Buscar departamentos por nombre
    List<Department> findByDeptNameContaining(String name);

    // Listar todos los departamentos en orden alfabético por nombre
    List<Department> findAllByOrderByDeptNameAsc();

    // Consultas personalizadas fuera del standar. Impresionante!!!

    // Buscar departamentos que tienen más/menos de una cantidad específica de empleados
    @Query("SELECT d FROM Department d WHERE SIZE(d.deptEmps) > :count")
    List<Department> findDepartmentsWithMoreThanXEmployees(int count);

    @Query("SELECT d FROM Department d WHERE SIZE(d.deptEmps) < :count")
    List<Department> findDepartmentsWithLessThanXEmployees(int count);

    // Buscar departamentos que no tienen ningún gerente asignado
    @Query("SELECT d FROM Department d WHERE d.deptManagers IS EMPTY")
    List<Department> findDepartmentsWithoutManagers();

    // Buscar departamentos con un número específico de empleados
    @Query("SELECT d FROM Department d WHERE SIZE(d.deptEmps) = :count")
    List<Department> findDepartmentsWithExactNumberOfEmployees(int count);

}
