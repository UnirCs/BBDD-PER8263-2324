package com.unir.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class EmployeesDao {

    private final Session session;
    /**
     * CONSULTA 1: Obtener el número de hombres y mujeres de la base de datos.
     * @return Lista con ambos sexos
     * @throws SQLException Excepción en caso de error
     */
    public List<Object> countGender() throws SQLException {
        List<Object> query = session.createNativeQuery("select gender, count(*) as 'Count' from employees group by gender order by Count DESC",Object.class).list();
        return query;
    }

    /**
     * CONSULTA 2: Persona mejor pagado.
     * @param departmentId Identificador del departamento
     * @return Persona mejor pagada
     * @throws SQLException Excepción en caso de error
     */
    public List<Object> mejorPagado(String departmentId) throws SQLException {
        Query<Object> query = session.createNativeQuery("SELECT e.first_name, e.last_name, s.salary FROM salaries s JOIN employees e on e.emp_no = s.emp_no JOIN dept_emp de on e.emp_no = de.emp_no where de.dept_no =  :deptNo order by s.salary DESC LIMIT 1", Object.class);
        query.setParameter("deptNo", departmentId);
        return query.list();
    }

    /**
     * CONSULTA 3: Segunda persona mejor pagado.
     * @param departmentId Identificador del departamento
     * @return Persona mejor pagada
     * @throws SQLException Excepción en caso de error
     */
    public List<Object> segundoMejorPagado(String departmentId) throws SQLException {
        Query<Object> query = session.createNativeQuery("SELECT e.first_name, e.last_name, s.salary FROM salaries s JOIN employees e on e.emp_no = s.emp_no JOIN dept_emp de on e.emp_no = de.emp_no where de.dept_no =  :deptNo order by s.salary DESC LIMIT 1 OFFSET 1", Object.class);
        query.setParameter("deptNo", departmentId);
        return query.list();
    }

    /**
     * CONSULTA 4: Numero de empleados contratados en un mes concreto.
     *
     * @param mes Mes de contratación
     * @return Numero de empleados contratados
     * @throws SQLException Excepción en caso de error
     */
    public String numeroEmpleadosMes(int mes) throws SQLException {
        Query <String> query = session.createNativeQuery("SELECT count(*) AS total_contract FROM employees e where month(e.hire_date)  =  :hireDate", String.class);
        query.setParameter("hireDate", mes);
        return query.getSingleResult();
    }

}
