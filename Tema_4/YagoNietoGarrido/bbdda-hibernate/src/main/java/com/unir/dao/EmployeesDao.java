package com.unir.dao;

import com.unir.model.mysql.Employee;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.lang.annotation.Native;
import java.sql.*;
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
     *
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
     *
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
     *
     * @param id - Identificador del empleado.
     * @return Empleado.
     * @throws SQLException - Excepción en caso de error.
     */
    public Employee getById(Integer id) throws SQLException {
        return session.get(Employee.class, id);
    }

    /**
     * Elimina un empleado de la base de datos.
     *
     * @param employee - Empleado a eliminar.
     * @return true si se ha eliminado correctamente.
     * @throws SQLException - Excepción en caso de error.
     */
    public Boolean remove(Employee employee) throws SQLException {
        session.remove(employee);
        return true;
    }

    /**
     * Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
     *
     * @throws SQLException
     */
    public List<Object[]> selectGroupEmployeesGender() throws SQLException {
        Query<Object[]> query = session.createNativeQuery(
                "SELECT  gender, count(*) as 'cantidad'  " +
                        "FROM employees GROUP BY gender " +
                        "ORDER BY cantidad DESC", Object[].class);
        return query.list();
    }

    /**
     * Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
     *
     * @param departmentId - Identificador del departamento.
     * @return Empleado.
     * @throws SQLException - Excepción en caso de error.
     */
    public Employee selectMaxSalaryPersonDeparment(String departmentId) throws SQLException {
        Query<Employee> query = session.createNativeQuery(
                "SELECT employees.*\n" +
                        "from ((employees INNER JOIN salaries ON salaries.emp_no = employees.emp_no)\n" +
                        "INNER JOIN dept_emp ON employees.emp_no = dept_emp.emp_no)\n" +
                        "WHERE dept_emp.dept_no = :deptNo\n" +
                        "ORDER BY salary DESC\n" +
                        "LIMIT 1;", Employee.class
        ).setParameter("deptNo", departmentId);

        return query.list().get(0);
    }

    /**
     * Mostrar el nombre, apellido y salario de la 2ª persona mejor pagada de un departamento concreto (parámetro variable).
     *
     * @param departmentId - Identificador del departamento.
     * @return Empleado.
     * @throws SQLException - Excepción en caso de error.
     */
    public Employee select2ndMaxSalaryPersonDeparment(String departmentId) throws SQLException {
        Query<Employee> query = session.createNativeQuery(
                "SELECT employees.*\n" +
                        "from ((employees INNER JOIN salaries ON salaries.emp_no = employees.emp_no)\n" +
                        "INNER JOIN dept_emp ON employees.emp_no = dept_emp.emp_no)\n" +
                        "WHERE dept_emp.dept_no = :deptNo\n" +
                        "ORDER BY salary DESC\n" +
                        "LIMIT 1 OFFSET 1;", Employee.class
        ).setParameter("deptNo", departmentId);

        return query.list().get(0);
    }

    /**
     * Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
     *
     * @throws SQLException
     */
    public Integer selectEmployeesHiredAtMonth(String month) throws SQLException {
        Query<Integer> query = session.createNativeQuery(
                    "SELECT count(*) as 'Total'\n" +
                            "from employees\n" +
                            "Where Month(employees.hire_date) = :monthNumber ;", Integer.class
                ).setParameter("monthNumber", month);

        return query.list().get(0);
    }
}
