
package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MySqlApplication {

    private static final String DATABASE = "employees";
    public static String sql01 ="SELECT gender, COUNT(*) AS cantidad\r\n" + //
                "FROM employees.employees\r\n" + //
                "GROUP BY gender\r\n" + //
                "ORDER BY cantidad DESC;";

    public static String sql02 ="SELECT e.first_name, e.last_name, s.salary, d.dept_no\r\n" + //
                "FROM employees e\r\n" + //
                "JOIN salaries s ON e.emp_no = s.emp_no\r\n" + //
                "JOIN dept_emp de ON e.emp_no = de.emp_no\r\n" + //
                "JOIN departments d ON de.dept_no = d.dept_no\r\n" + //
                "WHERE d.dept_no = ? \r\n" + //
                "ORDER BY s.salary DESC\r\n" + //
                "LIMIT 1;";
        
    public static String sql03 ="SELECT e.first_name, e.last_name, s.salary, d.dept_name\r\n" + //
                "FROM employees e\r\n" + //
                "JOIN salaries s ON e.emp_no = s.emp_no\r\n" + //
                "JOIN dept_emp de ON e.emp_no = de.emp_no\r\n" + //
                "JOIN departments d ON de.dept_no = d.dept_no\r\n" + //
                "WHERE d.dept_no = ? \r\n" + //
                "ORDER BY s.salary DESC\r\n" + //
                "LIMIT 1 OFFSET 1";

    public static String sql04 ="SELECT COUNT(*) AS numero_empleados_contratados\r\n" + //
                "FROM employees\r\n" + //
                "WHERE MONTH(hire_date) = ?;";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos Oracle");

            /*selectAllEmployees(connection);*/
            selectAllEmployeesOfDepartment(connection, "d001");
            selectAllEmployeesOfDepartment(connection, "d002");
            hombresyMujeres(connection);
            mejorPagada(connection, "d002");
            segundaPagada(connection, "d002");
            contratacionMes(connection, "11");
            
            

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
            log.debug("Empleados del departamento {}: {}",
                    department,
                    employees.getString("Total"));
        }
    }
    private static void hombresyMujeres(Connection connection) throws SQLException {


        

        Statement selectEmployees = connection.createStatement();
        ResultSet employees = selectEmployees.executeQuery(sql01);
        

        while (employees.next()) {
            String gender = employees.getString("gender");
            int cantidad = employees.getInt("cantidad");
    
            if ("M".equals(gender)) {
                log.debug("Hombres: {}", cantidad);
            } else if ("F".equals(gender)) {
                log.debug("Mujeres: {}", cantidad);
            }
        }
        
    }
    private static void mejorPagada(Connection connection, String department ) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(sql02);
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Employee: {} {}",
                    employees.getString("first_name"),
                    employees.getString("last_name"),
                    employees.getString("salary"));
        }
    }
    private static void segundaPagada(Connection connection, String department ) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(sql03);
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Employee: {} {}",
                    employees.getString("first_name"),
                    employees.getString("last_name"),
                    employees.getString("salary"));
        }
    }
    private static void contratacionMes(Connection connection, String date ) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement(sql04);
        selectEmployees.setString(1, date);
        ResultSet employees = selectEmployees.executeQuery();
        
        while (employees.next()) {
             int cantidad = employees.getInt("numero_empleados_contratados");
            log.debug("Empleados contratados en el mes {}: {}", date, cantidad);
        }
    }
}
