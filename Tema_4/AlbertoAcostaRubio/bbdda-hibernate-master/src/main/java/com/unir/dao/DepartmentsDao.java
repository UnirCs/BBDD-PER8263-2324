package com.unir.dao;

import com.unir.model.mysql.Department;
import com.unir.model.mysql.DeptEmployee;
import com.unir.model.mysql.Employee;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;

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
    public List<Employee> mejorPagado(String departmentId) {
        String sqlQuery = "SELECT e.first_name, e.last_name, s.salary, d.dept_no\n  " +
                "FROM employees e\n  " +
                "JOIN salaries s ON e.emp_no = s.emp_no\n  " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no\n  " +
                "JOIN departments d ON de.dept_no = d.dept_no\n  " +
                "WHERE d.dept_no = :deptNo\n  " +
                "ORDER BY s.salary DESC\n  " +
                "LIMIT 1";

        Query<Employee> query = session.createNativeQuery(sqlQuery, Employee.class);
        query.setParameter("deptNo", departmentId);

        return query.list();
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
