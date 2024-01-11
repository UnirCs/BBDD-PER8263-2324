package com.unir.employees.data;

import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TitleRepository extends JpaRepository<Title, String> {
    // Método para obtener el título actual del trabajo del empleado usando el ID de empleado.
    Title findFirstByEmpNo_EmpNoOrderByToDateDesc(Integer empNo);
}

