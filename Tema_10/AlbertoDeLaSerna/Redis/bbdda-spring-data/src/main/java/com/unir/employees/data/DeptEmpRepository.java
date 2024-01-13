package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.DeptEmp;
import com.unir.employees.model.db.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptEmpRepository extends JpaRepository<DeptEmp, String> {
    // Buscar el último departamento de un empleado, sabemos que el último departamento es el que tiene la fecha de fin "9999-01-01"
    //DeptEmp findFirstByEmployeeAndDepartmentOrderByToDateDesc(Employee employee, Department department);

    // Actualizar el departamento de un empleado
    DeptEmp save(DeptEmp deptEmp);

    // Crear un nuevo departamento para un empleado
    DeptEmp saveAndFlush(DeptEmp deptEmp);
}
