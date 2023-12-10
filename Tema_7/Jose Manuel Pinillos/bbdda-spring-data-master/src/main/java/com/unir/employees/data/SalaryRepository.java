package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, String> {

    // Método para buscar un salario por el id del empleado
    Salary findSalaryByEmpNoAndToDate(Employee employee, Date toDate);
}
