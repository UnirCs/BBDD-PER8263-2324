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
        try(Connection connection = new OracleDatabaseConnector("localhost:1522", SERIVCE_NAME).getConnection()) {

            log.debug("Conexión establecida con la base de datos Oracle");

            selectAllEmployees(connection);
            selectAllCountriesAsXml(connection);
            selectEmpleadosDep(connection);
            selectEmpleadosManag(connection);

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

    /** Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
     * @param connection
     * @throws SQLException
     */
    private static void selectEmpleadosDep(Connection connection) throws SQLException {
        PreparedStatement selectEmplDep = connection.prepareStatement(
                " select XMLELEMENT (\"empleados\",\n" +
                        " XMLATTRIBUTES(\n" +
                            "   e.FIRST_NAME as \"nombre\",\n" +
                            "   e.LAST_NAME as \"apellidos\",\n" +
                            "    d.DEPARTMENT_NAME as \"departamento\" )\n" +
                            " ) as empleados\n" +
                            " from employees e , DEPARTMENTS d\n" +
                            " where e.DEPARTMENT_ID = d.DEPARTMENT_ID");

        ResultSet countries = selectEmplDep.executeQuery();
        while (countries.next()) {
            log.debug("Country as XML: {}", countries.getString("CountryXml"));
        }
    }

    /** Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers.
     * @param connection
     * @throws SQLException
     */
    private static void selectEmpleadosManag(Connection connection) throws SQLException {
        PreparedStatement selectEmpleadosManag = connection.prepareStatement(
                "  SELECT XMLELEMENT (\"managers\",\n" +
                        "  XMLAGG(\n" +
                        "  XMLELEMENT(\"manager\",\n" +
                        "  XMLELEMENT(\"nombreCompleto\",\n" +
                        "  XMLFOREST(\n" +
                        "  e.FIRST_NAME as \"nombre\",\n" +
                        "  e.LAST_NAME as \"apellido\"\n" +
                        "  )),\n" +
                        "  XMLELEMENT(\"department\",d.DEPARTMENT_NAME),\n" +
                        "   XMLELEMENT(\"city\",l.CITY),\n" +
                        "  XMLELEMENT(\"country\",c.COUNTRY_NAME)\n" +
                        "  )\n" +
                        "  )\n" +
                        "  ) AS managers\n" +
                        "  from EMPLOYEES e, JOBS j, DEPARTMENTS d, LOCATIONS l, COUNTRIES c\n" +
                        "  where j.JOB_ID = e.JOB_ID\n" +
                        "  and e.DEPARTMENT_ID = e.DEPARTMENT_ID\n" +
                        "  and d.LOCATION_ID = l.LOCATION_ID\n" +
                        "  and l.COUNTRY_ID = c.COUNTRY_ID\n" +
                        "  and j.JOB_TITLE like '%Manager%'");

        ResultSet countries = selectEmpleadosManag.executeQuery();
        while (countries.next()) {
            log.debug("Country as XML: {}", countries.getString("CountryXml"));
        }
    }
}
