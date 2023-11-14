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

            log.info("Primera consulta:");
            selectNameLastDept(connection);
            log.info("Segunda consulta:");
            selectDataManagers(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    private static void selectNameLastDept(Connection connection) throws SQLException{
        PreparedStatement select = connection.prepareStatement(
                "SELECT XMLELEMENT(\"empleados\",\n" +
                        "    XMLATTRIBUTES(  em.FIRST_NAME AS \"nombre\", \n" +
                        "                    em.LAST_NAME AS \"apellidos\", \n" +
                        "                    de.DEPARTMENT_NAME AS \"departamento\")\n" +
                        ") AS EmpleadosXML\n" +
                        "FROM EMPLOYEES em\n" +
                        "JOIN DEPARTMENTS de ON em.DEPARTMENT_ID = de.DEPARTMENT_ID ");

        ResultSet empleados = select.executeQuery();
        while (empleados.next()) {
            log.debug("Departamento como XML: {}",
                    empleados.getString("EmpleadosXML")

            );
        }

    }

    private static void selectDataManagers(Connection connection) throws SQLException{
        PreparedStatement select = connection.prepareStatement(
                "SELECT XMLELEMENT(\"managers\",\n" +
                        "    XMLAGG(\n" +
                        "        XMLELEMENT(\"manager\",\n" +
                        "            XMLELEMENT(\"nombreCompleto\",\n" +
                        "                XMLFOREST(e.FIRST_NAME as \"nombre\", e.LAST_NAME as \"apellido\")\n" +
                        "            ),\n" +
                        "            XMLELEMENT(\"department\", d.DEPARTMENT_NAME),\n" +
                        "            XMLELEMENT(\"city\", l.CITY),\n" +
                        "            XMLELEMENT(\"country\", c.COUNTRY_NAME)\n" +
                        "        )\n" +
                        "    )\n" +
                        ")AS ManagersXML\n" +
                        "FROM EMPLOYEES e\n" +
                        "INNER JOIN DEPARTMENTS d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID\n" +
                        "INNER JOIN LOCATIONS l ON d.LOCATION_ID = l.LOCATION_ID\n" +
                        "INNER JOIN COUNTRIES c ON l.COUNTRY_ID = c.COUNTRY_ID\n" +
                        "WHERE e.EMPLOYEE_ID IN (SELECT DISTINCT manager_id FROM employees)");


        ResultSet empleados = select.executeQuery();
        while (empleados.next()) {
            log.debug("Managers XML: {}",
                    empleados.getString("ManagersXML")

            );
        }


    }

}
