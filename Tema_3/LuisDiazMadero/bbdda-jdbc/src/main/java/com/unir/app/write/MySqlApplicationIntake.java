package com.unir.app.write;

import com.google.protobuf.TextFormat;
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
            // Leemos los datos del fichero CSV
            List<MySqlDepartment> departments = readDepartmentData();
            List<MySqlEmployee> employees = readEmployeesData();
            //List<MySqlEmployee> employees = readEmployeesData();
            // Introducimos los datos en la base de datos
            ingestaDepartment(connection, departments);
            ingestaEmployees(connection, employees);
        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }
    /**
     * Lee los datos del fichero CSV de los nuevos departamentos y los introduce en la base de datos.
     * return - Lista de departamentos
     */
    private static List<MySqlDepartment> readDepartmentData(){
        try(CSVReader reader = new CSVReaderBuilder(
                new FileReader("departamentos.csv")).withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build()){
            List<MySqlDepartment> departments = new LinkedList<>();
            reader.skip(1);
            String[] nextLine;
            while((nextLine = reader.readNext()) != null) {
                MySqlDepartment department = new MySqlDepartment(
                        nextLine[0],
                        nextLine[1]
                );
                departments.add(department);
            }
            return departments;
        }catch(IOException e){
            log.error("Error al leer el ficheroCSV", e);
            throw new RuntimeException(e);
        }catch(CsvValidationException e){
            throw new RuntimeException(e);
        }
    }
    /*
    * Introducer los departamentos en la BD
    */
    private static void ingestaDepartment(Connection connection, List<MySqlDepartment>departments)throws SQLException{
        String select = "SELECT COUNT(*) FROM departments WHERE dept_no = ?";
        String insert = "INSERT INTO departments (dept_no, dept_name) VALUES (?, ?)";
        String update = "UPDATE departments SET dept_name = ? WHERE dept_no = ?";
        int lote = 5;
        int contador = 0;
        PreparedStatement insertStatement = connection.prepareStatement(insert);
        PreparedStatement updateStatement = connection.prepareStatement(update);
        connection.setAutoCommit(false);
        for (MySqlDepartment department : departments) {
            PreparedStatement selectStatement = connection.prepareStatement(select);
            selectStatement.setString(1, department.getDept_no());
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int rowCount = resultSet.getInt(1);

            //Si existe, actualizamos. Si no, insertamos
            if(rowCount > 0){
                fillUpdateStatement(updateStatement, department);
                updateStatement.addBatch();
            }else{
                fillInsertStatement(insertStatement, department);
                insertStatement.addBatch();
            }
            //Ejecutamos el batch cada 5 iteraciones
            if(++contador % lote == 0){
                updateStatement.executeBatch();
                insertStatement.executeBatch();
            }
        }
        insertStatement.executeBatch();
        updateStatement.executeBatch();
        connection.commit();
        connection.setAutoCommit(true);
    }
    /*
     * Rellenamos los parametros de un preparedStatement
     * PARA HACER INSERT
     */
    private static void fillInsertStatement(PreparedStatement statement, MySqlDepartment department)throws SQLException{
        statement.setString(1, department.getDept_no());
        statement.setString(2, department.getDepartmentName());
    }
    /*
     * Rellenamos los parametros de un preparedStatement
     * PARA HACER UPDATE
     */
    private static void fillUpdateStatement(PreparedStatement statement, MySqlDepartment department)throws SQLException{
        statement.setString(1, department.getDepartmentName());
        statement.setString(2, department.getDept_no());
    }
    /*
     * Lee los empleados del CSV y devuelve una lista
     */
    private static List<MySqlEmployee> readEmployeesData() {
        try(CSVReader reader = new CSVReaderBuilder(
                new FileReader("empleados.csv")).withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build()){
            List<MySqlEmployee> employees = new LinkedList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
            reader.skip(1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                MySqlEmployee employee = new MySqlEmployee(
                        Integer.parseInt(nextLine[0]),
                        new Date(format.parse(nextLine[1]).getTime()),
                        nextLine[2],
                        nextLine[3],
                        nextLine[4],
                        new Date(format.parse(nextLine[5]).getTime()),
                        nextLine[6]
                );
                employees.add(employee);
            }
            return employees;
        }catch(IOException e){
            log.error("Error leyendo los emleados del CSV");
            throw new RuntimeException(e);
        }catch(CsvValidationException | ParseException e){
            throw new RuntimeException(e);
        }
    }
    private static void ingestaEmployees(Connection connection, List<MySqlEmployee> employees) throws SQLException{
        String select = "SELECT COUNT(*) FROM employees WHERE emp_no = ?";
        String insert = "INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES (?, ?, ?, ?, ?, ?)";
        String update = "UPDATE employees SET birth_date = ?, first_name = ?, last_name = ?, gender = ?, hire_date = ? WHERE emp_no = ?";
        String insertDept_Emp = "INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date) VALUES (?, ?, ?, ?)";
        String updateDept_Emp = "UPDATE dept_emp SET dept_no = ?, from_date = ?, to_date = ? WHERE emp_no = ?";

        PreparedStatement insertStatement = connection.prepareStatement(insert);
        PreparedStatement updateStatement = connection.prepareStatement(update);
        PreparedStatement insertDept_EmpStatement = connection.prepareStatement(insertDept_Emp);
        PreparedStatement updateDept_EmpStatement = connection.prepareStatement(updateDept_Emp);
        connection.setAutoCommit(false);
        int lote = 5;
        int contador = 0;
        for (MySqlEmployee employee : employees) {
            PreparedStatement selectStatement = connection.prepareStatement(select);
            selectStatement.setInt(1, employee.getEmployeeId());
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int rowCount = resultSet.getInt(1);
            if(rowCount > 0){
                fillUpdateEmployee(updateStatement, employee);
                updateStatement.executeUpdate();
                fillUpdateDept_Emp(updateDept_EmpStatement, employee);
                updateDept_EmpStatement.executeUpdate();
            }else{
                fillInsertEmployee(insertStatement, employee);
                insertStatement.executeUpdate();
                fillInsertDept_Emp(insertDept_EmpStatement, employee);
                insertDept_EmpStatement.executeUpdate();
            }
            if (++contador % lote == 0) {
                updateStatement.executeBatch();
                insertStatement.executeBatch();
                updateDept_EmpStatement.executeBatch();
                insertDept_EmpStatement.executeBatch();
            }
        }
        insertStatement.executeBatch();
        updateStatement.executeBatch();
        insertDept_EmpStatement.executeBatch();
        updateDept_EmpStatement.executeBatch();
        connection.commit();
        connection.setAutoCommit(true);
    }
    private static void fillInsertEmployee(PreparedStatement statement, MySqlEmployee employee)throws SQLException{
        statement.setInt(1, employee.getEmployeeId());
        statement.setDate(2, employee.getBirthDate());
        statement.setString(3, employee.getFirstName());
        statement.setString(4, employee.getLastName());
        statement.setString(5, employee.getGender());
        statement.setDate(6, employee.getHireDate());
    }
    private static void fillUpdateEmployee(PreparedStatement statement, MySqlEmployee employee)throws SQLException{
        statement.setDate(1, employee.getBirthDate());
        statement.setString(2, employee.getFirstName());
        statement.setString(3, employee.getLastName());
        statement.setString(4, employee.getGender());
        statement.setDate(5, employee.getHireDate());
        statement.setInt(6, employee.getEmployeeId());
    }
    private static void fillInsertDept_Emp(PreparedStatement statement, MySqlEmployee employee)throws SQLException{
        statement.setInt(1, employee.getEmployeeId());
        statement.setString(2, employee.getDept_no());
        statement.setDate(3, employee.getHireDate());
        statement.setDate(4, employee.getHireDate());
    }
    private static void fillUpdateDept_Emp(PreparedStatement statement, MySqlEmployee employee)throws SQLException{
        statement.setString(1, employee.getDept_no());
        statement.setDate(2, employee.getHireDate());
        statement.setDate(3, employee.getHireDate());
        statement.setInt(4, employee.getEmployeeId());
    }
}
