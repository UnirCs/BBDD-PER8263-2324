package com.unir.app.read;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.unir.config.OracleDatabaseConnector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OracleApplication {

	private static final String SERVICE_NAME = "orcl";

	public static void main(String[] args) {

		// Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
		// Try-with-resources. Se cierra la conexión automáticamente al salir del bloque
		// try
		try (Connection connection = new OracleDatabaseConnector("localhost", SERVICE_NAME).getConnection()) {

			log.debug("Conexión establecida con la base de datos Oracle");

			//Primera SQLX
			getEmployeesFromDepartament(connection, 80);
			
			log.debug("\n");
			
			//Segunda SQLX
			getManagerEmployees(connection);

		} catch (Exception e) {
			log.error("Error al tratar con la base de datos", e);
		}
	}

	/**
	 * Ejemplo de consulta a la base de datos usando Statement. Statement es la
	 * forma más básica de ejecutar consultas a la base de datos. Es la más
	 * insegura, ya que no se protege de ataques de inyección SQL. No obstante, es
	 * útil para sentencias DDL.
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	private static void selectAllEmployees(Connection connection) throws SQLException {
		Statement selectEmployees = connection.createStatement();
		ResultSet employees = selectEmployees.executeQuery("select * from EMPLOYEES");

		while (employees.next()) {
			log.debug("Employee: {} {}", employees.getString("FIRST_NAME"), employees.getString("LAST_NAME"));
		}
	}

	/**
	 * Ejemplo de consulta a la base de datos usando PreparedStatement y SQL/XML.
	 * Para usar SQL/XML, es necesario que la base de datos tenga instalado el
	 * módulo XDB. En Oracle 19c, XDB viene instalado por defecto. Ademas, se
	 * necesitan las dependencias que se encuentran en el pom.xml.
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	private static void selectAllCountriesAsXml(Connection connection) throws SQLException {
		PreparedStatement selectCountries = connection.prepareStatement("SELECT\n"
				+ "  XMLELEMENT(\"countryXml\",\n"
				+ "       XMLATTRIBUTES(\n" 
				+ "         c.country_name AS \"name\",\n"
				+ "         c.region_id AS \"code\",\n" 
				+ "         c.country_id AS \"id\"))\n" 
				+ "  AS CountryXml\n"
				+ "FROM  countries c\n" 
				+ "WHERE c.country_name LIKE ?");
		selectCountries.setString(1, "S%");

		ResultSet countries = selectCountries.executeQuery();
		while (countries.next()) {
			log.debug("Country as XML: {}", countries.getString("CountryXml"));
		}
	}

	/**
	 * Recupera nombre y apellidos y departamento de los empleados de un departamento recibido como parámetro
	 * 
	 * @param connection
	 * @param dptoNumber
	 * @throws SQLException
	 */
	private static void getEmployeesFromDepartament(Connection connection, Integer dptoNumber) throws SQLException {
		PreparedStatement preparedSTMT = connection.prepareStatement(""
				+ "select XMLELEMENT(\"empleados\",\n"
				+ "                  XMLATTRIBUTES(\n"
				+ "           empleados.FIRST_NAME AS \"nombre\",\n"
				+ "           empleados.LAST_NAME AS \"apellidos\",\n"
				+ "           departamento.DEPARTMENT_NAME AS \"departamento\")\n"
				+ "       ) as EMPLEADO\n"
				+ "from   EMPLOYEES empleados\n"
				+ "    inner join DEPARTMENTS departamento on empleados.DEPARTMENT_ID = departamento.DEPARTMENT_ID\n"
				+ "where departamento.DEPARTMENT_ID = ?");
		preparedSTMT.setInt(1, dptoNumber);

		ResultSet empleados = preparedSTMT.executeQuery();
		while (empleados.next()) {
			log.debug("Empleado as XML: {}", empleados.getString("EMPLEADO"));
		}
	}

	/**
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	private static void getManagerEmployees(Connection connection) throws SQLException {
		PreparedStatement preparedSTMT = connection.prepareStatement("SELECT XMLELEMENT(\"managers\",\n"
				+ "    XMLAGG(\n"
				+ "        XMLELEMENT(\"manager\",\n"
				+ "            XMLELEMENT(\"nombreCompleto\",\n"
				+ "                XMLFOREST(\n"
				+ "                    empleados.FIRST_NAME as \"nombre\",\n"
				+ "                    empleados.LAST_NAME as \"apellido\"\n"
				+ "                )),\n"
				+ "            XMLELEMENT(\"department\",departamento.DEPARTMENT_NAME),\n"
				+ "            XMLELEMENT(\"city\",location.CITY),\n"
				+ "            XMLELEMENT(\"country\",pais.COUNTRY_NAME)\n"
				+ "        )\n"
				+ "    )\n"
				+ "    ) AS MANAGER\n"
				+ "from EMPLOYEES empleados\n"
				+ "    inner join DEPARTMENTS departamento on empleados.DEPARTMENT_ID = empleados.DEPARTMENT_ID\n"
				+ "    inner join JOBS jobs on jobs.JOB_ID = empleados.JOB_ID\n"
				+ "    inner join LOCATIONS location on departamento.LOCATION_ID = location.LOCATION_ID\n"
				+ "    inner join COUNTRIES pais on location.COUNTRY_ID = pais.COUNTRY_ID\n"
				+ "where jobs.JOB_TITLE like ?");
		preparedSTMT.setString(1, "%Manager%");

		ResultSet empleados = preparedSTMT.executeQuery();
		while (empleados.next()) {
			log.debug("Managers as XML: {}", empleados.getString("MANAGER"));
		}
	}
}
