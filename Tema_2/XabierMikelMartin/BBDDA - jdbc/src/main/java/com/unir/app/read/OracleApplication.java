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

            log.info("Conexión establecida con la base de datos Oracle \n\n");

            selectAllEmployeesAsXml(connection);
            selectAllManagersAsXml(connection);

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

    // Consulta 1
    // Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
    private static void selectAllEmployeesAsXml(Connection connection) throws SQLException {

        log.debug("Consulta 1 - Mostrar XML con el nombre y apellido de un empleado junto con el nombre de su departamento");

        Statement selectEmployees = connection.createStatement();
        ResultSet employees = selectEmployees.executeQuery("SELECT XMLELEMENT(\"empleados\",\n" +
                "           XMLATTRIBUTES (e.FIRST_NAME as \"nombre\",\n" +
                "                        e.LAST_NAME as \"apellidos\",\n" +
                "                        d.DEPARTMENT_NAME as \"departamento\")) as empleados\n" +
                "FROM EMPLOYEES e inner join DEPARTMENTS d on e.DEPARTMENT_ID = d.DEPARTMENT_ID");


        while (employees.next()) {
            log.debug("Employees as XML: {}", employees.getString("empleados"));
        }
    }

    // Consulta 2
    // Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers
    private static void selectAllManagersAsXml(Connection connection) throws SQLException {

        log.debug("Consulta 2 - Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers");

        Statement selectManagers = connection.createStatement();
        ResultSet managers = selectManagers.executeQuery("SELECT XMLELEMENT(\"managers\",\n" +
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
                ")AS managers\n" +
                "FROM EMPLOYEES e\n" +
                "INNER JOIN DEPARTMENTS d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID\n" +
                "INNER JOIN LOCATIONS l ON d.LOCATION_ID = l.LOCATION_ID\n" +
                "INNER JOIN COUNTRIES c ON l.COUNTRY_ID = c.COUNTRY_ID\n" +
                "WHERE e.EMPLOYEE_ID IN (SELECT DISTINCT manager_id FROM employees)");


        while (managers.next()) {
            log.debug("Managers as XML: {}", managers.getString("managers"));
        }


    }



}
