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

            log.info("Conexión establecida con la base de datos Mysql");
            selectNumeroHombresMujeres(connection);
            selectMejorPagadoDepartamento(connection, "d005");
            selectSegundoMejorPagadoDepartamento(connection, "d005");
            selectEmpleadosContratadosMes(connection, "11");

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando PreparedStatement.
     * PreparedStatement es la forma más segura de ejecutar consultas a la base de datos.
     * Se protege de ataques de inyección SQL.
     * Es útil para sentencias DML.
     * @param connection
     * @throws SQLException
     **/
    private static void selectNumeroHombresMujeres(Connection connection) throws SQLException {
        String sql = "SELECT e.gender, COUNT(*) AS count " +
                "FROM employees e " +
                "GROUP BY e.gender " +
                "ORDER BY count DESC";
        PreparedStatement selectStatement = connection.prepareStatement(sql);
        ResultSet result = selectStatement.executeQuery();

        while (result.next()) {
            log.debug("Género: {} - Total {}",
                    result.getString("gender"),
                    result.getString("count"));
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando PreparedStatement.
     * PreparedStatement es la forma más segura de ejecutar consultas a la base de datos.
     * Se protege de ataques de inyección SQL.
     * Es útil para sentencias DML.
     * @param connection
     * @throws SQLException
     **/
    private static void selectMejorPagadoDepartamento(Connection connection, String departamento) throws SQLException {
        String sql = "SELECT e.first_name, e.last_name, s.salary FROM salaries s\n" +
                "JOIN employees e on e.emp_no = s.emp_no\n" +
                "JOIN dept_emp de on e.emp_no = de.emp_no\n" +
                "where de.dept_no =  ?\n" +
                "order by s.salary DESC\n" +
                "LIMIT 1";
        PreparedStatement selectStatement = connection.prepareStatement(sql);
        selectStatement.setString(1,departamento);
        ResultSet result = selectStatement.executeQuery();

        while (result.next()) {
            log.debug("Mejor pagada del departamento {}: {} {}, Salario: {}",
                    departamento,
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getString("salary"));
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando PreparedStatement.
     * PreparedStatement es la forma más segura de ejecutar consultas a la base de datos.
     * Se protege de ataques de inyección SQL.
     * Es útil para sentencias DML.
     * @param connection
     * @throws SQLException
     **/
    private static void selectSegundoMejorPagadoDepartamento(Connection connection, String departamento) throws SQLException {
        String sql = "SELECT e.first_name, e.last_name, s.salary FROM salaries s\n" +
                "JOIN employees e on e.emp_no = s.emp_no\n" +
                "JOIN dept_emp de on e.emp_no = de.emp_no\n" +
                "where de.dept_no =  ?\n" +
                "order by s.salary DESC\n" +
                "LIMIT 1 OFFSET 1";
        PreparedStatement selectStatement = connection.prepareStatement(sql);
        selectStatement.setString(1,departamento);
        ResultSet result = selectStatement.executeQuery();

        while (result.next()) {
            log.debug("Segundo mejor pagada del departamento {}: {} {}, Salario: {}",
                    departamento,
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getString("salary"));
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando PreparedStatement.
     * PreparedStatement es la forma más segura de ejecutar consultas a la base de datos.
     * Se protege de ataques de inyección SQL.
     * Es útil para sentencias DML.
     * @param connection
     * @throws SQLException
     **/
    private static void selectEmpleadosContratadosMes(Connection connection, String mes) throws SQLException {
        String sql = "SELECT count(*) AS total_contract FROM employees e\n" +
                "where month(e.hire_date)  =  ?";
        PreparedStatement selectStatement = connection.prepareStatement(sql);
        selectStatement.setString(1,mes);
        ResultSet result = selectStatement.executeQuery();

        while (result.next()) {
            log.debug("Número empleados contratados en el mes {}: {}",
                    mes,
                    result.getString("total_contract"));
        }
    }
}
