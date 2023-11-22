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

            // selectAllEmployees(connection);
            // selectAllCountriesAsXml(connection);
            selectAllEmployeesXMLElement(connection);
            selectAllManagersXML(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }


    /**
     * 1. (Debes usar XMLELEMENT) Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
     * -- Cada resultado XML devuelto por la consulta (la consulta debe devolver 1 registro por empleado):
     * @param connection
     * @throws SQLException
     */
    private static void selectAllEmployeesXMLElement(Connection connection) throws SQLException {
        PreparedStatement selectAllEmployeesXML = connection.prepareStatement(
                "SELECT XMLELEMENT(\"empleados\",\n" +
                    "                  XMLATTRIBUTES(\n" +
                    "                      EMPLOYEES.FIRST_NAME AS \"nombre\",\n" +
                    "                      EMPLOYEES.LAST_NAME AS \"apellidos\",\n" +
                    "                      departments.DEPARTMENT_NAME AS \"departamento\"\n" +
                    "                  )\n" +
                    "       ) AS EMPLEADOS\n" +
                    "FROM EMPLOYEES\n" +
                    "         JOIN DEPARTMENTS departments ON EMPLOYEES.DEPARTMENT_ID = departments.DEPARTMENT_ID"
        );
        ResultSet employeesXML = selectAllEmployeesXML.executeQuery();

        while (employeesXML.next()) {
            log.debug("Employee XML Element: {}", employeesXML.getString("EMPLEADOS"));
        }
    }

    /**
     * -- 2. (Debes usar XMLELEMENT, XMLAGG y XMLFOREST) Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers.
     * -- El XML devuelto por la consulta (debe devolver un único registro, con todos los managers
     * @param connection
     * @throws SQLException
     */
    private static void selectAllManagersXML(Connection connection) throws SQLException {
        PreparedStatement selectAllManagersXML = connection.prepareStatement(
                "SELECT XMLELEMENT(\"managers\",\n" +
                        "    XMLAGG(\n" +
                        "        XMLELEMENT(\"manager\",\n" +
                        "            XMLELEMENT(\"nombreCompleto\", XMLFOREST(EMPLOYEES.FIRST_NAME as \"nombre\", EMPLOYEES.LAST_NAME as \"apellido\")),\n" +
                        "            XMLELEMENT(\"department\", DEPARTMENTS.DEPARTMENT_NAME),\n" +
                        "            XMLELEMENT(\"city\", LOCATIONS.CITY),\n" +
                        "            XMLELEMENT(\"country\", COUNTRIES.COUNTRY_NAME)\n" +
                        "        )\n" +
                        "    )\n" +
                        ") AS managers\n" +
                        "FROM EMPLOYEES\n" +
                        "INNER JOIN DEPARTMENTS ON EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID\n" +
                        "INNER JOIN LOCATIONS ON DEPARTMENTS.LOCATION_ID = LOCATIONS.LOCATION_ID\n" +
                        "INNER JOIN COUNTRIES ON LOCATIONS.COUNTRY_ID = COUNTRIES.COUNTRY_ID\n" +
                        "WHERE EMPLOYEES.EMPLOYEE_ID IN (SELECT DISTINCT manager_id FROM employees)"
        );
        ResultSet managersXML = selectAllManagersXML.executeQuery();

        while (managersXML.next()) {
            log.debug("All Managers (XML File): {}", managersXML.getString("MANAGERS"));
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
}
