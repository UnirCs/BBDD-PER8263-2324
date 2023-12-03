package com.unir.app.write;

import com.unir.config.MySqlConnector;
import com.unir.model.MySqlDepartament;
import com.unir.model.MySqlEmployee;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.List;

import static com.unir.model.MySqlDepartament.intakeDepartment;
import static com.unir.model.MySqlDepartament.readDataDepartment;
import static com.unir.model.MySqlEmployee.intakeEmployee;
import static com.unir.model.MySqlEmployee.readDataEmployee;

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
            List<MySqlDepartament> departments = readDataDepartment();

            // Introducimos los datos en la base de datos
            intakeDepartment(connection, departments);

            // Leemos los datos del fichero CSV
            List<MySqlEmployee> employees = readDataEmployee();

            // Introducimos los datos en la base de datos
            intakeEmployee(connection, employees);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

}
