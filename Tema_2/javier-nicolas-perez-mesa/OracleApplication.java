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

            selectAllEmployees(connection);
            selectAllCountriesAsXml(connection);
            selectAllEmployeesOnXML(connection);
            selectAllManagersOnXML(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando Statement.
     * Statement es la forma más básica de ejecutar consultas a la base de datos.
     * Es la más insegura, ya que no se protege de ataques de inyección SQL.
     * No obstante, es útil para sentencias DDL.
     * @param connection
     * @throws SQLException
     */
    private static void selectAllEmployees(Connection connection) throws SQLException {
        Statement selectEmployees = connection.createStatement();
        ResultSet employees = selectEmployees.executeQuery("select * from EMPLOYEES");

        while (employees.next()) {
            log.debug("Employee: {} {}",
                    employees.getString("FIRST_NAME"),
                    employees.getString("LAST_NAME"));
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando PreparedStatement y SQL/XML.
     * Para usar SQL/XML, es necesario que la base de datos tenga instalado el módulo XDB.
     * En Oracle 19c, XDB viene instalado por defecto.
     * Ademas, se necesitan las dependencias que se encuentran en el pom.xml.
     * @param connection
     * @throws SQLException
     */
    private static void selectAllCountriesAsXml(Connection connection) throws SQLException {
        PreparedStatement selectCountries = connection.prepareStatement("SELECT\n" +
                "  XMLELEMENT(\"countryXml\",\n" +
                "       XMLATTRIBUTES(\n" +
                "         c.country_name AS \"name\",\n" +
                "         c.region_id AS \"code\",\n" +
                "         c.country_id AS \"id\"))\n" +
                "  AS CountryXml\n" +
                "FROM  countries c\n" +
                "WHERE c.country_name LIKE ?");
        selectCountries.setString(1, "S%");

        ResultSet countries = selectCountries.executeQuery();
        while (countries.next()) {
            log.debug("Country as XML: {}", countries.getString("CountryXml"));
        }
    }

    /**
     * Ejemplo de obtención de respuesta de la base de datos en xml.
     * @param connection
     * @throws SQLException
     */
    private static void selectAllEmployeesOnXML(Connection connection) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT\n" +
                        "    XMLELEMENT(\n" +
                        "        NAME \"empleados\",\n" +
                        "        XMLATTRIBUTES (\n" +
                        "            EMPLOYEES.FIRST_NAME  AS \"nombre\",\n" +
                        "            EMPLOYEES.LAST_NAME   AS \"apellidos\",\n" +
                        "            DEPARTMENTS.DEPARTMENT_NAME AS \"departamento\"\n" +
                        "        )\n" +
                        "    ) AS \"xml\"\n" +
                        "FROM\n" +
                        "    HR.EMPLOYEES\n" +
                        "INNER JOIN\n" +
                        "    HR.DEPARTMENTS\n" +
                        "ON\n" +
                        "    EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID"
        );
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Employee xml: {}", employees.getString("xml"));
        }
    }

    /**
     * Ejemplo de obtención de respuesta de la base de datos en xml.
     * @param connection
     * @throws SQLException
     */
    private static void selectAllManagersOnXML(Connection connection) throws SQLException {
        // Create a PreparedStatement object to execute the SQL query.
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT XMLELEMENT(\n" +
                        "    NAME \"managers\",\n" +
                        "    XMLAGG(\n" +
                        "        XMLELEMENT(\n" +
                        "            NAME \"manager\",\n" +
                        "            XMLFOREST(\n" +
                        "                XMLFOREST(\n" +
                        "                    EMPLOYEES.FIRST_NAME AS \"first_name\",\n" +
                        "                    EMPLOYEES.LAST_NAME AS \"last_name\"\n" +
                        "                ) AS \"nombreCompleto\",\n" +
                        "                DEPARTMENTS.DEPARTMENT_NAME AS \"department\",\n" +
                        "                LOCATIONS.CITY AS \"city\",\n" +
                        "                COUNTRIES.COUNTRY_NAME AS \"country\"\n" +
                        "            )\n" +
                        "        )\n" +
                        "    )\n" +
                        ") AS \"xml\"\n" +
                        "FROM HR.EMPLOYEES\n" +
                        "INNER JOIN HR.DEPARTMENTS\n" +
                        "ON EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID\n" +
                        "INNER JOIN HR.LOCATIONS\n" +
                        "ON DEPARTMENTS.LOCATION_ID = LOCATIONS.LOCATION_ID\n" +
                        "INNER JOIN HR.COUNTRIES\n" +
                        "ON LOCATIONS.COUNTRY_ID = COUNTRIES.COUNTRY_ID"
        );

        // Execute the PreparedStatement object and get the results.
        ResultSet employees = selectEmployees.executeQuery();

        // Loop through the results and log the XML output to the debug level.
        while (employees.next()) {
            log.debug("Manager xml: {}", employees.getString("xml"));
        }
    }
}
