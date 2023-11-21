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

    public  List<Object> getNumberOfMenAndWomen() {
        List<Object> query= session.createNativeQuery("select gender as 'sexo', count(*) as 'cantidad' from employees.employees group by gender order by cantidad desc", Object.class).list();

        return query;

    }

    /**
     * Consulta el segundo salario de un departamento dado
     * @param departmentName Nombre del Departamenteo
     * @return Lista de empleados
     * @throws SQLException Excepción en caso de error
     */
    public  List<Object> findSecondSalayOfDepartment(String departmentName) throws SQLException {
        Query<Object> query =  session.createNativeQuery("Select e.first_Name,e.last_name,s.salary from employees.employees e\n" +
                "     join employees.salaries s on e.emp_no = s.emp_no\n" +
                "     join employees.dept_emp de on e.emp_no = de.emp_no\n" +
                "     join employees.departments d on de.dept_no = d.dept_no\n" +
                "where d.dept_name = :deptName " +
                "order by s.salary desc Limit 1 offset 1", Object.class);
        query.setParameter("deptName", departmentName);
        return query.list();
    }

    /**
     * Consulta el salario máximo de un departamento dado
     * @param departmentName Nombre del Departamenteo
     * @return Lista de empleados
     * @throws SQLException Excepción en caso de error
     */
    public  List<Object> findBestSalayOfDepartment(String departmentName) throws SQLException {
        Query<Object> query =  session.createNativeQuery("Select e.first_Name,e.last_name,s.salary from employees.employees e\n" +
                "     join employees.salaries s on e.emp_no = s.emp_no\n" +
                "     join employees.dept_emp de on e.emp_no = de.emp_no\n" +
                "     join employees.departments d on de.dept_no = d.dept_no\n" +
                "where d.dept_name = :deptName " +
                "order by s.salary desc Limit 1 ", Object.class);
        query.setParameter("deptName", departmentName);
        return query.list();
    }


    public List<String> findHiresInMonth (int month)throws SQLException {
        Query<String> query =  session.createNativeQuery("select count(*) as Contrataciones from employees.employees " +
                "where Month(hire_date) = :mes", String.class);
        query.setParameter("mes", month);
        return query.list();
    }

















}
