package com.unir.app.write;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.unir.config.MySqlConnector;
import com.unir.model.MySqlEmployee_Department;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;


@Slf4j
public class MySqlDept_EmptIntake {

    private static final String DATABASE = "employees";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos MySQL");

            // Leemos los datos del fichero CSV
            List<MySqlEmployee_Department> departamentos = readData();

            // Introducimos los datos en la base de datos
            intake(connection, departamentos);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Lee los datos del fichero CSV y los devuelve en una lista de departamentos.
     * El fichero CSV debe estar en la raíz del proyecto.
     *
     * @return - Lista de departamentos
     */
    private static List<MySqlEmployee_Department> readData() {

        // Try-with-resources. Se cierra el reader automáticamente al salir del bloque try
        // CSVReader nos permite leer el fichero CSV linea a linea
        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader("UnirEmpleadosDepartamentos.csv"))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(',').build()).build()) {

            // Creamos la lista de empleados y el formato de fecha
            List<MySqlEmployee_Department> departamentos = new LinkedList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            // Saltamos la primera linea, que contiene los nombres de las columnas del CSV
            reader.skip(1);
            String[] nextLine;

            // Leemos el fichero linea a linea
            while ((nextLine = reader.readNext()) != null) {

                // Creamos el departamento y lo añadimos a la lista
                MySqlEmployee_Department departamento = new MySqlEmployee_Department(
                        Integer.parseInt(nextLine[0]),
                        nextLine[6],
                        new Date(format.parse(nextLine[7]).getTime()),
                        new Date(format.parse(nextLine[8]).getTime())
                );
                departamentos.add(departamento);
            }
            return departamentos;
        } catch (IOException e) {
            log.error("Error al leer el fichero CSV", e);
            throw new RuntimeException(e);
        } catch (CsvValidationException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Introduce los datos en la base de datos.
     * Si el departamento ya existe, se actualiza.
     * Si no existe, se inserta.
     * <p>
     * Toma como referencia el campo emp_no para determinar si el departamento existe o no.
     *
     * @param connection    - Conexión a la base de datos
     * @param departamentos - Lista de departamentos
     * @throws SQLException - Error al ejecutar la consulta
     */
    private static void intake(Connection connection, List<MySqlEmployee_Department> departamentos) throws SQLException {

        String selectSql = "SELECT COUNT(*) FROM dept_emp WHERE emp_no = ? AND dept_no = ?";
        String insertSql = "INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date) "
                + "VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE dept_emp SET from_date = ? AND to_date = ? WHERE emp_no = ? AND dept_no = ?";
        int lote = 5;
        int contador = 0;

        // Preparamos las consultas, una unica vez para poder reutilizarlas en el batch
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        PreparedStatement updateStatement = connection.prepareStatement(updateSql);

        // Desactivamos el autocommit para poder ejecutar el batch y hacer commit al final
        connection.setAutoCommit(false);

        for (MySqlEmployee_Department departamento : departamentos) {

            // Comprobamos si el departamento existe
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, departamento.getEmp_no()); // Código del empleado
            selectStatement.setString(2, departamento.getDept_no()); // Código del departamento
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next(); // Nos movemos a la primera fila
            int rowCount = resultSet.getInt(1);

            // Si existe, actualizamos. Si no, insertamos
            if (rowCount > 0) {
                fillUpdateStatement(updateStatement, departamento);
                updateStatement.addBatch();
            } else {
                fillInsertStatement(insertStatement, departamento);
                insertStatement.addBatch();
            }

            // Ejecutamos el batch cada lote de registros
            if (++contador % lote == 0) {
                updateStatement.executeBatch();
                insertStatement.executeBatch();
            }
        }

        // Ejecutamos el batch final
        insertStatement.executeBatch();
        updateStatement.executeBatch();

        // Hacemos commit y volvemos a activar el autocommit
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta INSERT.
     *
     * @param statement    - PreparedStatement
     * @param departamento - departamento
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillInsertStatement(PreparedStatement statement, MySqlEmployee_Department departamento) throws SQLException {
        statement.setInt(1, departamento.getEmp_no());
        statement.setString(2, departamento.getDept_no());
        statement.setDate(3, departamento.getFrom_date());
        statement.setDate(4, departamento.getTo_date());
    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta UPDATE.
     *
     * @param statement    - PreparedStatement
     * @param departamento - departamento
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillUpdateStatement(PreparedStatement statement, MySqlEmployee_Department departamento) throws SQLException {
        statement.setDate(1, departamento.getFrom_date());
        statement.setDate(2, departamento.getTo_date());
        statement.setInt(3, departamento.getEmp_no());
        statement.setString(4, departamento.getDept_no());
    }
}