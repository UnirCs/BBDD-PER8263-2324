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
            selectEmpleadoDepartAsXml(connection);
            selectJefesAsXml(connection);

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

    /*Mostrar el nombre y apellido de un empleado
        junto con el nombre de su departamento
    */

    private static void selectEmpleadoDepartAsXml(Connection connection) throws SQLException
    {
                String sql="SELECT\n" +
                        "    XMLELEMENT(\"empleados\",\n" +
                        "               XMLATTRIBUTES(e.first_name AS \"nombre\",\n" +
                        "               e.last_name AS \"apellidos\",\n" +
                        "               d.department_name AS \"departamento\"))\n" +
                        "        AS EmpleadosXML\n" +
                        "FROM   employees e\n" +
                        "JOIN departments d ON e.department_id=d.department_id";



        PreparedStatement selectEmployeesWithDept = connection.prepareStatement(sql);

        ResultSet employees = selectEmployeesWithDept.executeQuery();
        while (employees.next()) {
            log.debug("Empleado XML: {}", employees.getString("EmpleadosXML"));
        }
    }

    private static void selectJefesAsXml(Connection connection) throws SQLException {
        String sql = "SELECT\n" +
                "    XMLELEMENT(\"managers\",\n" +
                "               XMLAGG(\n" +
                "                       XMLELEMENT(\"manager\",\n" +
                "                                  XMLELEMENT(\"nombreCompleto\",\n" +
                "                                             XMLELEMENT(\"nombre\", e.first_name),\n" +
                "                                             XMLELEMENT(\"apellido\", e.last_name)\n" +
                "                                  ),\n" +
                "                                  XMLELEMENT(\"department\", d.department_name),\n" +
                "                                  XMLELEMENT(\"city\", l.city ),\n" +
                "                                  XMLELEMENT(\"country\",c.country_name )\n" +
                "                       )\n" +
                "               )\n" +
                "    )\n" +
                "  as  ManagersXML\n" +
                "  from employees e\n" +
                "  JOIN departments d ON e.department_id=d.department_id\n" +
                "  JOIN locations l ON d.location_id = l.location_id\n" +
                "  JOIN countries c ON l.country_id = c.country_id\n" +
                "  JOIN jobs j ON e.JOB_ID=j.JOB_ID\n" +
                "  where j.JOB_TITLE LIKE '%Manager'";

        PreparedStatement selectManagers = connection.prepareStatement(sql);

        ResultSet managers = selectManagers.executeQuery();
        while (managers.next()) {
            log.debug("Manager XML: {}", managers.getString("ManagersXml"));
        }
    }

}

