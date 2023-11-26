package com.unir.app.read;

import com.unir.config.OracleDatabaseConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class OracleApplication {

    private static final String HOST = "localhost";
    private static final String SERIVCE_NAME = "orcl";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Connection connection = new OracleDatabaseConnector(HOST, SERIVCE_NAME).getConnection()) {

            log.debug("Conexión establecida con la base de datos Oracle");

            // Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
            select_01(connection);

            // Mostrar el nombre, apellido, nombre de departamento, ciudad y pais
            // de los empleados que son Managers.
            select_02(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * 01 Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
     *
     * @param connection conexión
     * @throws SQLException
     */
    private static void select_01(Connection connection) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT\n" +
                        "  XMLELEMENT(\"empleados\",\n" +
                        "    XMLATTRIBUTES(\n" +
                        "      e.FIRST_NAME AS \"nombre\",\n" +
                        "      e.LAST_NAME AS \"apellidos\",\n" +
                        "      d.DEPARTMENT_NAME AS \"departamento\"))\n" +
                        "  AS Empleados\n" +
                        "FROM EMPLOYEES e\n" +
                        "INNER JOIN HR.DEPARTMENTS d USING (DEPARTMENT_ID)");
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug(employees.getString("Empleados"));
        }
    }

    /**
     * 02 Mostrar el nombre, apellido, nombre de departamento, ciudad y pais
     * de los empleados que son Managers.
     *
     * @param connection conexión
     * @throws SQLException
     */
    private static void select_02(Connection connection) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "SELECT XMLELEMENT(\"managers\",\n" +
                        "    XMLAGG(\n" +
                        "      XMLELEMENT(\"manager\",\n" +
                        "        XMLELEMENT(\"nombreCompleto\",\n" +
                        "            XMLFOREST(E.FIRST_NAME AS \"nombre\",\n" +
                        "                      E.LAST_NAME AS \"apellido\")),\n" +
                        "        XMLFOREST(D.DEPARTMENT_NAME AS \"department\",\n" +
                        "                  L.CITY AS \"city\",\n" +
                        "                  C.COUNTRY_NAME AS \"country\"))\n" +
                        "    )) AS Managers\n" +
                        "FROM EMPLOYEES E\n" +
                        "INNER JOIN HR.DEPARTMENTS D USING (DEPARTMENT_ID)\n" +
                        "INNER JOIN HR.LOCATIONS L USING (LOCATION_ID)\n" +
                        "INNER JOIN HR.COUNTRIES C USING (COUNTRY_ID)\n" +
                        "WHERE E.EMPLOYEE_ID IN (\n" +
                        "    SELECT M.MANAGER_ID\n" +
                        "    FROM EMPLOYEES M\n" +
                        "    GROUP BY M.MANAGER_ID)");
        ResultSet managers = selectEmployees.executeQuery();

        while (managers.next()) {
            log.debug(managers.getString("Managers"));
        }
    }


    /**
     * Ejemplo de consulta a la base de datos usando Statement.
     * Statement es la forma más básica de ejecutar consultas a la base de datos.
     * Es la más insegura, ya que no se protege de ataques de inyección SQL.
     * No obstante, es útil para sentencias DDL.
     *
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
}