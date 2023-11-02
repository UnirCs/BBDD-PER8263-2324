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
            selectNumeroDePersonasGenero(connection,"M");
            selectNumeroDePersonasGenero(connection,"F");
            selectListaEmpleados(connection);
            selectMejorPagadoDepartamento(connection,"d009");
            selectSegundoMejorPagadoDepartamento(connection,"d009");
            selectContratadosFecha(connection,"1989-10-1","1989-10-31");
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

    /*Obtener el número de hombres
    y mujeres de la base de datos.*/

    private static void  selectNumeroDePersonasGenero(Connection connection, String genero) throws SQLException
    {
        PreparedStatement selectEmployees = connection.prepareStatement("Select count(*) as 'Total'\n"+
        "from employees.employees \n"+
        "where gender=?;\n");
        selectEmployees.setString(1, genero);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next())
        {
            log.debug("Empleados {}: {}",
                    genero,
                    employees.getString("Total"));
        }
    }

    /*Ordenar de forma descendentela lista de empleados*/
    private static void  selectListaEmpleados(Connection connection) throws SQLException
    {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "select first_name as 'Nombre',last_name as 'Apellido' \n"+
                "from employees.employees \n"+
                "order by last_name;\n");

        ResultSet employees = selectEmployees.executeQuery();

        log.debug("Listado de empleados: \n");
        while (employees.next())
        {
            log.debug("{} {}",employees.getString("Nombre"),
                    employees.getString("Apellido"));
        }
    }

    /*
     Mostrar el nombre, apellido y salario
     de la persona mejor pagada de un
     departamento concreto (parámetro variable).
    */
    private static void  selectMejorPagadoDepartamento(Connection connection, String departamento) throws SQLException
    {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "select  first_name as 'Nombre',last_name as 'Apellido' ,salary as 'Salario' \n"+
                "from  employees.employees \n"+
                "INNER JOIN employees.salaries ON employees.emp_no = salaries.emp_no \n"+
                "INNER  JOIN employees.dept_emp ON salaries.emp_no=dept_emp.emp_no \n"+
                "where employees.dept_emp.dept_no=? \n"+
                "order by salary desc \n"+
                "LIMIT 1;");
        selectEmployees.setString(1, departamento);
        ResultSet employees = selectEmployees.executeQuery();


        while (employees.next())
        {
            log.debug("Empleado mejor pagado {} {} {} {}",departamento,employees.getString("Nombre"),
                    employees.getString("Apellido"),employees.getString("Salario"));
        }
    }

    /*
     Mostrar el nombre, apellido y salario
     de la segunda persona mejor pagada de un
     departamento concreto (parámetro variable).
    */
    private static void  selectSegundoMejorPagadoDepartamento(Connection connection, String departamento) throws SQLException
    {
        PreparedStatement selectEmployees = connection.prepareStatement(
                "select  first_name as 'Nombre',last_name as 'Apellido' ,salary as 'Salario' \n"+
                        "from  employees.employees \n"+
                        "INNER JOIN employees.salaries ON employees.emp_no = salaries.emp_no \n"+
                        "INNER  JOIN employees.dept_emp ON salaries.emp_no=dept_emp.emp_no \n"+
                        "where employees.dept_emp.dept_no=? \n"+
                        "order by salary desc \n"+
                        "LIMIT 1 OFFSET 1;");
        selectEmployees.setString(1, departamento);
        ResultSet employees = selectEmployees.executeQuery();


        while (employees.next())
        {
            log.debug("Empleado segundo mejor pagado {} {} {} {}",departamento,employees.getString("Nombre"),
                    employees.getString("Apellido"),employees.getString("Salario"));
        }
    }


    /*Mostrar el número de empleados
    contratados en un mes concreto*/
    private static void  selectContratadosFecha(Connection connection, String fecha1,String fecha2) throws SQLException
    {   PreparedStatement selectEmployees = connection.prepareStatement(
            " select first_name as 'Nombre',last_name as 'Apellido',hire_date as 'Fecha'\n" +
                    " from  employees.employees\n" +
                    " where hire_date BETWEEN ? AND ?;"
                );
        selectEmployees.setString(1, fecha1);
        selectEmployees.setString(2, fecha2);
        ResultSet employees = selectEmployees.executeQuery();

        log.debug("Contratados entre {} y {}",fecha1,fecha2);

        while (employees.next())
        {
            log.debug("{} {} {}",employees.getString("Nombre"),
                    employees.getString("Apellido"),employees.getString("Fecha"));
        }
    }

}
