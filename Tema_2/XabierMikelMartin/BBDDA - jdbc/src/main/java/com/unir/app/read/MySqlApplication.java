package com.unir.app.read;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
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

            log.info("Conexión establecida con la base de datos Oracle\n\n");

            countMenAndWomen(connection);
            firstPersonWithBestSalary(connection,"Marketing");
            secondPersonWithBestSalary(connection,"Marketing");
            numberOfHiredIn(connection,7);

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
            log.debug("Empleados del departamento {}: {} ",
                    department,
                    employees.getString("Total"));
        }
    }

    /*
        Ejercicio 1
        Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente
     */
    private static void countMenAndWomen(Connection connection) throws SQLException {
        Statement selectEmployees = connection.createStatement();
        ResultSet employees = selectEmployees.executeQuery("select gender as 'sexo', count(*) as 'cantidad' from employees.employees group by gender order by cantidad desc");

        log.info("Consulta 1 - Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente");
        while (employees.next()) {
            log.debug("Employee: {} {}",
                    employees.getString("sexo"),
                    employees.getString("cantidad"));
        }


    }

    /*
        Ejercicio 2
        Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable)
     */
    private static void firstPersonWithBestSalary(Connection connection, String department) throws SQLException {

        log.info("Consulta 2 - Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable)");

        PreparedStatement selectEmployees = connection.prepareStatement("Select e.first_name,e.last_name,s.salary from employees.employees e " +
                "inner join employees.salaries s on e.emp_no = s.emp_no "
                        +"inner join employees.dept_emp de on e.emp_no = de.emp_no "
                        +"inner join employees.departments d on de.dept_no = d.dept_no "
                        +"where d.dept_name = ? "
                        +"order by s.salary desc Limit 1");

        selectEmployees.setString(1, department);

        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Persona con mejor salario del departamento {}: {} {} {}",
                    department,
                    employees.getString("first_name"),employees.getString("last_name"),employees.getString("salary"));
        }
    }

    /*
      Ejercicio 3
      Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
   */
    private static void secondPersonWithBestSalary(Connection connection, String department) throws SQLException {

        log.info("Consulta 3 - Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable)");

        PreparedStatement selectEmployees = connection.prepareStatement("Select e.first_name,e.last_name,s.salary from employees.employees e " +
                "inner join employees.salaries s on e.emp_no = s.emp_no "
                +"inner join employees.dept_emp de on e.emp_no = de.emp_no "
                +"inner join employees.departments d on de.dept_no = d.dept_no "
                +"where d.dept_name = ? "
                +"order by s.salary desc Limit 1 offset 1");

        selectEmployees.setString(1, department);

        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Segunda persona con mejor salario del departamento {}: {} {} {}",
                    department,
                    employees.getString("first_name"),employees.getString("last_name"),employees.getString("salary"));
        }
    }

    /*
      Ejercicio 4
       Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
   */
    private static void numberOfHiredIn (Connection connection, int month) throws SQLException {

        log.info("Consulta 4 - Mostrar el número de empleados contratados en un mes concreto (parámetro variable).");

        PreparedStatement selectEmployees = connection.prepareStatement("select count(*) as 'contrataciones' from employees.employees where Month(hire_date) = ?");

        selectEmployees.setInt(1, month);

        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Numero de contrataciones en el mes {}: {} ",
                    month,
                    employees.getString("contrataciones"));
        }
    }
}


