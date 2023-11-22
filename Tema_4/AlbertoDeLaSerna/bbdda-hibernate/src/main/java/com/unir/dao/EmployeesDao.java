package com.unir.dao;

import com.unir.model.mysql.Employee;
import com.unir.model.mysql.Salary;
import com.unir.model.mysql.Title;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        // List<Employee> employees = session.createNativeQuery("SELECT * FROM employees", Employee.class).list();
        List<Employee> employees = session.createQuery("FROM Employee", Employee.class).list();
        log.debug("Número de empleados: {}", employees.size());


        for(Employee employee : employees) {
            Set<Salary> salaries = employee.getSalaries();
            log.debug("Empleado: {} tiene {} salarios", employee.getFirstName(), salaries.size());
            Set<Title> titles = employee.getTitles();
            log.debug("Empleado: {} tiene {} títulos", employee.getFirstName(), titles.size());
        }
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
     * Consulta del número de empleados agrupados por género utilizando SQL nativo.
     * @return Mapa con el género como clave y el total de empleados como valor.
     * @throws SQLException Excepción en caso de error.
     */
    public List<Object[]> numberOfEmployeesGroupByGenere() throws SQLException {
        String sqlQuery = "SELECT gender, COUNT(*) as total FROM employees GROUP BY gender ORDER BY total DESC";
        NativeQuery<Object[]> query = session.createNativeQuery(sqlQuery);

        return query.list();

    }

    /**
     * Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto
     * @return Employee con el salario máximo en su propiedad salaries
     * @throws SQLException Excepción en caso de error.
     */
    public Employee getTheBestSalaryEmployees(String department) throws SQLException {
        String sqlQuery = "SELECT e.* FROM employees.employees e\n" +
                "JOIN employees.salaries s ON e.emp_no = s.emp_no\n" +
                "JOIN employees.dept_emp de ON e.emp_no = de.emp_no\n" +
                "JOIN employees.departments d ON de.dept_no = d.dept_no\n" +
                "WHERE d.dept_no = :deptNo\n" +
                "ORDER BY s.salary DESC\n" +
                "LIMIT 1";
        NativeQuery<Employee> query = session.createNativeQuery(sqlQuery, Employee.class);
        query.setParameter("deptNo", department);
        Employee employee = query.getSingleResult();


        Set<Salary> salaries = employee.getSalaries();

        // Buscar el salario más alto
        Salary maxSalary = salaries.stream().max((s1, s2) -> s1.getSalary().compareTo(s2.getSalary())).get();

        // Ahora sólo dejo el salario más alto
        employee.setSalaries(salaries.stream().filter(s -> s.getSalary().equals(maxSalary.getSalary())).collect(Collectors.toSet()));

        return employee;


    }

    /**
     * Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto
     * @return Employee con el salario máximo en su propiedad salaries
     * @throws SQLException Excepción en caso de error.
     */
    public Employee getTheSecondBestSalaryEmployees(String department) throws SQLException {
        String sqlQuery = "SELECT e.* FROM employees.employees e\n" +
                "JOIN employees.salaries s ON e.emp_no = s.emp_no\n" +
                "JOIN employees.dept_emp de ON e.emp_no = de.emp_no\n" +
                "JOIN employees.departments d ON de.dept_no = d.dept_no\n" +
                "WHERE d.dept_no = :deptNo\n" +
                "ORDER BY s.salary DESC\n" +
                "LIMIT 1,1";
        NativeQuery<Employee> query = session.createNativeQuery(sqlQuery, Employee.class);
        query.setParameter("deptNo", department);
        Employee employee = query.getSingleResult();


        Set<Salary> salaries = employee.getSalaries();

        // Buscar el salario más alto
        Salary maxSalary = salaries.stream().max((s1, s2) -> s1.getSalary().compareTo(s2.getSalary())).get();

        // Ahora sólo dejo el salario más alto
        employee.setSalaries(salaries.stream().filter(s -> s.getSalary().equals(maxSalary.getSalary())).collect(Collectors.toSet()));

        return employee;


    }

    /**
     * El total de empleados contratados en un mes concreto
     * @return el número total
     * @throws SQLException Excepción en caso de error.
     */
    public Long getTotalOfEmployeesHired(Integer month){
        String sqlQuery = "SELECT COUNT(*) FROM employees WHERE MONTH(hire_date) = :month";
        NativeQuery<Long> query = session.createNativeQuery(sqlQuery);
        query.setParameter("month", month);
        return query.getSingleResult();
    }

}
