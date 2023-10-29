package com.unir.app.read;

import com.unir.config.OracleDatabaseConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class OracleApplication {

    private static final String SERIVCE_NAME = "orcl";

    public static void main(String[] args) {

        //Creamos conexión. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new OracleDatabaseConnector("localhost", SERIVCE_NAME).getConnection()) {

            log.debug("Conexión establecida con la base de datos Oracle");

            //Ejemplos de consultas a la base de datos
            // selectAllEmployees(connection);
            // selectAllCountriesAsXml(connection);

            // Ejercicios planteados
            log.debug("Ejercicio 1: Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento");
            getEmployeesFromDepartament(connection, 60);
            log.debug("Ejercicio 2: Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers");
            getManagerEmployees(connection);

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
     * Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
     * @param connection
     * @param departament
     * @throws SQLException
     */
    private static void getEmployeesFromDepartament(Connection connection, Integer departament) throws SQLException {
        PreparedStatement selectCountries = connection.prepareStatement("SELECT\n" +
                "  XMLELEMENT(\"empleados\",\n" +
                "       XMLATTRIBUTES(\n" +
                "         e.FIRST_NAME AS \"nombre\",\n" +
                "         e.LAST_NAME AS \"apellidos\",\n" +
                "         d.DEPARTMENT_NAME AS \"departamento\"))\n" +
                "  AS employee\n" +
                "from   EMPLOYEES e\n" +
                "    inner join DEPARTMENTS d on e.DEPARTMENT_ID = d.DEPARTMENT_ID\n" +
                "where d.DEPARTMENT_ID = ?");
        selectCountries.setInt(1, departament);

        ResultSet countries = selectCountries.executeQuery();
        while (countries.next()) {
            log.debug("Empleado as XML: {}", countries.getString("employee"));
        }
    }

    /**
     * Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers
     * @param connection
     * @throws SQLException
     */
    private static void getManagerEmployees(Connection connection) throws SQLException {
        PreparedStatement selectCountries = connection.prepareStatement("SELECT\n" +
                "XMLELEMENT(\"managers\",\n" +
                "    XMLAGG(\n" +
                "        XMLELEMENT(\"manager\",\n" +
                "            XMLELEMENT(\"nombreCompleto\",\n" +
                "                XMLELEMENT(\"nombre\",e.FIRST_NAME),\n" +
                "                XMLELEMENT(\"apellido\",e.LAST_NAME)\n" +
                "            ),\n" +
                "            XMLELEMENT(\"department\",d.DEPARTMENT_NAME),\n" +
                "            XMLELEMENT(\"city\",l.CITY),\n" +
                "            XMLELEMENT(\"country\",c.COUNTRY_NAME)\n" +
                "        )\n" +
                "    )\n" +
                ") AS managers\n" +
                "from EMPLOYEES e\n" +
                "    inner join JOBS j on j.JOB_ID = e.JOB_ID\n" +
                "    inner join DEPARTMENTS d on e.DEPARTMENT_ID = e.DEPARTMENT_ID\n" +
                "    inner join LOCATIONS l on d.LOCATION_ID = l.LOCATION_ID\n" +
                "    inner join COUNTRIES c on l.COUNTRY_ID = c.COUNTRY_ID\n" +
                "where j.JOB_TITLE like ?");
        selectCountries.setString(1, "%Manager%");

        ResultSet countries = selectCountries.executeQuery();
        while (countries.next()) {
            log.debug("Managers as XML: {}", countries.getString("managers"));
        }
    }
}
