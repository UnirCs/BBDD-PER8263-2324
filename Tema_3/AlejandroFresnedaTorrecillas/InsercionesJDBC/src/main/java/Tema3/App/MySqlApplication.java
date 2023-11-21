package Tema3.App;

import Tema3.Conectores.MySqlConnector;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class MySqlApplication {
    private static final String DATABASE = "employees";
    /**Main con menu*/
    public static void main(String[] args) {
        /*Lectura por teclado*/
        Scanner sc = new Scanner(System.in);
        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos MySQL");
            boolean salir = false;
            int opcion; // Guardaremos la opción del usuario

            while (!salir) {
                log.info("\n=== Menú de opciones ===");
                log.info("1. Insertar nuevo empleado");
                log.info("2. Insertar fichero departamentos");
                log.info("3. Insertar fichero personas");
                log.info("4. Salir");

                try {
                    log.info("Escribe una de las opciones:");
                    opcion = sc.nextInt();

                    switch (opcion) {
                        case 1:

                            /**/
                            log.debug("Indica nacimiento YYYY-MM-DD:");
                            String uNacimiento = sc.next();
                            log.debug("Indicar primer nombre:");
                            String uprimNombre = sc.next();
                            log.debug("Indicar segundo nombre:");
                            String usecNombre = sc.next();
                            log.debug("Indica genero:");
                            String ugenero = sc.next();
                            log.debug("Indica fecha contrato YYYY-MM-DD:");
                            String ufechContrat = sc.next();

                            insertarEmpleado(connection, uNacimiento, uprimNombre, usecNombre, ugenero, ufechContrat);
                            break;
                        case 2:
                            log.warn("Solo números entre 1 y 4");
                            CSVDepartamentos(connection);
                            break;
                        case 3:
                            log.warn("Solo números entre 1 y 4");
                            CSVPersonas(connection);
                            break;
                        case 4:
                            salir = true;
                            break;
                        default:
                            log.warn("Solo números entre 1 y 3");
                    }
                } catch (Exception e) {
                    log.error("Opción inválida, por favor intenta de nuevo", e);
                    sc.next(); // Limpiar buffer del scanner
                }
            }

            log.info("Fin del menú");
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

    /*Lectura de ficheros + inserción*/
    private static void CSVDepartamentos(Connection connection) throws SQLException {
        String pathToCsv = "departamentos.csv";
        String insertSQL = "INSERT INTO departments (dept_no, dept_name) VALUES (?, ?) ON DUPLICATE KEY UPDATE dept_name = VALUES(dept_name)";
        connection.setAutoCommit(false);

        try (BufferedReader br = new BufferedReader(new FileReader(pathToCsv));
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {

            String line;
            boolean isFirstLine = true; // Para omitir la cabecera del CSV

            while ((line = br.readLine()) != null) {
                System.out.println("Line read: " + line);
                // Omitir la primera línea (cabecera)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                //El CSV tiene que separar por comas
                String[] departmentData = line.split(",");

                // Validar que tenemos suficientes partes después del split
                if (departmentData.length < 2) {
                    System.out.println("Línea mal formada o faltan datos: " + line);
                    continue; // Saltar esta línea
                }

                String deptNo = departmentData[0].trim(); //
                String departmentName = departmentData[1].trim();
                /*Borrado del punto y coma fantasma*/
                deptNo = deptNo.replace(";", "");
                departmentName = departmentName.replace(";", "");

                insertStatement.setString(1, deptNo);
                insertStatement.setString(2, departmentName);
                insertStatement.executeUpdate();
            }

            // Hacer commit solo si todas las inserciones han tenido éxito
            connection.commit();

        } catch (IOException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Error al leer el archivo CSV", e);
        }
    }
    private static void CSVPersonas(Connection connection) throws SQLException {
        String pathToCsv = "personas.csv";
        String insertSQL = "INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES (?, ?, ?, ?, ?, ?)";
        connection.setAutoCommit(false);

        try (BufferedReader br = new BufferedReader(new FileReader(pathToCsv))) {
            // Obtener el máximo número de empleado
            String selectMaxEmpNoSQL = "SELECT MAX(emp_no) + 1 AS new_emp_no FROM employees";
            int nextEmpNo = 1; // Valor por defecto si la tabla está vacía
            try (PreparedStatement selectMaxEmpNoStmt = connection.prepareStatement(selectMaxEmpNoSQL)) {
                ResultSet rs = selectMaxEmpNoStmt.executeQuery();
                if (rs.next()) {
                    nextEmpNo = rs.getInt("new_emp_no");
                }
            }

            PreparedStatement insertStatement = connection.prepareStatement(insertSQL);

            String line;
            boolean isFirstLine = true; // Para omitir la cabecera del CSV

            while ((line = br.readLine()) != null) {
                System.out.println("Line read: " + line);
                // Omitir la primera línea (cabecera)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                //El CSV tiene que separar por comas
                String[] personData = line.split(",");

                // Validar que tenemos suficientes partes después del split
                if (personData.length < 5) {
                    System.out.println("Línea mal formada o faltan datos: " + line);
                    continue; // Saltar esta línea
                }

                String birthDate = personData[0].trim();
                String firstName = personData[1].trim();
                String lastName = personData[2].trim();
                String gender = personData[3].trim();
                String hireDate = personData[4].trim();

                insertStatement.setInt(1, nextEmpNo++);
                insertStatement.setString(2, birthDate);
                insertStatement.setString(3, firstName);
                insertStatement.setString(4, lastName);
                insertStatement.setString(5, gender);
                insertStatement.setString(6, hireDate);
                insertStatement.executeUpdate();
            }

            // Hacer commit solo si todas las inserciones han tenido éxito
            connection.commit();
            insertStatement.close();

        } catch (IOException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Error al leer el archivo CSV", e);
        }
    }

    private static void selectAllGender(Connection connection) throws  SQLException {
        Statement selectGender = connection.createStatement();
        ResultSet Gender = selectGender.executeQuery("select GENDER, count(GENDER) as TOTAL from employees.employees group by GENDER");
        while (Gender.next()) {
            log.debug("Del genero {} hay {} personas",
                    Gender.getString("GENDER"),
                    Gender.getString("TOTAL"));
        }
    }

private static void insertarEmpleado(Connection connection, String nacimiento, String primNombre, String secNombre,
                                     String genero, String fechContrat) throws SQLException{

    String selectMaxEmpNoSQL = "SELECT MAX(emp_no) + 1 AS new_emp_no FROM employees";
    String SQLinsertEmpleado = "INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES (?, ?, ?, ?, ?, ?)";
    String SQLselectEmpleado = "SELECT * FROM employees WHERE emp_no = ?";

    Statement selectMaxEmpNoState = connection.createStatement();
    PreparedStatement insertEmpleado = connection.prepareStatement(SQLinsertEmpleado);
    PreparedStatement selectEmpleado = connection.prepareStatement(SQLselectEmpleado);

    long NumEmp = 0;
    ResultSet rsMaxEmpNo = selectMaxEmpNoState.executeQuery(selectMaxEmpNoSQL);
    if (rsMaxEmpNo.next()) {
        NumEmp = rsMaxEmpNo.getLong("new_emp_no");
    }
        insertEmpleado.setLong(1, NumEmp);
        insertEmpleado.setString(2, nacimiento);
        insertEmpleado.setString(3, primNombre);
        insertEmpleado.setString(4, secNombre);
        insertEmpleado.setString(5, genero);
        insertEmpleado.setString(6, fechContrat);

        insertEmpleado.executeUpdate();

        selectEmpleado.setLong(1, NumEmp);
        ResultSet persona = selectEmpleado.executeQuery();

        while (persona.next()) {
            String infoinsertEmpleado = String.format("Se ha creado el emplado %s",
                    persona.getString("emp_no")
            );
            log.debug(infoinsertEmpleado);
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
