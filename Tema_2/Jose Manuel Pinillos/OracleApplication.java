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

            log.debug("1. Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.");
            selectEmployeesWithDepartamentAsXml(connection);

            log.debug("2. Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers.");
            selectManagesrsAsXml(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    private static void selectEmployeesWithDepartamentAsXml(Connection connection) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("SELECT\n" +
                "    XMLELEMENT(\"empleados\",\n" +
                "        XMLATTRIBUTES (\n" +
                "            FIRST_NAME AS \"nombre\",\n" +
                "            LAST_NAME AS \"apellidos\",\n" +
                "            DEPARTMENT_NAME AS \"departamento\"\n" +
                "        )\n" +
                "    )\n" +
                "AS empleados\n" +
                "FROM EMPLOYEES, DEPARTMENTS\n" +
                "WHERE EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID");

        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleado as XML: {}",
                    employees.getString("empleados"));
        }
    }

    private static void selectManagesrsAsXml(Connection connection) throws SQLException {
        PreparedStatement selectManagers = connection.prepareStatement("SELECT DISTINCT\n" +
                "    XMLELEMENT(\"managers\",\n" +
                "        XMLAGG(\n" +
                "            XMLELEMENT(\"manager\",\n" +
                "                XMLELEMENT(\"nombreCompleto\",\n" +
                "                    XMLFOREST(\n" +
                "                        FIRST_NAME AS \"nombre\",\n" +
                "                        LAST_NAME AS \"apellido\")),\n" +
                "                XMLELEMENT(\"department\", DEPARTMENT_NAME),\n" +
                "                XMLELEMENT(\"city\", CITY),\n" +
                "                XMLELEMENT(\"country\", COUNTRY_NAME)\n" +
                "            )\n" +
                "        )\n" +
                "    ) AS managers\n" +
                "FROM EMPLOYEES, LOCATIONS, COUNTRIES, DEPARTMENTS, JOB_HISTORY, JOBS\n" +
                "WHERE EMPLOYEES.EMPLOYEE_ID = JOB_HISTORY.EMPLOYEE_ID and\n" +
                "      EMPLOYEES.DEPARTMENT_ID = JOB_HISTORY.DEPARTMENT_ID and\n" +
                "      JOB_HISTORY.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID and\n" +
                "      DEPARTMENTS.LOCATION_ID = LOCATIONS.LOCATION_ID and\n" +
                "      LOCATIONS.COUNTRY_ID = COUNTRIES.COUNTRY_ID and\n" +
                "      JOBS.JOB_TITLE like '%Manager%'");

        ResultSet employees = selectManagers.executeQuery();

        while (employees.next()) {
            log.debug("Managers as XML: {}",
                    employees.getString("managers"));
        }
    }
}
