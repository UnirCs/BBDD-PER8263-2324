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

            // Leemos los datos del fichero CSV de departamentos
            List<MySqlDepartment> departments = readDepartamentData();

            // Introducimos los datos en la base de datos
            intakeDepartments(connection, departments);

            // Leemos los datos del fichero CSV
            List<MySqlEmployee> employees = readEmployeeData();

            // Introducimos los datos en la base de datos
            MySqlApplicationIntake.intakeEmployees(connection, employees);




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
    private static List<MySqlEmployee> readEmployeeData() {

        // Try-with-resources. Se cierra el reader automáticamente al salir del bloque try
        // CSVReader nos permite leer el fichero CSV linea a linea
        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader("unirEmployees.csv"))
                .withCSVParser(new CSVParserBuilder()
                .withSeparator(',').build()).build()) {

            // Creamos la lista de empleados y el formato de fecha
            List<MySqlEmployee> employees = new LinkedList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");

            // Saltamos la primera linea, que contiene los nombres de las columnas del CSV
            reader.skip(1);
            String[] nextLine;

            // Leemos el fichero linea a linea
            while((nextLine = reader.readNext()) != null) {
                // Creamos el empleado y lo añadimos a la lista
                MySqlEmployee employee = new MySqlEmployee(
                        Integer.parseInt(nextLine[0]),
                        nextLine[1],
                        nextLine[2],
                        nextLine[3],
                        new Date(format.parse(nextLine[4]).getTime()),
                        new Date(format.parse(nextLine[5]).getTime()),
                        nextLine[6],
                        new Date(format.parse(nextLine[7]).getTime()),
                        new Date(format.parse(nextLine[8]).getTime())
                );
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
    private static void intakeEmployees(Connection connection, List<MySqlEmployee> employees) throws SQLException {

        // Queries para el empleado
        String selectSql = "SELECT COUNT(*) FROM employees WHERE emp_no = ?";
        String insertSql = "INSERT INTO employees (emp_no, first_name, last_name, gender, hire_date, birth_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE employees SET first_name = ?, last_name = ?, gender = ?, hire_date = ?, birth_date = ? WHERE emp_no = ?";

        // Queries para la relación de empleado y departamento
        String selectSqlDeptEmp = "SELECT COUNT(*) FROM dept_emp WHERE dept_no = ? and emp_no = ?";
        String insertSqlDeptEmp = "INSERT INTO dept_emp (dept_no, emp_no, from_date, to_date) "
                + "VALUES (?, ?, ?, ?)";
        String updateSqlDeptEmp = "UPDATE dept_emp SET from_date = ?, to_date = ? WHERE dept_no = ? and  emp_no = ?";


        int lote = 5;
        int contador = 0;

        // Preparamos las consultas, una unica vez para poder reutilizarlas en el batch
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        PreparedStatement updateStatement = connection.prepareStatement(updateSql);
        PreparedStatement insertStatementDeptEmp = connection.prepareStatement(insertSqlDeptEmp);
        PreparedStatement updateStatementDeptEmp = connection.prepareStatement(updateSqlDeptEmp);


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
                fillUpdateStatementEmployee(updateStatement, employee);
                updateStatement.addBatch();
            } else {
                fillInsertStatementEmployee(insertStatement, employee);
                insertStatement.addBatch();
            }


            // Ahora se comprueba si existe la relación de empleado y departamento
            PreparedStatement selectStatementDeptEmp = connection.prepareStatement(selectSqlDeptEmp);
            selectStatementDeptEmp.setString(1, employee.getDepartmentId()); // Código del departamento
            selectStatementDeptEmp.setInt(2, employee.getEmployeeId()); // Código del empleado

            ResultSet resultSetDeptEmp = selectStatementDeptEmp.executeQuery();
            resultSetDeptEmp.next(); // Nos movemos a la primera fila
            int rowCountDeptEmp = resultSetDeptEmp.getInt(1);

            // Si existe, actualizamos. Si no, insertamos
            if(rowCountDeptEmp > 0) {
                fillUpdateStatementDeptEmp(updateStatementDeptEmp, employee);
                updateStatementDeptEmp.addBatch();
            } else {
                fillInsertStatementDeptEmp(insertStatementDeptEmp, employee);
                insertStatementDeptEmp.addBatch();
            }

            // Ejecutamos el batch cada lote de registros
            if (++contador % lote == 0) {
                updateStatement.executeBatch();
                insertStatement.executeBatch();
                updateStatementDeptEmp.executeBatch();
                insertStatementDeptEmp.executeBatch();
            }
        }

        // Ejecutamos el batch final
        insertStatement.executeBatch();
        updateStatement.executeBatch();
        insertStatementDeptEmp.executeBatch();
        updateStatementDeptEmp.executeBatch();
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
    private static void fillInsertStatementEmployee(PreparedStatement statement, MySqlEmployee employee) throws SQLException {
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
    private static void fillUpdateStatementEmployee(PreparedStatement statement, MySqlEmployee employee) throws SQLException {
        statement.setString(1, employee.getFirstName());
        statement.setString(2, employee.getLastName());
        statement.setString(3, employee.getGender());
        statement.setDate(4, employee.getHireDate());
        statement.setDate(5, employee.getBirthDate());
        statement.setInt(6, employee.getEmployeeId());
    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta INSERT.
     * Relación de departamento y empleado
     * @param statement - PreparedStatement
     * @param employee - Empleado
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillInsertStatementDeptEmp(PreparedStatement statement, MySqlEmployee employee) throws SQLException {
        statement.setString(1, employee.getDepartmentId());
        statement.setInt(2, employee.getEmployeeId());
        statement.setDate(3, employee.getFromDate());
        statement.setDate(4, employee.getToDate());

    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta UPDATE.
     * Relación de departamento y empleado
     * @param statement - PreparedStatement
     * @param employee - Empleado
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillUpdateStatementDeptEmp(PreparedStatement statement, MySqlEmployee employee) throws SQLException {
        statement.setDate(1, employee.getFromDate());
        statement.setDate(2, employee.getToDate());
        statement.setString(3, employee.getDepartmentId());
        statement.setInt(4, employee.getEmployeeId());
    }


    /**
     * Lee los datos del fichero CSV y los devuelve en una lista de departamentos.
     * El fichero CSV debe estar en la raíz del proyecto.
     *
     * @return - Lista de departamentos
     */
    private static List<MySqlDepartment> readDepartamentData() {

        // Try-with-resources. Se cierra el reader automáticamente al salir del bloque try
        // CSVReader nos permite leer el fichero CSV linea a linea
        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader("unirDepartments.csv"))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(',').build()).build()) {

            // Creamos la lista de departamentos
            List<MySqlDepartment> departments = new LinkedList<>();

            // Saltamos la primera línea, que contiene los nombres de las columnas del CSV
            reader.skip(1);
            String[] nextLine;

            // Leemos el fichero linea a linea
            while((nextLine = reader.readNext()) != null) {

                // Creamos el empleado y lo añadimos a la lista
                MySqlDepartment department = new MySqlDepartment(
                        nextLine[0],
                        nextLine[1]
                );
                departments.add(department);
            }
            return departments;
        } catch (IOException e) {
            log.error("Error al leer el fichero CSV de departamentos", e);
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Introduce los datos en la base de datos.
     * Si el departamento ya existe, se actualiza.
     * Si no existe, se inserta.
     *
     * Toma como referencia el campo dept_no para determinar si el departamento existe o no.
     * @param connection - Conexión a la base de datos
     * @param departments - Lista de departamentos
     * @throws SQLException - Error al ejecutar la consulta
     */
    private static void intakeDepartments(Connection connection, List<MySqlDepartment> departments) throws SQLException {

        String selectSql = "SELECT COUNT(*) FROM departments WHERE dept_no = ?";
        String insertSql = "INSERT INTO departments (dept_no, dept_name) "
                + "VALUES (?, ?)";
        String updateSql = "UPDATE departments SET dept_name = ? WHERE dept_no = ?";
        int lote = 5;
        int contador = 0;

        // Preparamos las consultas, una única vez para poder reutilizarlas en el batch
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        PreparedStatement updateStatement = connection.prepareStatement(updateSql);

        // Desactivamos el autocommit para poder ejecutar el batch y hacer commit al final
        connection.setAutoCommit(false);

        for (MySqlDepartment department : departments) {

            // Comprobamos si el empleado existe
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setString(1, department.getDeptNo()); // Código del departamento
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next(); // Nos movemos a la primera fila
            int rowCount = resultSet.getInt(1);

            // Si existe, actualizamos. Si no, insertamos
            if(rowCount > 0) {
                fillUpdateStatementDepartment(updateStatement, department);
                updateStatement.addBatch();
            } else {
                fillInsertStatementDepartment(insertStatement, department);
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
     * @param department - Departamento
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillInsertStatementDepartment(PreparedStatement statement, MySqlDepartment department) throws SQLException {
        statement.setString(1, department.getDeptNo());
        statement.setString(2, department.getDeptName());
    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta UPDATE.
     *
     * @param statement - PreparedStatement
     * @param department - Departamento
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillUpdateStatementDepartment(PreparedStatement statement, MySqlDepartment department) throws SQLException {
        statement.setString(1, department.getDeptName());
        statement.setString(2, department.getDeptNo());
    }



}
