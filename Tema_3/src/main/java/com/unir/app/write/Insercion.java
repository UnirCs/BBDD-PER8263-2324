package com.unir.app.write;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class Insercion {

    public static void main(String[] args) {
        // Configuración de conexión a la base de datos
        MySqlConnector mySqlConnector = new MySqlConnector("localhost", "employees");

        try (Connection connection = mySqlConnector.getConnection()) {
            // Ingestión de departamentos
            processCSV("departamento.csv", "departments", connection);

            // Ingestión de empleados
            processCSV("unirEmployees2.csv", "employees", connection);
        } catch (SQLException e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    private static void processCSV(String csvFileName, String tableName, Connection connection) {
        try {
            // Deshabilitar el autocommit para gestionar las transacciones
            connection.setAutoCommit(false);

            // Leer el archivo CSV
            try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length > 0) {
                        // Verificar si el registro ya existe en la base de datos
                        if (recordExists(connection, tableName, data[0])) {
                            // Actualizar el registro
                            updateRecord(connection, tableName, data);
                        } else {
                            // Insertar el nuevo registro
                            insertRecord(connection, tableName, data);
                        }
                    }
                }

                // Commit al finalizar la operación
                connection.commit();

            } catch (Exception e) {
                // En caso de error, realizar rollback
                connection.rollback();
                log.error("Error al procesar el archivo CSV", e);
            }

        } catch (SQLException e) {
            log.error("Error en la conexión a la base de datos", e);
        }
    }

    private static boolean recordExists(Connection connection, String tableName, String id) {
        try {
            String selectSql = "SELECT COUNT(*) FROM " + tableName + " WHERE id_column = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setString(1, id);
                ResultSet resultSet = selectStatement.executeQuery();
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            log.error("Error al verificar si el registro existe", e);
            return false;
        }
    }

    private static void insertRecord(Connection connection, String tableName, String[] data) {
        try {
            String insertSql = "INSERT INTO " + tableName + " (column1, column2, ...) VALUES (?, ?, ...)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                // Configura los parámetros según las columnas de tu tabla
                insertStatement.setString(1, data[0]);
                insertStatement.setString(2, data[1]);
                // ... (configura los demás parámetros)

                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Error al insertar el nuevo registro", e);
        }
    }

    private static void updateRecord(Connection connection, String tableName, String[] data) {
        // Implementa la lógica para actualizar un registro en la base de datos
        // Utiliza la clase MySqlEmployee para establecer los parámetros en el PreparedStatement
    }
}
