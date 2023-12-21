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

            selectEmpleadoDepartamento(connection);
            selectEmpleadoManagers(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
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
    private static void selectEmpleadoDepartamento(Connection connection) throws SQLException {
        String xml = "SELECT XMLELEMENT(\"empleados\",\n" +
                "    XMLATTRIBUTES (e.FIRST_NAME as \"nombre\",\n" +
                "                  e.LAST_NAME as \"apellidos\",\n" +
                "                  d.DEPARTMENT_NAME as \"departamento\")) as employee\n" +
                "from EMPLOYEES e\n" +
                "join DEPARTMENTS d on d.DEPARTMENT_ID = e.DEPARTMENT_ID";
        PreparedStatement selectStatement = connection.prepareStatement(xml);

        ResultSet result = selectStatement.executeQuery();
        while (result.next()) {
            log.debug("Empleado: {}",
                    result.getString("employee"));
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
    private static void selectEmpleadoManagers(Connection connection) throws SQLException {
        String xml = "SELECT XMLELEMENT(\"managers\",\n" +
                "    XMLATTRIBUTES (e.FIRST_NAME as \"nombre\",\n" +
                "                  e.LAST_NAME as \"apellido\",\n" +
                "                  d.DEPARTMENT_NAME as \"departamento\",\n" +
                "                  l.CITY as \"city\",\n" +
                "                  c.COUNTRY_NAME as \"country\")\n" +
                "       )as manager\n" +
                "from EMPLOYEES e\n" +
                "join DEPARTMENTS d on d.DEPARTMENT_ID = e.DEPARTMENT_ID\n" +
                "join LOCATIONS l on d.LOCATION_ID = l.LOCATION_ID\n" +
                "join COUNTRIES c on l.COUNTRY_ID = c.COUNTRY_ID\n" +
                "where e.MANAGER_ID IN (SELECT DISTINCT manager_id FROM employees)";
        PreparedStatement selectStatement = connection.prepareStatement(xml);

        ResultSet result = selectStatement.executeQuery();
        while (result.next()) {
            log.debug("Empleado_manager: {}",
                    result.getString("manager"));
        }
    }
}
