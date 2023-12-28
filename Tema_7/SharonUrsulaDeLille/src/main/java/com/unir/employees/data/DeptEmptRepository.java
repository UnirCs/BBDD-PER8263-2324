package com.unir.employees.data;

import com.unir.employees.model.db.DeptEmp;
import com.unir.employees.model.db.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeptEmptRepository extends JpaRepository<DeptEmp, String> {
    //Método que nos permita obtener, a partir del ID de empleado únicamente, el departamento actual del trabajo del empleado.
    DeptEmp findFirstByEmployeeEmpNoOrderByToDateDesc(Integer employee);

}
