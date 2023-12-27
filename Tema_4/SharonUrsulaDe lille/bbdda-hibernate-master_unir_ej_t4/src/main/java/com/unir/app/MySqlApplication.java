package com.unir.app;

import com.unir.config.HibernateMySqlConfig;
import com.unir.config.LogbackConfig;
import com.unir.dao.DepartmentsDao;
import com.unir.dao.EmployeesDao;
import com.unir.model.mysql.Department;
import com.unir.model.mysql.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;

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

            //CONSULTA 1: Obtener el número de hombres y mujeres de la base de datos.
            List<Object> resultadoC1 = employeesDao.countGender();
            log.info("****************************************************************");
            log.info("********Número de hombres y mujeres de la base de datos:********");
            for (Object filaC1 : resultadoC1){
                String sexo = String.valueOf(((Object[]) filaC1)[0]);
                Number count = (Number)((Object[]) filaC1)[1];
                log.info("Sexo: " + sexo + ", total: " + count);
            }
            log.info("****************************************************************");

            //CONSULTA 2: Persona mejor pagado.
            List<Object> resultadoC2 = employeesDao.mejorPagado("d005");
            log.info("****************************************************************");
            log.info("********Mostrar persona mejor pagada de un departamento:********");
            for (Object filaC2 : resultadoC2){
                String nombreC2 = (String)((Object[]) filaC2)[0];
                String apellidoC2 = (String)((Object[]) filaC2)[1];
                Number salarioC2 = (Number) ((Object[]) filaC2)[2];
                log.info("Nombre: " + nombreC2 + ", apellido: " + apellidoC2 + ", salario: " + salarioC2);
            }
            log.info("****************************************************************");

            //CONSULTA 3: Segundo persona mejor pagado.
            List<Object> resultadoC3 = employeesDao.segundoMejorPagado("d005");
            log.info("****************************************************************");
            log.info("****Mostrar persona segundo mejor pagada de un departamento:****");
            for (Object filaC3 : resultadoC3){
                String nombreC3 = (String)((Object[]) filaC3)[0];
                String apellidoC3 = (String)((Object[]) filaC3)[1];
                Number salarioC3 = (Number) ((Object[]) filaC3)[2];
                log.info("Nombre: " + nombreC3 + ", apellido: " + apellidoC3 + ", salario: " + salarioC3);
            }
            log.info("****************************************************************");

            //CONSULTA 4: Numero de empleados contratados en un mes concreto.
            String resultadoC4 = employeesDao.numeroEmpleadosMes(11);
            log.info("****************************************************************");
            log.info("************Mostrar numero de empleados contratados:************");
            log.info("Número de personas contratadas: " + resultadoC4);
            log.info("****************************************************************");



        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }
}
