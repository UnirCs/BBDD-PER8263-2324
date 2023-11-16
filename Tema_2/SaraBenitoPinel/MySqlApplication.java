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
            selecthombMujeEmployees(connection);
            selectMejorPagadoEmployees(connection, "Customer Service");
            selectSMejorPagadoEmployees(connection, "Customer Service");
            selectEmployeesContratados(connection, "6");
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

    /** Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
     * @param connection
     * @throws SQLException
     */
    private static void selecthombMujeEmployees(Connection connection) throws SQLException {
        PreparedStatement selectHomMuj = connection.prepareStatement(
                " Select COUNT(emp_no) as numero, " +
                "gender as sexo\n" +
                "from employees.employees\n" +
                "group by gender\n" +
                "order by numero desc;");

        ResultSet employees = selectHomMuj.executeQuery();

        while (employees.next()) {
            log.debug("Empleados {}: {}",
                    employees.getString("numero"),
                    employees.getString("sexo"));
        }
    }

    /** Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
     * @param connection
     * @throws SQLException
     */
    private static void selectMejorPagadoEmployees(Connection connection, String department) throws SQLException {
        PreparedStatement selectMejPag = connection.prepareStatement(
                " Select e.first_name as nombre, e.last_name as apellidos, s.salary salario\n" +
                     " from employees.employees e, employees.dept_emp de, employees.departments d, employees.salaries s\n" +
                        " where\n" +
                        " e.emp_no = s.emp_no\n" +
                        " and e.emp_no = de.emp_no\n" +
                        " and de.dept_no = d.dept_no\n" +
                        " and d.dept_name in (?)\n" +
                        " ORDER BY salario DESC\n" +
                        " LIMIT 1;");
        selectMejPag.setString(1, department);
        ResultSet employees = selectMejPag.executeQuery();

        while (employees.next()) {
            log.debug("Empleado mejor pagado {}, {} : {}",
                    employees.getString("nombre"),
                    employees.getString("apellidos"),
                    employees.getString("salario"));
        }
    }

    /** Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
     * @param connection
     * @throws SQLException
     */
    private static void selectSMejorPagadoEmployees(Connection connection, String department) throws SQLException {
        PreparedStatement selectSMejPag = connection.prepareStatement(
                " Select e.first_name as nombre, e.last_name as apellidos, s.salary salario\n" +
                        " from employees.employees e, employees.dept_emp de, employees.departments d, employees.salaries s\n" +
                        " where\n" +
                        " e.emp_no = s.emp_no\n" +
                        " and e.emp_no = de.emp_no\n" +
                        " and de.dept_no = d.dept_no\n" +
                        " and d.dept_name in (?)\n" +
                        " ORDER BY salario DESC\n" +
                        " LIMIT 1 OFFSET 1;");
        selectSMejPag.setString(1, department);
        ResultSet employees = selectSMejPag.executeQuery();

        while (employees.next()) {
            log.debug("Empleado segundo mejor pagado {}, {} : {}",
                    employees.getString("nombre"),
                    employees.getString("apellidos"),
                    employees.getString("salario"));
        }
    }

    /** Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
     * @param connection
     * @throws SQLException
     */
    private static void selectEmployeesContratados(Connection connection, String mes) throws SQLException {
        PreparedStatement selectEmplContrat = connection.prepareStatement(
                " Select COUNT(*) as numContratadosMes\n" +
                        " from employees.employees\n" +
                        "  where month(hire_date) = ?;");
        selectEmplContrat.setString(1, mes);
        ResultSet employees = selectEmplContrat.executeQuery();

        while (employees.next()) {
            log.debug("Empleado contratados en el mes {}",
                    employees.getString("numContratadosMes"));
        }
    }
}
