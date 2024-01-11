package com.unir.app.read;

import com.unir.config.MySqlConnector;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;

@Slf4j
public class MySqlApplication {

    private static final String DATABASE = "employees";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos Oracle");

            selectMaleFemale(connection);
            selectBestSalary(connection, "d005");
            selectSegundonSalary(connection, "d005");
            selectCountNewEmp(connection, "02");

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }
    //1 Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
    private static void selectMaleFemale(Connection connection) throws SQLException{
        Statement selectGender = connection.createStatement();
        ResultSet gender = selectGender.executeQuery("SELECT gender, COUNT(*) as cantidad\n" +
                "FROM employees\n" +
                "GROUP BY gender\n" +
                "ORDER BY cantidad DESC;");
        while (gender.next()){
            log.debug("Genero: {}, Cantidad: {}",
                    gender.getString("gender"), gender.getInt("cantidad"));
        }
    }
    //2 Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parametro variable)
    private static void selectBestSalary(Connection connection, String department) throws SQLException{
        PreparedStatement bestSalary = connection.prepareStatement("SELECT first_name, last_name, salary\n" +
                "FROM employees e\n" +
                "JOIN salaries s ON e.emp_no = s.emp_no\n" +
                "JOIN dept_emp d ON e.emp_no = d.emp_no\n" +
                "WHERE d.dept_no = ?\n" +
                "ORDER BY salary DESC\n" +
                "LIMIT 1;");
        bestSalary.setString(1, department);
        ResultSet employee = bestSalary.executeQuery();
        while(employee.next()){
            log.debug("La persona mejor pagada del departamento {} es {} {} con {} euros",
                    department, employee.getString("first_name"), employee.getString("last_name"), employee.getInt("salary"));
        }
    }

    //3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
    private static void selectSegundonSalary(Connection connection, String department)throws SQLException{
        PreparedStatement segundon = connection.prepareStatement("SELECT first_name, last_name, salary\n" +
                "FROM employees e\n" +
                "         JOIN salaries s ON e.emp_no = s.emp_no\n" +
                "         JOIN dept_emp d ON e.emp_no = d.emp_no\n" +
                "WHERE d.dept_no = ?\n" +
                "ORDER BY salary DESC\n" +
                "LIMIT 1 OFFSET 1;");
        segundon.setString(1, department);
        ResultSet employee = segundon.executeQuery();
        while(employee.next()){
            log.debug("El segundo empleado mejor pagado del departamento: {}, es {} {} con un salario de {} Berries",
                    department, employee.getString("first_name"), employee.getString("last_name"), employee.getInt("salary"));
        }
    }

    //4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
    private static void selectCountNewEmp(Connection connection, String month)throws SQLException{
        PreparedStatement count = connection.prepareStatement("SELECT COUNT(*) as numero_empleados_contratados\n" +
                "FROM employees\n" +
                "WHERE MONTH(hire_date) = ?");
        count.setString(1, month);
        ResultSet cantidad = count.executeQuery();
        while(cantidad.next()){
            log.debug("La cantidad de empleados contratados el mes: {} es {}",
                    month, cantidad.getInt("numero_empleados_contratados"));
        }
    }
}
