package com.unir.app;

import com.unir.config.HibernateMySqlConfig;
import com.unir.config.LogbackConfig;
import com.unir.dao.DepartmentsDao;
import com.unir.dao.EmployeesDao;
import com.unir.model.mysql.Department;
import com.unir.model.mysql.Employee;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class MySqlApplication {

    public static void main(String[] args) {
        //Código de poyo original dado por el profesor de la asignatura
        //CodigoApoyoOriginal();

        //Configuramos Logback para que muestre las sentencias SQL que se ejecutan unicamente.
        LogbackConfig.configureLogbackForHibernateSQL();

        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Session session = HibernateMySqlConfig.getSessionFactory().openSession()) {

            log.info("Conexión establecida con la base de datos MySQL");

            //Creamos los DAOs que nos permitirán interactuar con la base de datos
            EmployeesDao employeesDao = new EmployeesDao(session);
            DepartmentsDao departmentsDao = new DepartmentsDao(session);


            consulta1(employeesDao,"Marketing");
            consulta2(employeesDao);
            consulta3(employeesDao,"Marketing");
            consulta4(employeesDao, 7);

        }  catch (Exception e) {
        log.error("Error al tratar con la base de datos", e);
        }

    }

    /**
     * -- Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable)
     * @param employeesDao
     */
    public static void consulta1(EmployeesDao employeesDao, String deptName) throws SQLException {

        List<Object> response= employeesDao.findBestSalayOfDepartment(deptName);

        log.info("####################################################################");
        log.info("Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable)");

        for (int i=0 ; i < response.size(); i++){
            log.info("Nombre : " + (((Object[])response.get(i))[0]).toString() + " Apellido: " + (((Object[])response.get(i))[1]).toString() + " Salario : " + (((Object[])response.get(i))[2]).toString());
        }
        log.info("####################################################################");

    }

    /**
     * -- Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente
     * @param employeesDao
     */
    public static void consulta2(EmployeesDao employeesDao) throws SQLException {

        List<Object> respone = employeesDao.getNumberOfMenAndWomen();

        log.info("####################################################################");
        log.info("Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente");

        for (int i=0 ; i < respone.size(); i++){
            log.info("SEXO : " + (((Object[])respone.get(i))[0]).toString() + " CANTIDAD: " + (((Object[])respone.get(i))[1]).toString());
        }

        log.info("####################################################################\n\n");

    }

    /**
     * -- Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
     * @param employeesDao
     */
    public static void consulta3(EmployeesDao employeesDao, String deptName) throws SQLException {


        List<Object> response = employeesDao.findSecondSalayOfDepartment(deptName);

        log.info("####################################################################");
        log.info("Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).");

        for (int i=0 ; i < response.size(); i++){
            log.info("Nombre : " + (((Object[])response.get(i))[0]).toString() + " Apellido: " + (((Object[])response.get(i))[1]).toString() + " Salario : " + (((Object[])response.get(i))[2]).toString());
        }
        log.info("####################################################################");

    }

    /**
     * -- Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
     * @param employeesDao
     */
    public static void consulta4(EmployeesDao employeesDao, int month) throws SQLException {

        List<String> response = employeesDao.findHiresInMonth(month);

        log.info("####################################################################");
        log.info("Mostrar el número de empleados contratados en un mes concreto (parámetro variable).");
        log.info("Nº de contrataciones en el mes "+month + ": "+ response.get(0));
        log.info("####################################################################");

    }

    public static void CodigoApoyoOriginal(){
        //Configuramos Logback para que muestre las sentencias SQL que se ejecutan unicamente.
        LogbackConfig.configureLogbackForHibernateSQL();

        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Session session = HibernateMySqlConfig.getSessionFactory().openSession()) {

            log.info("Conexión establecida con la base de datos MySQL");

            //Creamos los DAOs que nos permitirán interactuar con la base de datos
            EmployeesDao employeesDao = new EmployeesDao(session);
            DepartmentsDao departmentsDao = new DepartmentsDao(session);

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

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }
}
