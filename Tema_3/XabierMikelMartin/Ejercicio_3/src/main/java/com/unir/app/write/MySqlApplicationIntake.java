package com.unir.app.write;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.unir.config.MySqlConnector;
import com.unir.model.MySqlDepartamento;
import com.unir.model.MySqlEmpleado;
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

            //Leer datos 
            List<MySqlDepartamento> departamentos = readDepartamentData();
            List<MySqlEmpleado> empleados = readEmployeesData();
            //Escribir datos
            writeDataManagement(connection,departamentos,empleados);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    private static void writeDataManagement(Connection connection, List<MySqlDepartamento> departamentos, List<MySqlEmpleado> empleados) throws SQLException, ParseException {

        // Desactivamos el autocommit para poder ejecutar el batch y hacer commit al final
        connection.setAutoCommit(false);

        writeDepartments(connection, departamentos);
        writeEmployees(connection,empleados);

        // Hacemos commit y volvemos a activar el autocommit
        connection.commit();
        connection.setAutoCommit(true);
    }


    /**
     * Lee los datos del fichero CSV y los devuelve en una lista de departamentos.
     * El fichero CSV debe estar en la raíz del proyecto.
     *
     * @return - Lista de departamentosd
     */
    private static List<MySqlDepartamento> readDepartamentData() {

        // Try-with-resources. Se cierra el reader automáticamente al salir del bloque try
        // CSVReader nos permite leer el fichero CSV linea a linea
        try (CSVReader reader = new CSVReader(new FileReader("departamentos.csv"))) {

            // Creamos la lista de empleados y el formato de fecha
            List<MySqlDepartamento> depts = new LinkedList<>();

            // Saltamos la primera linea, que contiene los nombres de las columnas del CSV
            reader.skip(1);
            String[] nextLine;

            // Leemos el fichero linea a linea
            while((nextLine = reader.readNext()) != null) {

                // Creamos el empleado y lo añadimos a la lista
                MySqlDepartamento departamento = new MySqlDepartamento(
                        nextLine[0],
                        nextLine[1]
                );
                depts.add(departamento);
            }
            return depts;
        } catch (IOException e) {
            log.error("Error al leer el fichero CSV", e);
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Lee los datos del fichero CSV y los devuelve en una lista de empleados.
     * El fichero CSV debe estar en la raíz del proyecto.
     *
     * @return - Lista de empleados
     */
    private static List<MySqlEmpleado> readEmployeesData() {

        // Try-with-resources. Se cierra el reader automáticamente al salir del bloque try
        // CSVReader nos permite leer el fichero CSV linea a linea
        try (CSVReader reader = new CSVReader(new FileReader("empleados.csv"))) {

            // Creamos la lista de empleados y el formato de fecha
            List<MySqlEmpleado> employees = new LinkedList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");

            // Saltamos la primera linea, que contiene los nombres de las columnas del CSV
            reader.skip(1);
            String[] nextLine;

            // Leemos el fichero linea a linea
            while((nextLine = reader.readNext()) != null) {

                // Creamos el empleado y lo añadimos a la lista
                MySqlEmpleado employee = new MySqlEmpleado(
                        Integer.parseInt(nextLine[0]),
                        nextLine[1],
                        nextLine[2],
                        nextLine[3],
                        new Date(format.parse(nextLine[4]).getTime()),
                        new Date(format.parse(nextLine[5]).getTime()),
                        nextLine[6]
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
     * Introduce los datos de departamentos en la base de datos.
     * Si el departamento ya existe, se actualiza.
     * Si no existe, se inserta.
     *
     * Toma como referencia el campo dept_no para determinar si el departamento existe o no.
     * @param connection - Conexión a la base de datos
     * @param departament - Lista de department
     * @throws SQLException - Error al ejecutar la consulta
     */
    private static void writeDepartments(Connection connection, List<MySqlDepartamento> departament) throws SQLException {

        String selectSql = "SELECT COUNT(*) FROM departments WHERE dept_no = ?";
        String insertSql = "INSERT INTO departments (dept_no, dept_name) VALUES (?, ?) ";
        String updateSql = "UPDATE departments SET dept_name = ? WHERE dept_no = ?";

        int lote = 5;
        int contador = 0;

        // Preparamos las consultas, una unica vez para poder reutilizarlas en el batch
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        PreparedStatement updateStatement = connection.prepareStatement(updateSql);

        for (MySqlDepartamento dept : departament) {

            // Comprobamos si el empleado existe
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setString(1, dept.getDeptNo()); // Código del departamento
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next(); // Nos movemos a la primera fila
            int rowCount = resultSet.getInt(1);

            // Si existe, actualizamos. Si no, insertamos
            if(rowCount > 0) {
                fillUpdateDepartmentsStatement(updateStatement, dept);
                updateStatement.addBatch();
            } else {
                fillInsertDepartmentsStatement(insertStatement, dept);
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

    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta UPDATE de departamentos
     *
     * @param statement - PreparedStatement
     * @param dept - departamento
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillUpdateDepartmentsStatement(PreparedStatement statement, MySqlDepartamento dept) throws SQLException {
        statement.setString(1, dept.getDeptName());
        statement.setString(2, dept.getDeptNo());
    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta INSERT de departamentos
     *
     * @param statement - PreparedStatement
     * @param dept - departamento
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillInsertDepartmentsStatement(PreparedStatement statement, MySqlDepartamento dept) throws SQLException {
        statement.setString(1, dept.getDeptNo());
        statement.setString(2, dept.getDeptName());
    }

    /**
     * Introduce los datos en la base de datos.
     * Si el empleado ya existe, se actualiza.
     * Si no existe, se inserta.
     * Además inserta o updatea la tabla dept_emp
     *
     * Toma como referencia el campo emp_no para determinar si el empleado existe o no.
     * @param connection - Conexión a la base de datos
     * @param employees - Lista de empleados
     * @throws SQLException - Error al ejecutar la consulta
     */
    private static void writeEmployees(Connection connection, List<MySqlEmpleado> employees) throws SQLException, ParseException {

        String selectSql = "SELECT COUNT(*) FROM employees WHERE emp_no = ?";
        String insertEmployeeSql = "INSERT INTO employees (emp_no, first_name, last_name, gender, hire_date, birth_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String updateEmployeeSql = "UPDATE employees SET first_name = ?, last_name = ?, gender = ?, hire_date = ?, birth_date = ? WHERE emp_no = ?";

        String insertDeptEmpSql = "INSERT INTO dept_emp (emp_no,dept_no,from_date,to_date) Values (?,?,?,?)";
        String updateDeptEmpSql = "UPDATE dept_emp SET from_date = ?, to_date = ? WHERE emp_no = ? and dept_no= ?";

        int lote = 5;
        int contador = 0;

        // Preparamos las consultas, una unica vez para poder reutilizarlas en el batch
        PreparedStatement insertEmployeeStatement = connection.prepareStatement(insertEmployeeSql);
        PreparedStatement updateEmployeeStatement = connection.prepareStatement(updateEmployeeSql);
        PreparedStatement insertDeptEmpStatement = connection.prepareStatement(insertDeptEmpSql);
        PreparedStatement updateDeptEmpStatement = connection.prepareStatement(updateDeptEmpSql);

        for (MySqlEmpleado employee : employees) {

            // Comprobamos si el empleado existe
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, employee.getEmployeeId()); // Código del empleado
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next(); // Nos movemos a la primera fila
            int rowCount = resultSet.getInt(1);

            // Si existe, actualizamos. Si no, insertamos
            if(rowCount > 0) {
                fillUpdateEmployeeStatement(updateEmployeeStatement, employee);
                updateEmployeeStatement.addBatch();
                fillUpdateDeptEmpStatement(updateDeptEmpStatement, employee);
                updateDeptEmpStatement.addBatch();

            } else {
                fillInsertEmployeeStatement(insertEmployeeStatement, employee);
                insertEmployeeStatement.addBatch();
                fillInsertDeptEmpStatement(insertDeptEmpStatement, employee);
                insertDeptEmpStatement.addBatch();
            }

            // Ejecutamos el batch cada lote de registros
            if (++contador % lote == 0) {
                updateEmployeeStatement.executeBatch();
                insertEmployeeStatement.executeBatch();
                insertDeptEmpStatement.executeBatch();
                updateDeptEmpStatement.executeBatch();
            }
        }

        // Ejecutamos el batch final
        insertEmployeeStatement.executeBatch();
        updateEmployeeStatement.executeBatch();
        insertDeptEmpStatement.executeBatch();
        updateDeptEmpStatement.executeBatch();

    }
    /**
     * Rellena los parámetros de un PreparedStatement para una consulta INSERT.
     *
     * @param statement - PreparedStatement
     * @param employee - Empleado
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillInsertDeptEmpStatement(PreparedStatement statement, MySqlEmpleado employee) throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        statement.setInt(1, employee.getEmployeeId());
        statement.setString(2, employee.getDept_no());
        statement.setDate(3, employee.getHireDate());
        statement.setDate(4,  new Date(format.parse("9999-01-01").getTime()));
    }

    /**
     * Rellena los parámetros de un PreparedStatement para una consulta UPDATE.
     *
     * @param statement - PreparedStatement
     * @param employee - Empleado
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillUpdateDeptEmpStatement(PreparedStatement statement, MySqlEmpleado employee) throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        statement.setDate(1, employee.getHireDate());
        statement.setDate(2, new Date(format.parse("9999-01-01").getTime()));
        statement.setInt(3, employee.getEmployeeId());
        statement.setString(4, employee.getDept_no());
    }


    /**
     * Rellena los parámetros de un PreparedStatement para una consulta INSERT.
     *
     * @param statement - PreparedStatement
     * @param employee - Empleado
     * @throws SQLException - Error al rellenar los parámetros
     */
    private static void fillInsertEmployeeStatement(PreparedStatement statement, MySqlEmpleado employee) throws SQLException {
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
    private static void fillUpdateEmployeeStatement(PreparedStatement statement, MySqlEmpleado employee) throws SQLException {
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
