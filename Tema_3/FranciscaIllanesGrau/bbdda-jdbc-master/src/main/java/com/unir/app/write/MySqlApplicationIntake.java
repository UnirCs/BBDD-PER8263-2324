package com.unir.app.write;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.unir.config.MySqlConnector;
import com.unir.model.MySqlDepartment;
import com.unir.model.MySqlEmployee;
import lombok.extern.slf4j.Slf4j;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;

/**
 * La version para Oracle seria muy similar a esta, cambiando únicamente el Driver y los datos de sentencias.
 * La tabla de Oracle contiene muchas restricciones y triggers. Por simplicidad, usamos MySQL en este caso.
 */
@Slf4j
public class MySqlApplicationIntake {

    private static final String DATABASE = "employees";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos MySQL");

            // Procesar departamentos
            List<MySqlDepartment> departments = readDepartmentsData("new_departments.csv");
            intakeDepartments(connection, departments);

            // Procesar empleados
            List<MySqlEmployee> employees = readData("new_employees.csv");
            intake(connection, employees);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Lee los datos del fichero CSV y los devuelve en una lista de empleados.
     * El fichero CSV debe estar en la raíz del proyecto.
     *
     * @return - Lista de empleados
     */

    private static List<MySqlDepartment> readDepartmentsData(String csvFile) {
        // Aquí va la implementación para leer los datos del archivo CSV de departamentos
        List<MySqlDepartment> departments = new LinkedList<>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile))
                .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build()) {

            String[] nextLine;
            reader.skip(1); // Omitir la cabecera del CSV

            while ((nextLine = reader.readNext()) != null) {
                String departmentNo = nextLine[0].trim();
                String departmentName = nextLine[1].trim();
                MySqlDepartment department = new MySqlDepartment(departmentNo, departmentName);
                departments.add(department);
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Error al leer el fichero CSV", e);
            throw new RuntimeException(e);
        }
        return departments;

    }

    private static void intakeDepartments(Connection connection, List<MySqlDepartment> departments) {
        // Aquí va la implementación para insertar o actualizar los datos de los departamentos
        String selectSql = "SELECT COUNT(*) FROM departments WHERE dept_no = ?";
        String insertSql = "INSERT INTO departments (dept_no, dept_name) VALUES (?, ?)";
        String updateSql = "UPDATE departments SET dept_name = ? WHERE dept_no = ?";

        try {
            // Preparar las sentencias SQL para reutilizarlas en el bucle
            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            PreparedStatement updateStmt = connection.prepareStatement(updateSql);

            // Desactivar el autocommit para manejar transacciones
            connection.setAutoCommit(false);

            for (MySqlDepartment department : departments) {
                selectStmt.setString(1, department.getDeptNo());
                try (ResultSet resultSet = selectStmt.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        updateStmt.setString(1, department.getDeptName());
                        updateStmt.setString(2, department.getDeptNo());
                        updateStmt.executeUpdate();
                    } else {
                        insertStmt.setString(1, department.getDeptNo());
                        insertStmt.setString(2, department.getDeptName());
                        insertStmt.executeUpdate();
                    }
                }
            }

            // Hacer commit de todas las operaciones
            connection.commit();
        } catch (SQLException e) {
            log.error("Error al insertar o actualizar departamentos", e);
            try {
                connection.rollback(); // En caso de error, revertir los cambios
            } catch (SQLException ex) {
                log.error("Error al revertir los cambios", ex);
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Restablecer el comportamiento predeterminado
                }
            } catch (SQLException e) {
                log.error("Error al restablecer el autocommit", e);
            }
        }

    }

    private static List<MySqlEmployee> readData(String csvFile) {

        // Try-with-resources. Se cierra el reader automáticamente al salir del bloque try
        // CSVReader nos permite leer el fichero CSV linea a linea
        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader("new_employees.csv"))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(',').build()).build()) {

            // Creamos la lista de empleados y el formato de fecha
            List<MySqlEmployee> employees = new LinkedList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");

            // Saltamos la primera linea, que contiene los nombres de las columnas del CSV
            reader.skip(1);
            String[] nextLine;

            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            while ((nextLine = reader.readNext()) != null) {
                int empNo = Integer.parseInt(nextLine[0].trim());
                java.util.Date utilBirthDate = format.parse(nextLine[1].trim());
                String firstName = nextLine[2].trim();
                String lastName = nextLine[3].trim();
                String gender = nextLine[4].trim();
                java.util.Date utilHireDate = format.parse(nextLine[5].trim());

                java.sql.Date sqlBirthDate = new java.sql.Date(utilBirthDate.getTime());
                java.sql.Date sqlHireDate = new java.sql.Date(utilHireDate.getTime());

                MySqlEmployee employee = new MySqlEmployee(empNo, firstName, lastName, gender, sqlHireDate, sqlBirthDate);
                employees.add(employee);
            }

            return employees;
        } catch (IOException e) {
            log.error("Error al leer el fichero CSV", e);
            throw new RuntimeException(e);
        } catch (CsvValidationException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Introduce los datos en la base de datos.
     * Si el empleado ya existe, se actualiza.
     * Si no existe, se inserta.
     *
     * Toma como referencia el campo emp_no para determinar si el empleado existe o no.
     * @param connection - Conexión a la base de datos
     * @param employees - Lista de empleados
     * @throws SQLException - Error al ejecutar la consulta
     */
    private static void intake(Connection connection, List<MySqlEmployee> employees) throws SQLException {

        String selectSql = "SELECT COUNT(*) FROM employees WHERE emp_no = ?";
        String insertSql = "INSERT INTO employees (emp_no, first_name, last_name, gender, hire_date, birth_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE employees SET first_name = ?, last_name = ?, gender = ?, hire_date = ?, birth_date = ? WHERE emp_no = ?";
        int lote = 5;
        int contador = 0;

        // Preparamos las consultas, una unica vez para poder reutilizarlas en el batch
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        PreparedStatement updateStatement = connection.prepareStatement(updateSql);

        // Desactivamos el autocommit para poder ejecutar el batch y hacer commit al final
        connection.setAutoCommit(false);

        for (MySqlEmployee employee : employees) {

            // Comprobamos si el empleado existe
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, employee.getEmployeeId()); // Código del empleado
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next(); // Nos movemos a la primera fila
            int rowCount = resultSet.getInt(1);

            // Si existe, actualizamos. Si no, insertamos
            if(rowCount > 0) {
                fillUpdateStatement(updateStatement, employee);
                updateStatement.addBatch();
            } else {
                fillInsertStatement(insertStatement, employee);
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
     * @param statement - PreparedStatement
     * @param employee - Empleado
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillInsertStatement(PreparedStatement statement, MySqlEmployee employee) throws SQLException {
        statement.setInt(1, employee.getEmployeeId());
        statement.setString(2, employee.getFirstName());
        statement.setString(3, employee.getLastName());
        statement.setString(4, employee.getGender());
        statement.setDate(5, employee.getHireDate());
        statement.setDate(6, employee.getBirthDate());

    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta UPDATE.
     *
     * @param statement - PreparedStatement
     * @param employee - Empleado
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillUpdateStatement(PreparedStatement statement, MySqlEmployee employee) throws SQLException {
        statement.setString(1, employee.getFirstName());
        statement.setString(2, employee.getLastName());
        statement.setString(3, employee.getGender());
        statement.setDate(4, employee.getHireDate());
        statement.setDate(5, employee.getBirthDate());
        statement.setInt(6, employee.getEmployeeId());
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
        String selectSql = "SELECT MAX(?) FROM ?";
        PreparedStatement selectStatement = connection.prepareStatement(selectSql);
        selectStatement.setString(1, fieldName);
        selectStatement.setString(2, table);
        ResultSet resultSet = selectStatement.executeQuery();
        resultSet.next(); // Nos movemos a la primera fila
        return resultSet.getInt(1);
    }
}
