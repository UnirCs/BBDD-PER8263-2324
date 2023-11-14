package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MySqlApplication {

    private static final String DATABASE = "employees";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos Oracle");

            //selectAllEmployeesOfDepartment(connection, "d001");
            //selectAllEmployeesOfDepartment(connection, "d002");
            countMenAndWomen(connection);
            highestPaidEmployee(connection, "Development");
            secondHighestPaidEmployee(connection, "Marketing");
            hiredEmployeesByMonth(connection, "11");


        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * 1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente
     * @throws SQLException
     */
    private static void countMenAndWomen(Connection connection) throws SQLException {
        PreparedStatement selectMenAndWomen = connection.prepareStatement(
                "SELECT COUNT(gender) AS genders_num, gender\n" +
                "FROM employees\n" +
                "GROUP BY gender\n" +
                "ORDER BY genders_num DESC;\n"
        );
        ResultSet menAndWomen = selectMenAndWomen.executeQuery();

        while (menAndWomen.next()) {
            log.debug("Género: {}, Total: {}",
                    menAndWomen.getString("gender"),
                    menAndWomen.getInt("genders_num"));
        }
    }

    /**
     * -- 2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
     * @param department
     * @throws SQLException
     */
    private static void highestPaidEmployee(Connection connection, String department) throws SQLException {
        PreparedStatement selecthighestPaidEmployee = connection.prepareStatement(
                "SELECT employees.first_name, employees.last_name, salaries.salary\n" +
                    "FROM salaries\n" +
                    "         JOIN employees ON salaries.emp_no = employees.emp_no\n" +
                    "where salaries.emp_no IN (SELECT employees.emp_no\n" +
                    "                          FROM employees\n" +
                    "                                   JOIN dept_emp ON employees.emp_no = dept_emp.emp_no\n" +
                    "                                   JOIN departments ON departments.dept_no = dept_emp.dept_no\n" +
                    "                          WHERE departments.dept_name = ?)\n" +
                    "ORDER BY salaries.salary DESC\n" +
                    "LIMIT 1;"
        );
        selecthighestPaidEmployee.setString(1, department);
        ResultSet highestPaidEmployee = selecthighestPaidEmployee.executeQuery();

        while (highestPaidEmployee.next()) {
            log.debug("Nombre: {}, Apellido: {}, Salario: {}",
                    highestPaidEmployee.getString("first_name"),
                    highestPaidEmployee.getString("last_name"),
                    highestPaidEmployee.getFloat("salary"));
        }
    }

    /**
     * -- 3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
     * @param department
     * @throws SQLException
     */
    private static void secondHighestPaidEmployee(Connection connection, String department) throws SQLException {
        PreparedStatement selecthighestPaidEmployee = connection.prepareStatement(
                "SELECT employees.first_name, employees.last_name, salaries.salary\n" +
                        "FROM salaries\n" +
                        "         JOIN employees ON salaries.emp_no = employees.emp_no\n" +
                        "where salaries.emp_no IN (SELECT employees.emp_no\n" +
                        "                          FROM employees\n" +
                        "                                   JOIN dept_emp ON employees.emp_no = dept_emp.emp_no\n" +
                        "                                   JOIN departments ON departments.dept_no = dept_emp.dept_no\n" +
                        "                          WHERE departments.dept_name = ?)\n" +
                        "ORDER BY salaries.salary DESC\n" +
                        "LIMIT 1;"
        );
        selecthighestPaidEmployee.setString(1, department);
        ResultSet highestPaidEmployee = selecthighestPaidEmployee.executeQuery();

        while (highestPaidEmployee.next()) {
            log.debug("Nombre: {}, Apellido: {}, Salario: {}",
                    highestPaidEmployee.getString("first_name"),
                    highestPaidEmployee.getString("last_name"),
                    highestPaidEmployee.getFloat("salary"));
        }
    }

    /**
     * -- 4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
     * @param month
     * @throws SQLException
     */
    private static void hiredEmployeesByMonth(Connection connection, String month) throws SQLException {
        PreparedStatement selectHiredEmployeesByMonth = connection.prepareStatement(
                "SELECT COUNT(*) AS numOfHiredEmployees\n" +
                    "FROM employees\n" +
                    "WHERE MONTH(employees.hire_date) = ?;"
        );
        selectHiredEmployeesByMonth.setString(1, month);
        ResultSet hiredEmployeesByMonth = selectHiredEmployeesByMonth.executeQuery();

        while (hiredEmployeesByMonth.next()) {
            log.debug("Número de empleados contratados en el mes {}: {}",
                    month,
                    hiredEmployeesByMonth.getInt("numOfHiredEmployees"));
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando Statement.
     * Statement es la forma más básica de ejecutar consultas a la base de datos.
     * Es la más insegura, ya que no se protege de ataques de inyección SQL.
     * No obstante es útil para sentencias DDL (creando una tabla, un índice...) sentencias que NUNCA nos la va a dar un terecero
     * @param connection
     * @throws SQLException
     */
    private static void selectAllEmployees(Connection connection) throws SQLException {
        Statement selectEmployees = connection.createStatement();
        ResultSet employees = selectEmployees.executeQuery("select * from employees");

        while (employees.next()) {
            log.debug("Employee: {} {}",
                    employees.getString("first_name"),
                    employees.getString("last_name"));
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando PreparedStatement.
     * PreparedStatement es la forma más segura de ejecutar consultas a la base de datos.
     * Se protege de ataques de inyección SQL.
     * Es útil para sentencias DML.
     * @param connection
     * @throws SQLException
     */
    private static void selectAllEmployeesOfDepartment(Connection connection, String department) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select count(*) as 'Total'\n" +
                "from employees emp\n" +
                "inner join dept_emp dep_rel on emp.emp_no = dep_rel.emp_no\n" +
                "inner join departments dep on dep_rel.dept_no = dep.dept_no\n" +
                "where dep_rel.dept_no = ?;\n");
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleados del departamento {}: {}",
                    department,
                    employees.getString("Total"));
        }
    }
}
