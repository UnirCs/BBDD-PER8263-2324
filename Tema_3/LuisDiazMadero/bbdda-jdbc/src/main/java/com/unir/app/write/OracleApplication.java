package com.unir.app.write;

import com.unir.config.OracleDatabaseConnector;
import com.unir.model.OracleCountry;
import com.unir.model.OracleEmployee;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * La version para MySQL seria muy similar a esta, cambiando únicamente el Driver.
 */
@Slf4j
public class OracleApplication {

    private static final String SERIVCE_NAME = "orcl";

    public static void main(String[] args) {
        try(Connection connection = new OracleDatabaseConnector("localhost", SERIVCE_NAME).getConnection()) {
            log.info("Conexión establecida con la base de datos Oracle");
            OracleEmployee Juanito = new OracleEmployee(300, "Juanito", "Perez", "juanilloElPillo", "666.555.5430", new java.sql.Date(2021, 1, 1), "IT_PROG", 25001, 0.1, 108, 100);
            newEmployee(connection, Juanito);
        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    //Ejercicio 1 Tema 3
    //Funcion para añadir un nuevo empleado a la base de datos usando JDBC Y usando el modelo de empleados
    public static void newEmployee(Connection connection, OracleEmployee employee) throws SQLException {
        String select = "SELECT COUNT(*) FROM employees WHERE employee_id = ?";
        String insert = "INSERT INTO employees (employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String update = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, hire_date = ?, job_id = ?, salary = ?, commission_pct = ?, manager_id = ?, department_id = ? WHERE employee_id = ?";

        PreparedStatement selectStatement = connection.prepareStatement(select);
        selectStatement.setInt(1, employee.getEmployee_id());
        ResultSet resultSet = selectStatement.executeQuery();
        resultSet.next();
        int rowCount = resultSet.getInt(1);

        if(rowCount > 0){
            PreparedStatement updateStatement = connection.prepareStatement(update);
            updateStatement.setString(1, employee.getFirst_name());
            updateStatement.setString(2, employee.getLast_name());
            updateStatement.setString(3, employee.getEmail());
            updateStatement.setString(4, employee.getPhone_number());
            updateStatement.setDate(5, employee.getHire_date());
            updateStatement.setString(6, employee.getJob_id());
            updateStatement.setDouble(7, employee.getSalary());
            updateStatement.setDouble(8, employee.getCommission_pct());
            updateStatement.setInt(9, employee.getManager_id());
            updateStatement.setInt(10, employee.getDepartment_id());
            updateStatement.setInt(11, employee.getEmployee_id());
            int filasActualizadas = updateStatement.executeUpdate();
            log.debug("Filas Actualizadas: {}", filasActualizadas);
        }
        else{
            PreparedStatement insertStatement = connection.prepareStatement(insert);
            insertStatement.setInt(1, employee.getEmployee_id());
            insertStatement.setString(2, employee.getFirst_name());
            insertStatement.setString(3, employee.getLast_name());
            insertStatement.setString(4, employee.getEmail());
            insertStatement.setString(5, employee.getPhone_number());
            insertStatement.setDate(6, employee.getHire_date());
            insertStatement.setString(7, employee.getJob_id());
            insertStatement.setDouble(8, employee.getSalary());
            insertStatement.setDouble(9, employee.getCommission_pct());
            insertStatement.setInt(10, employee.getManager_id());
            insertStatement.setInt(11, employee.getDepartment_id());
            int filasInsertadas = insertStatement.executeUpdate();
            log.debug("Filas Insertadas: {}", filasInsertadas);
        }
    }

}
