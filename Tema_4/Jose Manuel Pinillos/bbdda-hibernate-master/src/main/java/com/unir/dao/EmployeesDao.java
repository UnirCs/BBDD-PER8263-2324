package com.unir.dao;

import com.unir.model.mysql.Employee;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class EmployeesDao {

    private final Session session;

    // 1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
    public List<Object[]> groupEmployeesByGender() throws SQLException {
        NativeQuery<Object[]> query = session.createNativeQuery("SELECT gender, count(*) as Total\n" +
                "        FROM employees\n" +
                "        GROUP BY gender\n" +
                "        ORDER BY Total desc");

        return query.list();
    }

    // 2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
    public Employee findSecondEmployeeBestSalary(String departmentId) throws SQLException {
        NativeQuery<Employee> query = session.createNativeQuery("SELECT employees.*\n" +
                "    FROM employees, salaries, departments, dept_emp\n" +
                "    WHERE employees.emp_no = salaries.emp_no and\n" +
                "    employees.emp_no = dept_emp.emp_no and\n" +
                "    departments.dept_no = dept_emp.dept_no and\n" +
                "    dept_emp.dept_no = :deptNo\n" +
                "    ORDER BY salary DESC\n" +
                "    LIMIT 1,1", Employee.class);

        query.setParameter("deptNo", departmentId);

        Employee employee = query.getSingleResult();
        return employee;
    }

    // 3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
    public Employee findEmployeeBestSalary(String departmentId) throws SQLException {
        NativeQuery<Employee> query = session.createNativeQuery("SELECT employees.*\n" +
                "    FROM employees, salaries, departments, dept_emp\n" +
                "    WHERE employees.emp_no = salaries.emp_no and\n" +
                "    employees.emp_no = dept_emp.emp_no and\n" +
                "    departments.dept_no = dept_emp.dept_no and\n" +
                "    dept_emp.dept_no = :deptNo\n" +
                "    ORDER BY salary DESC\n" +
                "    LIMIT 1", Employee.class);

        query.setParameter("deptNo", departmentId);

        Employee employee = query.getSingleResult();
        return employee;
    }

    // 4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
    public Long findNumberOfEmployees(int month) throws SQLException {
        NativeQuery<Long> query = session.createNativeQuery("SELECT count(*) as Empleados_contratados\n" +
                "FROM employees\n" +
                "WHERE month(hire_date) = :hireDate");

        query.setParameter("hireDate", month);
        return query.getSingleResult();
    }
}
