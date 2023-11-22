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
        try (Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos MySQL");

            selectGroupEmployeesGender(connection);
            selectMaxSalaryPersonDeparment(connection, "d001");
            select2ndMaxSalaryPersonDeparment(connection, "d001");
            selectEmployeesHiredAtMonth(connection, "1");

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
     *
     * @param connection
     * @throws SQLException
     */
    private static void selectGroupEmployeesGender(Connection connection) throws SQLException {
        Statement selectEmployees = connection.createStatement();
        ResultSet employees = selectEmployees.executeQuery(
                "SELECT gender, count(*) as 'cantidad'\n" +
                        "from employees\n" +
                        "GROUP BY gender\n" +
                        "ORDER BY cantidad DESC");

        while (employees.next()) {
            log.debug("Empleados del género {}: {}",
                    employees.getString("gender"),
                    employees.getString("cantidad"));
        }
    }

    /**
     * Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
     *
     * @param connection
     * @throws SQLException
     */
    private static void selectMaxSalaryPersonDeparment(Connection connection, String departmentId) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT CONCAT(first_name, ' ', last_name) as 'Name', MAX(salary) as 'MaxSalary'\n" +
                        "from ((employees INNER JOIN salaries ON salaries.emp_no = employees.emp_no)\n" +
                        "INNER JOIN dept_emp ON employees.emp_no = dept_emp.emp_no)\n" +
                        "WHERE dept_emp.dept_no = ?\n" +
                        "group by Name\n" +
                        "ORDER BY MaxSalary DESC\n" +
                        "LIMIT 1;");
        selectEmployees.setString(1, departmentId);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleado del departamento {} que más cobra: {} {}€",
                    departmentId,
                    employees.getString("Name"),
                    employees.getString("MaxSalary"));
        }
    }

    /**
     * Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
     *
     * @param connection
     * @throws SQLException
     */
    private static void select2ndMaxSalaryPersonDeparment(Connection connection, String departmentId) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT CONCAT(first_name, ' ', last_name) as 'Name', MAX(salary) as 'MaxSalary'\n" +
                        "from ((employees INNER JOIN salaries ON salaries.emp_no = employees.emp_no)\n" +
                        "INNER JOIN dept_emp ON employees.emp_no = dept_emp.emp_no)\n" +
                        "WHERE dept_emp.dept_no = ?\n" +
                        "group by Name\n" +
                        "ORDER BY MaxSalary DESC\n" +
                        "LIMIT 1 OFFSET 1;");
        selectEmployees.setString(1, departmentId);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("2º Empleado del departamento {} que más cobra: {} {}€",
                    departmentId,
                    employees.getString("Name"),
                    employees.getString("MaxSalary"));
        }
    }

    /**
     * Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
     *
     * @param connection
     * @throws SQLException
     */
    private static void selectEmployeesHiredAtMonth(Connection connection, String month) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT count(*) as 'Total'\n" +
                        "from employees\n" +
                        "Where Month(employees.hire_date) = ?;");
        selectEmployees.setString(1, month);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleados contratados en el mes {} : {}",
                    month,
                    employees.getString("Total"));
        }
    }
}
