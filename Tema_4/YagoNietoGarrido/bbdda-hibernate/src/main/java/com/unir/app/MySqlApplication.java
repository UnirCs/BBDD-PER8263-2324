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

import java.time.Month;
import java.util.*;

@Slf4j
public class MySqlApplication {
    public static void main(String[] args) {


        //Configuramos Logback para que muestre las sentencias SQL que se ejecutan unicamente.
        LogbackConfig.configureLogbackForHibernateSQL();

        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Session session = HibernateMySqlConfig.getSessionFactory().openSession()) {

            log.info("Conexión establecida con la base de datos MySQL ");

            //Creamos los DAOs que nos permitirán interactuar con la base de datos
            EmployeesDao employeesDao = new EmployeesDao(session);
            DepartmentsDao departmentsDao = new DepartmentsDao(session);

            List<Object[]> employeesGender = employeesDao.selectGroupEmployeesGender();
            for (Object[] employeeG : employeesGender) {
                log.debug("Empleados del género {}: {}", employeeG[0], employeeG[1]);
            }

            String department1 = "d001";
            Employee employee = employeesDao.selectMaxSalaryPersonDeparment(department1);
            Salary maxSalary = employee.getSalary().stream().reduce((s1, s2) -> s1.getSalary() > s2.getSalary() ? s1 : s2).get();
            log.debug("Persona que más cobra del departamento "+department1+": {} {}, {}€", employee.getFirstName(), employee.getLastName(), maxSalary.getSalary());

            String department2 = "d001";
            Employee employee2 = employeesDao.select2ndMaxSalaryPersonDeparment(department2);
            Salary maxSalary2 = employee.getSalary().stream().reduce((s1, s2) -> s1.getSalary() > s2.getSalary() ? s1 : s2).get();
            log.debug("Persona que más cobra del departamento "+department2+": {} {}, {}€", employee2.getFirstName(), employee2.getLastName(), maxSalary2.getSalary());

            // month valores del 1 al 12
            String month = "1";
            Integer numEmpleados = employeesDao.selectEmployeesHiredAtMonth(month);
            log.debug("Empleados contratados en el mes "+month+": {}",numEmpleados);



            /*
            //Ejemplo de uso de DAO 1: Obtenemos todos los empleados de la base de datos
            List<Employee> employees = employeesDao.findByDepartment("d001");
            log.info("Empleados del departamento d001: {}", employees.size());

            //Ejemplo de uso de DAO 2: Insertamos un nuevo empleado. Save por defecto no actualiza, solo inserta.
            Department bbddDepartment = new Department();
            bbddDepartment.setDeptName("Database Department");
            bbddDepartment.setDeptNo("d010");
            departmentsDao.save(bbddDepartment);
            log.info("Departamento insertado: {}", bbddDepartment);

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
