package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MySqlApplication {

    private static final String DATABASE = "employees";


    /**
     * Departamentos disponibles para la segunda y tercera consulta
     * -- Customer Service
     * -- Development
     * -- Finance
     * -- Human Resources
     * -- Marketing
     * -- Production
     * -- Quality Management
     * -- Research
     * -- Sales
     */
    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos MySQL");

            log.info("Primera consulta:");
            selectManWomanCount(connection);
            log.info("Segunda consulta:");
            selectNameLastSalaryBest(connection, "Sales");
            log.info("Tercera consulta");
            selectNameLastSalarySecondBest(connection, "Sales");
            log.info("Cuarta consulta");
            selectHiredOnMonth(connection, 1);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }


    private static void selectManWomanCount(Connection connection) throws SQLException{
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT gender AS Genero, COUNT(gender) AS Cantidad \n" +
                        "FROM employees \n" +
                        "GROUP BY gender \n" +
                        "ORDER BY Cantidad DESC;");
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleados totales {}: {}",
                    employees.getString("Genero"),
                    employees.getString("Cantidad"));
        }
    }

    private static void selectNameLastSalaryBest(Connection connection, String department) throws SQLException{
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT em.first_name, em.last_name, sa.salary \n" +
                        "FROM salaries as sa\n" +
                        "JOIN employees as em ON sa.emp_no = em.emp_no \n" +
                        "where sa.emp_no IN (\n" +
                        "\tSELECT e.emp_no\n" +
                        "\tFROM employees as e\n" +
                        "\tJOIN dept_emp as de ON e.emp_no = de.emp_no\n" +
                        "\tJOIN departments as d ON d.dept_no = de.dept_no\n" +
                        "\tWHERE d.dept_name = ?)\n" +
                        "ORDER BY sa.salary DESC\n" +
                        "LIMIT 1;\n"
                );
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleado mejor pagado del departamento {}: {} {} - Cobrando: {}",
                    department,
                    employees.getString("first_name"),
                    employees.getString("last_name"),
                    employees.getString("salary")
                    );
        }

    }

    private static void selectNameLastSalarySecondBest(Connection connection, String department) throws SQLException{

        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT em.first_name, em.last_name, sa.salary \n" +
                        "FROM salaries as sa\n" +
                        "JOIN employees as em ON sa.emp_no = em.emp_no \n" +
                        "where sa.emp_no IN (\n" +
                        "\tSELECT e.emp_no\n" +
                        "\tFROM employees as e\n" +
                        "\tJOIN dept_emp as de ON e.emp_no = de.emp_no\n" +
                        "\tJOIN departments as d ON d.dept_no = de.dept_no\n" +
                        "\tWHERE d.dept_name = ?)\n" +
                        "ORDER BY sa.salary DESC\n" +
                        "LIMIT 1, 1;"
        );
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Segundo empleado mejor pagado del departamento {}: {} {} - Cobrando: {}",
                    department,
                    employees.getString("first_name"),
                    employees.getString("last_name"),
                    employees.getString("salary")
            );
        }

    }

    private static void selectHiredOnMonth(Connection connection, int month) throws SQLException{
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT COUNT(*) AS Cantidad\n" +
                        "FROM employees as e\n" +
                        "WHERE DATE_FORMAT(hire_date, '%m') = ?"
        );
        selectEmployees.setInt(1, month);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleados contratados en el mes {}: {}",
                    month,
                    employees.getString("Cantidad")
            );
        }
    }
}
