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
            log.info("Consulta todos empleados--------------------------------------------------------");
            //Ejemplo de uso de DAO 1: Obtenemos todos los empleados de la base de datos
            List<Employee> employees = employeesDao.findByDepartment("d001");
            log.info("Empleados del departamento d001: {}", employees.size());

            log.info("Resolucion pregunta 1--------------------------------------------------------");
            //Resolucion pregunta 1
            List<Object[]> genderCounts = employeesDao.countEmployeesByGender();

            for (Object[] result : genderCounts) {
                char gender = (char) result[0];
                int count = ((Number) result[1]).intValue();
                log.info("\nGender: " + gender + ", Count: " + count);
            }
            log.info("Resolucion pregunta 2--------------------------------------------------------");
            //Resolucion pregunta 2
            String departmentId = "d005";

            List<Employee> empleadosMejorPagados = employeesDao.mejorPagado("d001");

            log.info("Empleados del departamento d001: {}", empleadosMejorPagados.size());

            log.info("Resolucion pregunta 3--------------------------------------------------------");
            //Resolucion pregunta 3
            String departmentId2 = "d004";

            Object[] result2 = employeesDao.segundoMejorPagado(departmentId2);

            String firstName2 = (String) result2[0];
            String lastName2 = (String) result2[1];
            int salary2 = (int) result2[2];
            String deptNo2 = (String) result2[3];
            log.info("\nSegundo Mejor Pagado " + deptNo2 + "\nNombre: " + firstName2 + "\nApellido: " + lastName2+ "\nsalario: " + salary2);



            log.info("Resolucion pregunta 4--------------------------------------------------------");
            //Resolucion pregunta 4
            int mes = 12; // Cambia esto al mes que desees

            Long empleadosContratadosMes = employeesDao.empleadosContratadosMes(mes);

            log.info("\nNúmero de empleados contratados en el mes " + mes + ": " + empleadosContratadosMes);


            log.info("Ejemplo de uso de DAO 2--------------------------------------------------------");
            //Ejemplo de uso de DAO 2: Insertamos un nuevo empleado. Save por defecto no actualiza, solo inserta.
            Department bbddDepartment = new Department();
            bbddDepartment.setDeptName("Database Department");
            bbddDepartment.setDeptNo("d010");
            //departmentsDao.save(bbddDepartment);
            //log.info("Departamento insertado: {}", bbddDepartment);

            log.info("Ejemplo de uso de DAO 3--------------------------------------------------------");
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
