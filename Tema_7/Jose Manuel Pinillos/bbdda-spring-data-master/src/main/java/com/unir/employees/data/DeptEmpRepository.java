package com.unir.employees.data;

import com.unir.employees.model.db.DeptEmp;
import com.unir.employees.model.db.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DeptEmpRepository extends JpaRepository<DeptEmp, String> {

    // Método para buscar el departamento actual de un empleado por el id del empleado
    DeptEmp findDeptEmpByEmpNoAndToDate(String empNo, Date toDate);
}
