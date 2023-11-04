package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Scanner;

@Slf4j
public class MySqlApplication {

    private static final String DATABASE = "employees";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos Oracle");

            /*selectAllEmployeesOfDepartment(connection, "d001");
            selectAllEmployeesOfDepartment(connection, "d002");*/
            selectAllGender(connection);
            /*Solicitamos código del departamento*/
            Scanner sc= new Scanner(System.in);
            log.debug("Consulta mejor pagado: Insertar código de departamento");
            String depart = sc.next();
            selectMejorPagado(connection,depart);
            log.debug("Consulta 2º mejor pagado: Insertar código de departamento");
            selectSegundoMejorPagado(connection,depart);
            log.debug("Introduce el mes para consultar contrataciones");
            String mes = sc.next();
            selectContratados(connection, mes);

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

    private static void selectAllGender(Connection connection) throws  SQLException {
        Statement selectGender = connection.createStatement();
        ResultSet Gender = selectGender.executeQuery("select GENDER, count(GENDER) as TOTAL from employees.employees group by GENDER");
        while (Gender.next()) {
            log.debug("Del genero {} hay {} personas",
                    Gender.getString("GENDER"),
                    Gender.getString("TOTAL"));
        }
    }

private static void selectMejorPagado(Connection connection, String departamento) throws  SQLException {

    PreparedStatement selectMejor = connection.prepareStatement("select e.first_name, e.last_name, s.salary, cd.dept_no from employees e\n" +
            "                INNER JOIN salaries s ON e.emp_no=s.emp_no\n" +
            "                INNER JOIN dept_emp d ON d.emp_no=e.emp_no\n" +
            "                INNER JOIN current_dept_emp cd ON d.emp_no=cd.emp_no\n" +
            "    where s.salary=(select max(salary) from salaries\n" +
            "                    INNER JOIN dept_emp ON salaries.emp_no=dept_emp.emp_no where dept_emp.dept_no=?)\n" +
            "      and d.dept_no=?");

    selectMejor.setString(1, departamento);
    selectMejor.setString(2, departamento);
    ResultSet mejor = selectMejor.executeQuery();

    while (mejor.next()) {
        log.debug("La 2º persona mejor pagada es '{} {}' con {}$ en el departamento {}.",
                mejor.getString("first_name"),
                mejor.getString("last_name"),
                mejor.getString("salary"),
                mejor.getString("dept_no"));
    }
}
    private static void selectAllEmployees(Connection connection) throws SQLException {
        Statement selectEmployees = connection.createStatement();
        ResultSet employees = selectEmployees.executeQuery("select * from employees");

        while (employees.next()) {
            log.debug("Employee: {} {}",
                    employees.getString("first_name"),
                    employees.getString("last_name"));
        }
    }

    private static void selectSegundoMejorPagado(Connection connection, String departamento) throws  SQLException {

        PreparedStatement selectSegundoMejor = connection.prepareStatement("select e.first_name, e.last_name, s.salary, cd.dept_no from employees e\n" +
                "                INNER JOIN salaries s ON e.emp_no = s.emp_no\n" +
                "                INNER JOIN dept_emp d ON d.emp_no = e.emp_no\n" +
                "                INNER JOIN current_dept_emp cd ON d.emp_no=cd.emp_no\n" +
                "    where s.salary = (select MAX(s.salary) from salaries s\n" +
                "        INNER JOIN dept_emp de ON s.emp_no = de.emp_no where de.dept_no = ? AND s.salary < (\n" +
                "        select MAX(salary) from salaries INNER JOIN dept_emp ON salaries.emp_no = dept_emp.emp_no\n" +
                "        where dept_emp.dept_no = ?)\n" +
                ") AND d.dept_no = ?");

        selectSegundoMejor.setString(1, departamento);
        selectSegundoMejor.setString(2, departamento);
        selectSegundoMejor.setString(3, departamento);
        ResultSet segundomejor = selectSegundoMejor.executeQuery();

        while (segundomejor.next()) {
            log.debug("La persona mejor pagada es '{} {}' con {}$ en el departamento {}.",
                    segundomejor.getString("first_name"),
                    segundomejor.getString("last_name"),
                    segundomejor.getString("salary"),
                    segundomejor.getString("dept_no"));
        }
    }
    private static void selectContratados(Connection connection, String fecha) throws  SQLException {
        if (fecha.length() == 1) {fecha = "0" + fecha;}
        String ajuste = "%-"+fecha+"-%";
        PreparedStatement selectContratos = connection.prepareStatement("select count(hire_date) as cuenta, hire_date from employees\n" +
                "    where hire_date like ? group by hire_date order by  cuenta desc limit 1");

       selectContratos.setString(1, ajuste);

        ResultSet contratados = selectContratos.executeQuery();

        while (contratados.next()) {

            log.debug("El mes  que mas se contrataron fue {} con {} personas.",

                    contratados.getString("hire_date"),
                    contratados.getString("cuenta"));
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
}
