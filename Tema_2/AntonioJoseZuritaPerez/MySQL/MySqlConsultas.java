package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MySqlConsultas {
    private static final String DATABASE = "employees";

    public static void main(String[] args) {
        try (Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {
            log.info("Conexión establecida con la base de datos Oracle");

            numeroNomMug(connection);
            mejorPagadoDepartamento(connection, "d001");
            segundaMejorPagadoDepartamento(connection, "d001");
            numeroEmpleadosMes(connection, 1, 1990);


        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /*
    Consulta 1: -- 1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.

    SELECT gender, COUNT(*) as Total
    FROM employees
    GROUP BY gender
    ORDER BY Total DESC;

     @param connection
    @throws SQLException
     */

    private static void numeroNomMug(Connection connection) throws SQLException {
        Statement selectGender = connection.createStatement();
        ResultSet gender = selectGender.executeQuery("SELECT gender, COUNT(*) as Total FROM employees GROUP BY gender ORDER BY Total DESC");

        while (gender.next()) {
            log.info("Género: {}, Total: {}", gender.getString("gender"), gender.getInt("Total"));
        }
    }


    /*
    Consulta 2: -- 2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
SELECT e.first_name, e.last_name, s.salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp de ON e.emp_no = de.emp_no
WHERE de.dept_no  = ? -- parámetro variable
AND s.from_date <= current_date  -- Aseguran que se estén considerando solo los salarios y asignaciones actuales
AND s.to_date >= current_date
AND de.from_date <= current_date
AND de.to_date >= current_date
ORDER BY s.salary DESC
LIMIT 1;
     */
    private static void mejorPagadoDepartamento(Connection connection, String department) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT e.first_name, e.last_name, s.salary\n" +
                "FROM employees e\n" +
                "JOIN salaries s ON e.emp_no = s.emp_no\n" +
                "JOIN dept_emp de ON e.emp_no = de.emp_no\n" +
                "WHERE de.dept_no  = ? -- parámetro variable\n" +
                "AND s.from_date <= current_date  -- Aseguran que se estén considerando solo los salarios y asignaciones actuales\n" +
                "AND s.to_date >= current_date\n" +
                "AND de.from_date <= current_date\n" +
                "AND de.to_date >= current_date\n" +
                "ORDER BY s.salary DESC\n" +
                "LIMIT 1;");

        stmt.setString(1, department);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            log.info("Nombre: {}, Apellido: {}, Salario: {}", rs.getString("first_name"), rs.getString("last_name"), rs.getFloat("salary"));
        }
    }



    /*
    Consulta 3: -- Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
SELECT e.first_name, e.last_name, s.salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp de ON e.emp_no = de.emp_no
WHERE de.dept_no  = ? -- parámetro variable
AND s.from_date <= current_date  -- Aseguran que se estén considerando solo los salarios y asignaciones actuales
AND s.to_date >= current_date
AND de.from_date <= current_date
AND de.to_date >= current_date
ORDER BY s.salary DESC
LIMIT 1 OFFSET 1; -- Selecciona el segundo registro después de ordenar los resultados por salario de forma descendente. OFFSET 1 omite el primer regfistro
     */

    private static void segundaMejorPagadoDepartamento(Connection connection, String department) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT e.first_name, e.last_name, s.salary\n" +
                "FROM employees e\n" +
                "JOIN salaries s ON e.emp_no = s.emp_no\n" +
                "JOIN dept_emp de ON e.emp_no = de.emp_no\n" +
                "WHERE de.dept_no  = ? -- parámetro variable\n" +
                "AND s.from_date <= current_date  -- Aseguran que se estén considerando solo los salarios y asignaciones actuales\n" +
                "AND s.to_date >= current_date\n" +
                "AND de.from_date <= current_date\n" +
                "AND de.to_date >= current_date\n" +
                "ORDER BY s.salary DESC\n" +
                "LIMIT 1 OFFSET 1;");

        stmt.setString(1, department);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            log.info("Nombre: {}, Apellido: {}, Salario: {}", rs.getString("first_name"), rs.getString("last_name"), rs.getFloat("salary"));
        }
    }

    /*

    Consulta 4: -- Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
SELECT COUNT(*) as Total
FROM employees
WHERE MONTH(hire_date) = ? -- parámetro variable
AND YEAR(hire_date) = ?; -- parámetro variable
     */

    private static void numeroEmpleadosMes(Connection connection, int mes, int año) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) as Total\n" +
                "FROM employees\n" +
                "WHERE MONTH(hire_date) = ? -- parámetro variable\n" +
                "AND YEAR(hire_date) = ?; -- parámetro variable");

        stmt.setInt(1, mes);
        stmt.setInt(2, año);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            log.info("Total: {}", rs.getInt("Total"));
        }


    }
}



