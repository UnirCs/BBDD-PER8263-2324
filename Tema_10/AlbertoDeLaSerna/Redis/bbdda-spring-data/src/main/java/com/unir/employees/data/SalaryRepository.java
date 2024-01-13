package com.unir.employees.data;

import com.unir.employees.model.db.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, String> {

    // Actualizar el salario de un empleado
    Salary save(Salary salary);

    // Crear un nuevo salario para un empleado
    Salary saveAndFlush(Salary salary);
}
