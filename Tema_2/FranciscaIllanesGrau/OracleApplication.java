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

            selectEmployeesWithDepartmentAsXml(connection);
            selectManagersAsXml(connection);

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
    private static void selectEmployeesWithDepartmentAsXml(Connection connection) throws SQLException {
        String sql = "SELECT XMLELEMENT(\"empleados\", " +
                "XMLATTRIBUTES(e.first_name AS \"nombre\", " +
                "e.last_name AS \"apellidos\", " +
                "d.department_name AS \"departamento\")) AS EmployeeXml " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.department_id";

        PreparedStatement selectEmployeesWithDept = connection.prepareStatement(sql);

        ResultSet employees = selectEmployeesWithDept.executeQuery();
        while (employees.next()) {
            log.debug("Employee as XML: {}", employees.getString("EmployeeXml"));
        }
    }

    private static void selectManagersAsXml(Connection connection) throws SQLException {
        String sql = "SELECT " +
                "XMLELEMENT(\"managers\"," +
                "    XMLAGG(" +
                "        XMLELEMENT(\"manager\"," +
                "            XMLELEMENT(\"nombreCompleto\"," +
                "                XMLELEMENT(\"nombre\", e.first_name)," +
                "                XMLELEMENT(\"apellido\", e.last_name)" +
                "            )," +
                "            XMLELEMENT(\"department\", d.department_name)," +
                "            XMLELEMENT(\"city\", l.city)," +
                "            XMLELEMENT(\"country\", c.country_name)" +
                "        )" +
                "    )" +
                ") AS ManagersXml " +
                "FROM employees e " +
                "JOIN departments d ON e.employee_id = d.manager_id " +
                "JOIN locations l ON d.location_id = l.location_id " +
                "JOIN countries c ON l.country_id = c.country_id " +
                "WHERE e.job_id LIKE '%MAN%'";

        PreparedStatement selectManagers = connection.prepareStatement(sql);

        ResultSet managers = selectManagers.executeQuery();
        while (managers.next()) {
            log.debug("Managers as XML: {}", managers.getString("ManagersXml"));
        }
    }
}
