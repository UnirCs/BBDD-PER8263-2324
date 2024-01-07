package com.unir.app.write;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.unir.config.MySqlConnector;
import com.unir.model.MySqlEmployee;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    private static final String DATABASE = "employees";

    public static void main(String[] args) {
        try (Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {
            log.info("Conexión establecida con la base de datos MySQL");

            // Inserto un único empleado
            insertSingleEmployee(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Inserta un único empleado en la base de datos.
     *
     * @param connection - Conexión a la base de datos
     * @throws SQLException - Error al ejecutar la consulta
     * @throws ParseException - Error al parsear la fecha
     */
    private static void insertSingleEmployee(Connection connection) throws SQLException, ParseException {
        String insertSql = "INSERT INTO employees (emp_no, first_name, last_name, gender, hire_date, birth_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        // Obtengo el siguiente ID disponible (simplemente incrementando el último ID)
        int nextEmployeeId = lastId(connection, "employees", "emp_no") + 1;

        // Creo el empleado de ejemplo
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = format.parse("1990-01-01");
        Date hireDate = format.parse("2023-01-01");

        // Preparo la consulta de inserción
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            insertStatement.setInt(1, nextEmployeeId);
            insertStatement.setString(2, "John");
            insertStatement.setString(3, "Doe");
            insertStatement.setString(4, "M");
            insertStatement.setDate(5, new java.sql.Date(hireDate.getTime()));
            insertStatement.setDate(6, new java.sql.Date(birthDate.getTime()));

            // Ejecuto la consulta de inserción
            insertStatement.executeUpdate();

            log.info("Empleado insertado correctamente");
        }
    }

    /**
     * Devuelve el último id de una columna de una tabla.
     * Util para obtener el siguiente id a insertar.
     *
     * @param connection - Conexión a la base de datos
     * @param table - Nombre de la tabla
     * @param fieldName - Nombre de la columna
     * @return - Último id de la columna
     * @throws SQLException - Error al ejecutar la consulta
     */
    private static int lastId(Connection connection, String table, String fieldName) throws SQLException {
        String selectSql = "SELECT MAX(" + fieldName + ") FROM " + table;
        try (PreparedStatement selectStatement = connection.prepareStatement(selectSql);
             ResultSet resultSet = selectStatement.executeQuery()) {
            resultSet.next(); // Nos movemos a la primera fila
            return resultSet.getInt(1);
        }
    }
}
