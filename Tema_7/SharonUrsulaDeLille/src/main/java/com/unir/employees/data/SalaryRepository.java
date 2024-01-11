package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SalaryRepository extends JpaRepository<Salary, String> {
    // Método para obtener el salario actual del trabajo del empleado usando el ID de empleado.
    Salary findFirstByEmpNo_EmpNoOrderByToDateDesc(Integer empNo);
}

