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

            log.info("Conexión establecida con la base de datos MySql");

            selectGenderCount(connection);
            selectTopPaidEmployeeOfDepartment(connection, "d008");
            selectTopPaidEmployeeOfDepartment(connection, "d008");
            selectSecondTopPaidEmployeeOfDepartment(connection, "d008");
            selectEmployeesHiredInMonth(connection, 11);  // Por ejemplo, para nov

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Consulta a la base de datos usando PreparedStatement.
     * PreparedStatement es la forma más segura de ejecutar consultas a la base de datos.
     * Se protege de ataques de inyección SQL.
     * Es útil para sentencias DML.
     * @param connection
     * @throws SQLException
     */

    private static void selectGenderCount(Connection connection) throws SQLException {
        String sqlGenderCount = "SELECT gender, COUNT(*) as total " +
                "FROM employees " +
                "GROUP BY gender " +
                "ORDER BY total DESC";

        PreparedStatement selectGenderStatement = connection.prepareStatement(sqlGenderCount);
        ResultSet genderResult = selectGenderStatement.executeQuery();
        while (genderResult.next()) {
            log.debug("Género: {}, Total: {}",
                    genderResult.getString("gender"),
                    genderResult.getInt("total"));
        }
    }

    private static void selectTopPaidEmployeeOfDepartment(Connection connection, String department) throws SQLException {
        // Definir la consulta SQL con un marcador de posición para el parámetro
        String sql = "SELECT e.first_name, e.last_name, s.salary " +
                "FROM employees e " +
                "JOIN salaries s ON e.emp_no = s.emp_no " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no " +  // Unir con dept_emp para obtener el departamento
                "WHERE de.dept_no = ? " +  // Filtrar por el número de departamento
                "ORDER BY s.salary DESC " +
                "LIMIT 1";

        PreparedStatement selectTopPaidEmployee = connection.prepareStatement(sql);

        // Establecer el valor del parámetro
        selectTopPaidEmployee.setString(1, department);

        // Ejecutar la consulta y procesar los resultados
        ResultSet result = selectTopPaidEmployee.executeQuery();
        while (result.next()) {
            log.debug("Empleado mejor pagado del departamento {}: {} {}, Salario: {}",
                    department,
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getDouble("salary"));
        }
    }

    private static void selectSecondTopPaidEmployeeOfDepartment(Connection connection, String department) throws SQLException {
        String sql2 = "SELECT e.first_name, e.last_name, s.salary " +
                "FROM employees e " +
                "JOIN salaries s ON e.emp_no = s.emp_no " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                "WHERE de.dept_no = ? " +
                "ORDER BY s.salary DESC " +
                "LIMIT 1 OFFSET 1";

        PreparedStatement selectSecondTopPaidEmployee = connection.prepareStatement(sql2);
        selectSecondTopPaidEmployee.setString(1, department);
        ResultSet result2 = selectSecondTopPaidEmployee.executeQuery();
        while (result2.next()) {
            log.debug("Segundo empleado mejor pagado del departamento {}: {} {}, Salario: {}",
                    department,
                    result2.getString("first_name"),
                    result2.getString("last_name"),
                    result2.getDouble("salary"));
        }
    }

    private static void selectEmployeesHiredInMonth(Connection connection, int month) throws SQLException {
        String sql3 = "SELECT COUNT(*) as total_employees " +
                "FROM employees " +
                "WHERE MONTH(hire_date) = ?";

        PreparedStatement selectEmployeesInMonth = connection.prepareStatement(sql3);
        selectEmployeesInMonth.setInt(1, month);
        ResultSet result3 = selectEmployeesInMonth.executeQuery();
        if (result3.next()) {
            log.debug("Número de empleados contratados en el mes {}: {}",
                    month,
                    result3.getInt("total_employees"));
        }
    }
}