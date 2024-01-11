package com.unir.app.read;

import com.unir.config.OracleDatabaseConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class OracleApplication {

    private static final String SERIVCE_NAME = "orcl";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new OracleDatabaseConnector("localhost", SERIVCE_NAME).getConnection()) {

            log.debug("Conexión establecida con la base de datos Oracle");
            selectEmplyeesDepar(connection);
            selectManagers(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    //1. Mostrar el nombre y apellidos de un empleado junto con el nombre de su departamento.
    private static void selectEmplyeesDepar(Connection connection)throws SQLException{
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT XMLELEMENT(\"empleados\", " +
                        "                  XMLATTRIBUTES(e.first_name AS \"nombre\", " +
                        "                  e.last_name AS \"apellidos\", " +
                        "                  d.department_name AS \"departamento\")) " +
                        "FROM employees e " +
                        "JOIN departments d ON e.department_id = d.department_id"
        );
        ResultSet employees = selectEmployees.executeQuery();
        while(employees.next()){
            String xmlString = employees.getString(1);
            log.debug("Empleado como XML: {} ",
                    xmlString);
        }
    }
    //2. Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son manager
    private static void selectManagers(Connection connection) throws SQLException {
        PreparedStatement selectManagers = connection.prepareStatement(
                "SELECT XMLELEMENT(\"managers\", " +
                        "    XMLAGG(" +
                        "          XMLELEMENT(\"manager\", " +
                        "             XMLELEMENT(\"nombreCompleto\", " +
                        "                XMLFOREST(" +
                        "                        e.first_name AS \"nombre\", " +
                        "                        e.last_name AS \"apellido\"" +
                        "                )" +
                        "             ), " +
                        "             XMLELEMENT(\"department\", d.department_name), " +
                        "             XMLELEMENT(\"city\", l.city), " +
                        "             XMLELEMENT(\"country\", c.COUNTRY_NAME)" +
                        "            )" +
                        "    )" +
                        ") AS managers " +
                        "FROM employees e " +
                        "JOIN departments d ON e.department_id = d.department_id " +
                        "JOIN locations l ON d.location_id = l.location_id " +
                        "JOIN COUNTRIES c ON l.COUNTRY_ID = c.COUNTRY_ID " +
                        "WHERE e.employee_id IN (SELECT DISTINCT manager_id FROM employees)"
        );
        ResultSet managers = selectManagers.executeQuery();
        while (managers.next()) {
            // Obtener la cadena XML del ResultSet
            String xmlString = managers.getString(1);

            // Imprimir la cadena XML en el log
            log.debug("Managers como XML: {} ", xmlString);
        }
    }

}
