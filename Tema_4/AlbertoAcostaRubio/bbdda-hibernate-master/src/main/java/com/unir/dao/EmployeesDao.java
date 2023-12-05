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
    public List<Object[]> countEmployeesByGender() {
        String sqlQuery = "SELECT gender, COUNT(*) AS cantidad\n " +
                "FROM employees.employees\n " +
                "GROUP BY gender\n " +
                "ORDER BY cantidad DESC";

        Query<Object[]> query = session.createNativeQuery(sqlQuery);
        List<Object[]> lista = query.getResultList();

        return lista;
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

        return query.getResultList();
    }

    public Object[] segundoMejorPagado(String departmentId) {
        String sqlQuery = "SELECT e.first_name, e.last_name, s.salary, d.dept_no\n  " +
                "FROM employees e\n  " +
                "JOIN salaries s ON e.emp_no = s.emp_no\n  " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no\n  " +
                "JOIN departments d ON de.dept_no = d.dept_no\n  " +
                "WHERE d.dept_no = :deptNo\n  " +
                "ORDER BY s.salary DESC\n  " +
                "LIMIT 1 OFFSET 1";


        Query query = session.createNativeQuery(sqlQuery);
        query.setParameter("deptNo", departmentId);

        Object[] lista = (Object[]) query.getSingleResult();

        return lista;
    }

    public Long empleadosContratadosMes(int mes) {
        String sqlQuery = "SELECT COUNT(*) AS numero_empleados_contratados\n  " +
                "FROM employees\n  " +
                "WHERE MONTH(hire_date) = :hire_date";

        Query query = session.createNativeQuery(sqlQuery);
        query.setParameter("hire_date", mes);

        return ((Number) query.getSingleResult()).longValue();
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

}
