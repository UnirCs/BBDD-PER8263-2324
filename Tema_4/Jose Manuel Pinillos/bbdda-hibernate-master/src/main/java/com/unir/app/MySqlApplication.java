package com.unir.app;

import com.unir.config.HibernateMySqlConfig;
import com.unir.config.LogbackConfig;
import com.unir.dao.DepartmentsDao;
import com.unir.dao.EmployeesDao;
import com.unir.model.mysql.Department;
import com.unir.model.mysql.Employee;
import com.unir.model.mysql.Salary;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

            // 1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
            List <Object[]> employeesByGender = employeesDao.groupEmployeesByGender();

            log.info("CONSULTA 1");
            for (Object[] gender :employeesByGender) {
                log.info("Género: {}, Total: {}", gender[0], gender[1]);
            }

            // 2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
            Employee personBestSalary = employeesDao.findEmployeeBestSalary("d005");

            Integer max = 0;

            for (Salary salary:personBestSalary.getSalaries()) {
                if (salary.getSalary() > max ) {
                    max = salary.getSalary();
                }
            }
            log.info("CONSULTA 2");
            log.info("Nombre: {} {} ---> Salario: {}", personBestSalary.getFirstName(), personBestSalary.getLastName(), max);

            // 3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
            Employee secondPersonBestSalary = employeesDao.findSecondEmployeeBestSalary("d005");

            max = 0;

            for (Salary salary:secondPersonBestSalary.getSalaries()) {
                if (salary.getSalary() > max ) {
                    max = salary.getSalary();
                }
            }

            log.info("CONSULTA 3");
            log.info("Nombre: {} {} ---> Salario: {}", secondPersonBestSalary.getFirstName(), secondPersonBestSalary.getLastName(), max);

            // 4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
            Long numberOfEmployees = employeesDao.findNumberOfEmployees(3);

            log.info("CONSULTA 4");
            log.info("Numero de empleados contratadoses: {}", numberOfEmployees);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }
}
