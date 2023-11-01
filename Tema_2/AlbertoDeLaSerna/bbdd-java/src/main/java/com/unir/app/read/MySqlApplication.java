package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MySqlApplication {

    private static final String DATABASE = "employees";

    public static void main(String[] args) {


        //Creamos conexión. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos Oracle");
            // Estas son las que nos proporciona el profesor
            //selectAllEmployeesOfDepartment(connection, "d001");
            //selectAllEmployeesOfDepartment(connection, "d002");

            // Estas son las que he hecho yo∫
            numberOfEmployeesGroupByGenere(connection);
            getTheBestSalaryEmployee(connection, "d001");
            getTheSecondBestSalaryEmployee(connection, "d001");
            getTotalOfEmpoyeesHired(connection, 10);
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

    /**
     * Número de personas empleadas agrupadas por género (Hombre, Mujer)
     * @param connection
     * @throws SQLException
     */
    private static void numberOfEmployeesGroupByGenere(Connection connection) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select count(*) as total, gender from employees group by gender order by total desc;");
        ResultSet employees = selectEmployees.executeQuery();
        log.debug("Número de empleados agrupados por género");
        while (employees.next()) {
            log.debug("Total de {}: {}",
                    employees.getString("gender"),
                    employees.getString("total"));
        }
    }

    /**
     *  Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto
     *  @param connection
     *  @param department
     *  @throws SQLException
     */
    private static void getTheBestSalaryEmployee(Connection connection, String department) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select first_name, last_name, salary\n" +
                "from employees\n" +
                "inner join dept_emp on employees.emp_no = dept_emp.emp_no\n" +
                "inner join departments on dept_emp.dept_no = departments.dept_no and dept_emp.emp_no = employees.emp_no\n" +
                "inner join salaries on employees.emp_no = salaries.emp_no\n" +
                "where dept_emp.dept_no = ?\n" +
                "order by salary desc\n"+
                "limit 1");
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Mejor persona pagada del departamento {} con el salario {}: {} {}",
                    department,
                    employees.getDouble("salary"),
                    employees.getString("first_name"),
                    employees.getString("last_name")
                    );
        }
    }

    /**
     *  Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto
     *  @param connection
     *  @param department
     *  @throws SQLException
     */
    private static void getTheSecondBestSalaryEmployee(Connection connection, String department) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select first_name, last_name, salary\n" +
                "from employees\n" +
                "inner join dept_emp on employees.emp_no = dept_emp.emp_no\n" +
                "inner join departments on dept_emp.dept_no = departments.dept_no and dept_emp.emp_no = employees.emp_no\n" +
                "inner join salaries on employees.emp_no = salaries.emp_no\n" +
                "where dept_emp.dept_no = ?\n" +
                "order by salary desc\n"+
                "limit 1,1");
        selectEmployees.setString(1, department);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Mejor persona pagada del departamento {} con el salario {}: {} {}",
                    department,
                    employees.getDouble("salary"),
                    employees.getString("first_name"),
                    employees.getString("last_name")
            );
        }
    }

    /**
     * Mostrar el número de empleados contratados en un mes concreto
     * @param connection
     * @param month
     * @throws SQLException
     */
    private static void getTotalOfEmpoyeesHired(Connection connection, int month) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select count(*) as numHireEmployees\n" +
                "from employees\n" +
                "where month(employees.hire_date) = ?;\n");
        selectEmployees.setInt(1, month);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("El número de empleados contratados en el mes de {} es: {}",
                    month,
                    employees.getInt("numHireEmployees")
            );
        }
    }
}
