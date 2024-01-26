package com.unir.employees.data;

import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Title;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<Title, String> {


    // Actualizar el título de un empleado
    Title save(Title title);

    // Crear un nuevo título para un empleado
    Title saveAndFlush(Title title);
}
