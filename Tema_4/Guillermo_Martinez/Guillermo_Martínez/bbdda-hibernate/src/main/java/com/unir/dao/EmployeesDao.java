package com.unir.dao;

import com.unir.model.mysql.Employee;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class EmployeesDao {

    private final Session session;

    /**
     * Consulta de todos los empleados de la base de datos
     * Se ofrecen dos versiones
     * 1. Con SQL nativo
     * 2. Con HQL: https://docs.jboss.org/hibernate/orm/3.5/reference/es-ES/html/queryhql.html
     * @throws SQLException Excepción en caso de error
     */
    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = session.createNativeQuery("SELECT * FROM employees", Employee.class).list();
        log.debug("Número de empleados: {}", employees.size());
        session.createQuery("FROM Employee", Employee.class).list();
        return employees;
    }

    /**
     * Consulta de todos los empleados de un departamento
     * @param departmentId Identificador del departamento
     * @return Lista de empleados
     * @throws SQLException Excepción en caso de error
     */
    public List<Employee> findByDepartment(String departmentId) throws SQLException {
        Query<Employee> query = session.createNativeQuery("SELECT e.*\n" +
                "FROM employees.employees e\n" +
                "JOIN employees.dept_emp de ON e.emp_no = de.emp_no\n" +
                "JOIN employees.departments d ON de.dept_no = d.dept_no\n" +
                "WHERE d.dept_no = :deptNo", Employee.class);
        query.setParameter("deptNo", departmentId);
        return query.list();
    }

    /**
     * Obtención de un empleado por su identificador.
     * @param id - Identificador del empleado.
     * @return Empleado.
     * @throws SQLException - Excepción en caso de error.
     */
    public Employee getById(Integer id) throws SQLException {
        return session.get(Employee.class, id);
    }

    /**
     * Elimina un empleado de la base de datos.
     * @param employee - Empleado a eliminar.
     * @return true si se ha eliminado correctamente.
     * @throws SQLException - Excepción en caso de error.
     */
    public Boolean remove(Employee employee) throws SQLException {
        session.remove(employee);
        return true;
    }

    /**
     * Consulta para obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
     * @return Lista con los resultados (gender y total)
     * @throws SQLException Excepción en caso de error
     */
    public List<Object[]> findGenderCount() throws SQLException {
        Query<Object[]> query = session.createNativeQuery("SELECT gender, COUNT(*) AS total " +
                "FROM employees " +
                "GROUP BY gender " +
                "ORDER BY total DESC");
        return query.list();
    }
    /**
     * Consulta para obtener el nombre, apellido y salario de la persona mejor pagada de un departamento concreto.
     * @param departmentId Identificador del departamento
     * @return Lista con los resultados (first_name, last_name, salary)
     * @throws SQLException Excepción en caso de error
     */
    public List<Object[]> findHighestPaidEmployeeInDepartment(String departmentId) throws SQLException {
        Query<Object[]> query = session.createNativeQuery("SELECT e.first_name, e.last_name, s.salary " +
                "FROM employees e " +
                "JOIN salaries s ON e.emp_no = s.emp_no " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                "WHERE de.dept_no = :deptNo " +
                "      AND s.to_date = '9999-01-01' " +
                "ORDER BY s.salary DESC " +
                "LIMIT 1");
        query.setParameter("deptNo", departmentId);
        return query.list();
    }

}
