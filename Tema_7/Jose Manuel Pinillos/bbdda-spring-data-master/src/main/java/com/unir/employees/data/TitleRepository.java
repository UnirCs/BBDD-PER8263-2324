package com.unir.employees.data;

import com.unir.employees.model.db.Salary;
import com.unir.employees.model.db.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> {

    // Método para buscar el título por el id del empleado
    Title findTitleByEmpNoAndToDate(String empNo, Date toDate);
}
