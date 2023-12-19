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
        try (Connection connection = new OracleDatabaseConnector("localhost", SERIVCE_NAME).getConnection()) {

            log.debug("Conexión establecida con la base de datos Oracle");

            selectEmployeesXML(connection);
            selectAllManagersXML(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento. Cada resultado XML devuelto por la consulta (la consulta debe devolver 1 registro por empleado) debe ser válido frente a este XML Schema
     * <?xml version="1.0" encoding="UTF-8"?>
     * <xs:schema
     * xmlns:xs="http://www.w3.org/2001/XMLSchema"
     * xmlns:xdb="http://xmlns.oracle.com/xdb">
     * <xs:element name="empleados">
     * <xs:complexType>
     * <xs:attribute name="nombre" type="xs:string" />
     * <xs:attribute name="apellidos" type="xs:string" />
     * <xs:attribute name="departamento" type="xs:string"/>
     * </xs:complexType>
     * </xs:element>
     * </xs:schema> .
     *
     * @param connection
     * @throws SQLException
     */
    private static void selectEmployeesXML(Connection connection) throws SQLException {
        Statement selectEmployees = connection.createStatement();

        ResultSet employees = selectEmployees.executeQuery("SELECT XMLELEMENT(\"empleados\", " +
                "XMLATTRIBUTES(E.first_name AS \"nombre\", " +
                "              E.last_name AS \"apellidos\", " +
                "              D.DEPARTMENT_NAME AS \"departamento\")" +
                ") AS empleado\n" +
                "from EMPLOYEES E INNER JOIN DEPARTMENTS D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID");

        while (employees.next()) {
            log.debug("Employee: {}",
                    employees.getString("empleado"));
        }
    }

    /**
     * (Debes usar XMLELEMENT, XMLAGG y XMLFOREST) Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers. El XML devuelto por la consulta (debe devolver un único registro, con todos los managers) debe ser válido frente a este XML Schema:
     * <p>
     * <?xml version = "1.0" encoding="UTF-8"?>
     * <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
     * xmlns:xdb="http://xmlns.oracle.com/xdb" elementFormDefault="qualified">
     * <xs:element name="managers">
     * <xs:complexType>
     * <xs:sequence>
     * <xs:element name="manager" minOccurs="0" maxOccurs="unbounded">
     * <xs:complexType>
     * <xs:sequence>
     * <xs:element name ="nombreCompleto">
     * <xs:complexType>
     * <xs:sequence>
     * <xs:element name = "nombre" type="xs:string"/>
     * <xs:element name = "apellido" type="xs:string"/>
     * </xs:sequence>
     * </xs:complexType>
     * </xs:element>
     * <xs:element name = "department" type="xs:string"/>
     * <xs:element name = "city" type="xs:string"/>
     * <xs:element name = "country" type="xs:string"/>
     * </xs:sequence>
     * </xs:complexType>
     * </xs:element>
     * </xs:sequence>
     * </xs:complexType>
     * </xs:element>
     * </xs:schema>
     *
     * @param connection
     * @throws SQLException
     */
    private static void selectAllManagersXML(Connection connection) throws SQLException {
        Statement selectEmployees = connection.createStatement();
        ResultSet managers = selectEmployees.executeQuery(
                "Select XMLELEMENT(\"managers\",\n" +
                        " XMLAGG(\n" +
                        "  XMLFOREST(\n" +
                        "   XMLFOREST(\n" +
                        "    XMLFOREST(\n" +
                        "     E.FIRST_NAME AS \"nombre\",\n" +
                        "     E.LAST_NAME AS \"apellido\"\n" +
                        "    ) AS \"nombreCompleto\",\n" +
                        "     DEPARTMENT_NAME AS \"department\",\n" +
                        "     CITY AS \"city\",\n" +
                        "     COUNTRY_NAME AS \"country\"\n" +
                        "   ) AS \"manager\"\n" +
                        "  )\n" +
                        " )\n" +
                        ") AS Managers\n" +
                        "from ((((EMPLOYEES E inner join HR.JOBS J on J.JOB_ID = E.JOB_ID) INNER JOIN DEPARTMENTS D\n" +
                        "on E.DEPARTMENT_ID = D.DEPARTMENT_ID) INNER JOIN LOCATIONS L\n" +
                        "on L.LOCATION_ID = D.LOCATION_ID) INNER JOIN COUNTRIES C ON L.COUNTRY_ID = C.COUNTRY_ID)\n" +
                        "Where J.JOB_TITLE like '%Manager%'");

        while (managers.next()) {
            log.debug(
                    managers.getString("Managers")
            );
        }
    }
}
