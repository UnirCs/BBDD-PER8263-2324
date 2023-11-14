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

            log.debug("1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.");
            selectMenAndWomenDescend(connection);

            log.debug("2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).");
            highestpaidperson(connection, "d005");

            log.debug("3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).");
            secondhighestpaidperson(connection, "d005");

            log.debug("4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).");
            hireemployees(connection, 3);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    private static void selectMenAndWomenDescend(Connection connection) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("SELECT gender as Genero, count(*) as Total\n" +
                "FROM employees\n" +
                "GROUP BY gender\n" +
                "ORDER BY Total desc");

        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleados {}: {}",
                    employees.getString("Genero"),
                    employees.getString("Total"));
        }
    }

    private static void highestpaidperson(Connection connection, String departamento) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select first_name as 'Nombre', last_name as 'Apellido', salary as 'Salario'\n" +
                "from employees, salaries, departments, dept_emp\n" +
                "where employees.emp_no = salaries.emp_no and\n" +
                "      employees.emp_no = dept_emp.emp_no and\n" +
                "      departments.dept_no = dept_emp.dept_no and\n" +
                "      dept_emp.dept_no = ?\n" +
                "order by salary DESC\n" +
                "limit 1");

        selectEmployees.setString(1, departamento);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("La primera persona mejor pagada del departamento {} es: {} {} con un salario de {}",
                    departamento,
                    employees.getString("Nombre"),
                    employees.getString("Apellido"),
                    employees.getString("Salario"));
        }
    }

    private static void secondhighestpaidperson(Connection connection, String departamento) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select first_name as 'Nombre', last_name as 'Apellido', salary as 'Salario'\n" +
                "from employees, salaries, departments, dept_emp\n" +
                "where employees.emp_no = salaries.emp_no and\n" +
                "      employees.emp_no = dept_emp.emp_no and\n" +
                "      departments.dept_no = dept_emp.dept_no and\n" +
                "      dept_emp.dept_no = ?\n" +
                "order by salary DESC\n" +
                "limit 1,1");

        selectEmployees.setString(1, departamento);
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("La segunda persona mejor pagada del departamento {} es: {} {} con un salario de {}",
                    departamento,
                    employees.getString("Nombre"),
                    employees.getString("Apellido"),
                    employees.getString("Salario"));
        }
    }

    private static void hireemployees(Connection connection, Integer mes) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select count(*) as 'Total'\n" +
                "from employees\n" +
                "where month(hire_date) = ?\n");

        selectEmployees.setString(1, String.valueOf(mes));
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleados contratados el mes {}: {}",
                    mes,
                    employees.getString("Total"));
        }
    }
}
