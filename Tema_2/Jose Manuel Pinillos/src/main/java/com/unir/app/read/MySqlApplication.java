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

            selectMenAndWomenDescend(connection);
            highestpaidperson(connection, "d005");
            secondhighestpaidperson(connection, "d005");
            hiredemployees(connection, 3);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    private static void selectMenAndWomenDescend(Connection connection) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select gender as 'Genero', count(*)\n as 'Total'" +
                "from employees\n" +
                "group by gender");

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

    private static void hiredemployees(Connection connection, Integer mes) throws SQLException {
        PreparedStatement selectEmployees = connection.prepareStatement("select count(*) as 'Total'\n" +
                "from employees\n" +
                "where month(hire_date) = ?;\n");

        selectEmployees.setString(1, String.valueOf(mes));
        ResultSet employees = selectEmployees.executeQuery();

        while (employees.next()) {
            log.debug("Empleados contratados el mes {}: {}",
                    mes,
                    employees.getString("Total"));
        }
    }
}
