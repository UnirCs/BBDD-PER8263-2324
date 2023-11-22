package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MySqlApplication {

    private static final String DATABASE = "employees";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos Oracle");

            selectAllEmployeesOfDepartment(connection, "d001");
            selectAllEmployeesOfDepartment(connection, "d002");
            // Obtener el total de empleados y empleadas
            getEmployeeCountByGender(connection);
            // Obtener el empleado con el salario más alto del departamento de Marketing
            getEmployeeHighestSalaryWithDeparmentAndPosition(connection, "Marketing", 1);
            // Obtener el segundo empleado con el salario más alto del departamento de Marketing
            getEmployeeHighestSalaryWithDeparmentAndPosition(connection, "Marketing", 2);
            // Obtener empleados contratados en enero de 1999
            getEmployeesByMonthAndYear(connection, "1", "1999");

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando Statement.
     * Statement es la forma más básica de ejecutar consultas a la base de datos.
     * Es la más insegura, ya que no se protege de ataques de inyección SQL.
     * No obstante es útil para sentencias DDL.
     * @param connection
     * @throws SQLException
     */
    private static void selectAllEmployees(Connection connection) throws SQLException {
        Statement selectEmployees = connection.createStatement();
        ResultSet employees = selectEmployees.executeQuery("select * from employees");

        while (employees.next()) {
            log.debug("Employee: {} {}",
                    employees.getString("first_name"),
                    employees.getString("last_name"));
        }
    }

    /**
     * Ejemplo de consulta a la base de datos usando PreparedStatement.
     * PreparedStatement es la forma más segura de ejecutar consultas a la base de datos.
     * Se protege de ataques de inyección SQL.
     * Es útil para sentencias DML.
     * @param connection
     * @throws SQLException
     */
    private static void selectAllEmployeesOfDepartment(Connection connection, String department) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select count(*) as 'Total'\n" +
                "from employees emp\n" +
                "inner join dept_emp dep_rel on emp.emp_no = dep_rel.emp_no\n" +
                "inner join departments dep on dep_rel.dept_no = dep.dept_no\n" +
                "where dep_rel.dept_no = ?;\n");
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug(
                "Empleados del departamento {}: {}",
                department,
                employees.getString("Total")
            );
        }
    }

    /**
     * Obtener el total de empleados y empleadas.
     * @param connection
     * @throws SQLException
     */
    public static void getEmployeeCountByGender(Connection connection) throws SQLException {
        String sql = "SELECT employees.gender AS genero, COUNT(*) AS total "
                + "FROM employees.employees "
                + "GROUP BY gender "
                + "ORDER BY total DESC";

        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String genero = resultSet.getString("genero");
            int total = resultSet.getInt("total");
            log.info("Género: " + genero + ", Total: " + total);
        }
    }

    /**
     * Obtener el empleado con el salario más alto de un departamento y posición concretos.
     * @param connection
     * @throws SQLException
     */
    public static void getEmployeeHighestSalaryWithDeparmentAndPosition(Connection connection, String nombreDepartamento, int posicion) throws SQLException {
        String sql = "SELECT employees.first_name AS nombre, "
                + "employees.last_name AS apellidos, "
                + "salaries.salary AS salario "
                + "FROM employees.employees "
                + "INNER JOIN employees.salaries "
                + "ON employees.emp_no = salaries.emp_no "
                + "WHERE salaries.to_date = '9999-01-01' "
                + "AND employees.emp_no IN ( "
                + "SELECT dept_emp.emp_no "
                + "FROM employees.dept_emp "
                + "WHERE dept_no = ( "
                + "SELECT dept_no "
                + "FROM employees.departments "
                + "WHERE dept_name = ? "
                + ") "
                + ") "
                + "ORDER BY salaries.salary DESC "
                + "LIMIT ?, 1";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, nombreDepartamento);
        statement.setInt(2, posicion - 1);


        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String nombre = resultSet.getString("nombre");
            String apellidos = resultSet.getString("apellidos");
            double salario = resultSet.getDouble("salario");
            log.info("Nombre: " + nombre + ", Apellidos: " + apellidos + ", Salario: " + salario);
        }

    }

    /**
     * Obtener los empleados que se han contratado en un mes y año concretos.
     * @param connection
     * @throws SQLException
     */
    public static void getEmployeesByMonthAndYear(Connection connection, String mes, String anio) throws SQLException {
        String sql = "SELECT employees.first_name AS nombre, "
                + "employees.last_name AS apellidos, "
                + "employees.hire_date AS fecha_contratacion "
                + "FROM employees.employees "
                + "WHERE MONTH(employees.hire_date) = ? "
                + "AND YEAR(employees.hire_date) = ? "
                + "ORDER BY employees.hire_date DESC";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, mes);
        statement.setString(2, anio);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String nombre = resultSet.getString("nombre");
            String apellidos = resultSet.getString("apellidos");
            String fechaContratacion = resultSet.getString("fecha_contratacion");
            log.info("Nombre: " + nombre + ", Apellidos: " + apellidos + ", Fecha de Contratación: " + fechaContratacion);
        }
    }
}
