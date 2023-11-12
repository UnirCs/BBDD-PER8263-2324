package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Slf4j
public class MySqlApplication {

    private static final String HOST = "localhost";
    private static final String DATABASE = "employees";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 3306
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Connection connection = new MySqlConnector(HOST, DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos MySql");

            // Obtener el número de hombres y mujeres de la base de datos.
            // Ordenar de forma descendente.
            select_01(connection);

            // Mostrar el nombre, apellido y salario de la persona mejor pagada
            // de un departamento concreto.
            select_02(connection, "d007");

            //Mostrar el nombre, apellido y salario de la segunda persona mejor pagada
            // de un departamento concreto.
            select_03(connection, "d007");

            //Mostrar el número de empleados contratados en un mes concreto.
            select_04(connection, 11);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * 01 Obtener el número de hombres y mujeres de la base de datos.
     *    Ordenar de forma descendente.
     *
     * @param connection conexión
     * @throws SQLException
     */
    private static void select_01(Connection connection) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT e.gender, COUNT(e.gender) count\n" +
                        "FROM employees e\n" +
                        "GROUP BY e.gender\n" +
                        "ORDER BY count DESC");
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Número de Empleados {}: {}",
                    employees.getString("gender").equals("M") ? "Hombres" : "Mujeres",
                    employees.getInt("count"));
        }
    }

    /**
     * 02 Mostrar el nombre, apellido y salario de la persona mejor pagada
     *    de un departamento concreto.
     *
     * @param connection conexión
     * @param department departamento
     * @throws SQLException
     */
    private static void select_02(Connection connection, String department) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT e.first_name, e.last_name, s.salary max_salary\n" +
                        "FROM employees e\n" +
                        "INNER JOIN salaries s ON e.emp_no = s.emp_no\n" +
                        "INNER JOIN dept_emp dr ON e.emp_no = dr.emp_no\n" +
                        "WHERE dr.dept_no = ?\n" +
                        "ORDER BY max_salary DESC\n" +
                        "LIMIT 1");
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Persona mejor pagada del departamento {}: {} {}, salario: {}",
                    department,
                    employees.getString("first_name"),
                    employees.getString("last_name"),
                    employees.getInt("max_salary"));
        }
    }

    /**
     * 03 Mostrar el nombre, apellido y salario de la segunda persona mejor pagada
     *    de un departamento concreto.
     *
     * @param connection conexión
     * @param department departamento
     * @throws SQLException
     */
    private static void select_03(Connection connection, String department) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT e.first_name, e.last_name, MAX(s.salary) max_salary\n" +
                        "FROM employees e\n" +
                        "INNER JOIN salaries s ON e.emp_no = s.emp_no\n" +
                        "INNER JOIN dept_emp dr ON e.emp_no = dr.emp_no\n" +
                        "WHERE dr.dept_no = ?\n" +
                        "GROUP BY e.emp_no\n" +
                        "ORDER BY max_salary DESC\n" +
                        "LIMIT 1 OFFSET 1");
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Segunda persona mejor pagada del departamento {}: {} {}, salario: {}",
                    department,
                    employees.getString("first_name"),
                    employees.getString("last_name"),
                    employees.getInt("max_salary"));
        }
    }

    /**
     * 04 Mostrar el número de empleados contratados en un mes concreto.
     *
     * @param connection conexión
     * @param month      mes
     * @throws SQLException
     */
    private static void select_04(Connection connection, int month) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT MONTH(e.hire_date) month, COUNT(e.emp_no) AS count\n" +
                        "FROM employees e\n" +
                        "WHERE MONTH(e.hire_date) = ?\n" +
                        "GROUP BY month");
        selectEmployees.setInt(1, month);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Número de empleados contratados en todos los meses de {}: {}",
                    Month.of(month).getDisplayName(TextStyle.FULL, new Locale("es", "ES")),
                    employees.getInt("count"));
        }
    }
}