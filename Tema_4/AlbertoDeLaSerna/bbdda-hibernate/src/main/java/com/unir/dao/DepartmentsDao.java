package com.unir.dao;

import com.unir.model.mysql.Department;
import com.unir.model.mysql.DeptEmployee;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Slf4j
public class DepartmentsDao {

    private final Session session;

    /**
     * Consulta de todos los departamentos de la base de datos
     * @return Lista de departamentos
     * @throws SQLException Excepción en caso de error
     */
    public List<Department> findAll() throws SQLException {
        List<Department> departments = session.createQuery("from Department", Department.class).list();
        for(Department department : departments) {
            //Podemos cambiar el fetch de Department para ver los cambios. Por defecto es LAZY
            Set<DeptEmployee> employeesOfDepartment = department.getDeptEmployees();
            log.info("Departamento: {} tiene {} empleados", department.getDeptName(), employeesOfDepartment.size());
        }
        return departments;
    }

    /**
     * Inserta un nuevo departamento en la base de datos.
     * @param department Departamento a insertar
     * @return Departamento insertado
     * @throws SQLException Excepción en caso de error
     */
    public Department save(Department department) throws SQLException {
        session.beginTransaction();
        session.persist(department);
        session.getTransaction().commit();
        return department;
    }
}
