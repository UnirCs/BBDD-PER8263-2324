package com.unir.app;

import com.unir.config.HibernateMySqlConfig;
import com.unir.config.LogbackConfig;
import com.unir.dao.DepartmentsDao;
import com.unir.dao.EmployeesDao;
import com.unir.model.mysql.Department;
import com.unir.model.mysql.Employee;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import java.util.List;
import java.util.Map;

@Slf4j
public class MySqlApplication {

    public static void main(String[] args) {

        //Configuramos Logback para que muestre las sentencias SQL que se ejecutan unicamente.
        LogbackConfig.configureLogbackForHibernateSQL();

        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Session session = HibernateMySqlConfig.getSessionFactory().openSession()) {

            log.info("Conexión establecida con la base de datos MySQL");

            //Creamos los DAOs que nos permitirán interactuar con la base de datos
            EmployeesDao employeesDao = new EmployeesDao(session);
            DepartmentsDao departmentsDao = new DepartmentsDao(session);

            log.info("------------------------------- Consulta 01 ---------------------------------");

            // Suma de personas por genero
            List<Object[]> resultGeneres = employeesDao.numberOfEmployeesGroupByGenere();
            // Número de empleados por género
            for (Object[] result : resultGeneres
            ) {
                log.info("Género: {}, Total: {}", result[0], result[1]);
            }

            log.info("------------------------------- Consulta 02 ---------------------------------");
            // Persona que más cobre en el departamento d001
            Employee resultMaxSalary = employeesDao.getTheBestSalaryEmployees("d001");

            log.info("Persona que más cobre en el departamento d001: {}. Con un salario de:", resultMaxSalary.getFirstName());
            resultMaxSalary.getSalaries().forEach(salary -> {
                log.info("          - Salario: {}", salary.getSalary());
            });

            log.info("------------------------------- Consulta 03 ---------------------------------");
            // Persona que más cobre en el departamento d001
            Employee resultSecondMaxSalary = employeesDao.getTheSecondBestSalaryEmployees("d001");

            log.info("Persona que más cobre en el departamento d001: {}. Con un salario de:", resultSecondMaxSalary.getFirstName());
            resultSecondMaxSalary.getSalaries().forEach(salary -> {
                log.info("          - Salario: {}", salary.getSalary());
            });

            log.info("------------------------------- Consulta 04 ---------------------------------");
            // Número de personas contratadas en un mes en concreto
            Long hiredPersonsNumber = employeesDao.getTotalOfEmployeesHired(5);

            log.info("Número de personas contratadas en el mes 5: {}", hiredPersonsNumber);





            /*
            //Ejemplo de uso de DAO 1: Obtenemos todos los empleados de la base de datos
            List<Employee> employees = employeesDao.findByDepartment("d001");
            log.info("Empleados del departamento d001: {}", employees.size());

            //Ejemplo de uso de DAO 2: Insertamos un nuevo empleado. Save por defecto no actualiza, solo inserta.
            Department bbddDepartment = new Department();
            bbddDepartment.setDeptName("Database Department");
            bbddDepartment.setDeptNo("d010");
            //departmentsDao.save(bbddDepartment);
            //log.info("Departamento insertado: {}", bbddDepartment);

            //Ejemplo de uso de DAO 3: La actualizacion ocurre cuando modificamos un objeto que ya existe en la base de datos (Entity Manager controla su ciclo de vida)
            //Lo recuperamos de la base de datos.
            //Lo modificamos.
            session.beginTransaction();
            log.info("Obteniendo jesus");
            Employee jesus = employeesDao.getById(1001);
            jesus.setFirstName(("J" + System.currentTimeMillis()));
            log.info("jesus modificado");
            //Al hacer commit de la transaccion se actualiza el objeto en la base de datos sin hacer un update explicito (EM controla el ciclo de vida del objeto)
            session.getTransaction().commit();

            //Ejemplo de uso de DAO 4: Eliminamos un empleado
            session.beginTransaction();
            //Eliminamos un empleado
            employeesDao.remove(jesus);
            jesus = employeesDao.getById(1001);
            //Hacemos rollback para que no se aplique la eliminación
            session.getTransaction().rollback();
            */


        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }
}
