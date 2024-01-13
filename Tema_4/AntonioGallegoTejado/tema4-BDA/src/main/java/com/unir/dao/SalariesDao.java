package com.unir.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.unir.model.mysql.Department;
import com.unir.model.mysql.DeptEmployee;
import com.unir.model.mysql.Employee;
import com.unir.model.mysql.Salary;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class SalariesDao {

	private final Session session;

	/**
	 * Consulta de todos los sueldos de la empresa
	 * 
	 * @return Lista de departamentos
	 * @throws SQLException Excepción en caso de error
	 */
	public List<Salary> findAll() throws SQLException {
		List<Salary> salaries = session.createQuery("from Salary", Salary.class).list();

		return salaries;
	}
	
	/**
	 * Recupera la lista de sueldos, ordenados por fecha descendente, del empleado recibido como parámetro
	 * 
	 * @param Número de empleado
	 * @return Lista de salarios
	 * @throws SQLException Excepción en caso de error
	 */
	public List<Salary> findByEmpNo(Integer empNo) throws SQLException {
		Query<Salary> query = session.createNativeQuery(
				"SELECT * from salaries where emp_no=:empNo order by from_date desc", Salary.class);
		query.setParameter("empNo", empNo);

		return query.list();
	}
	
	public Salary getActualSalary(Integer empNo) throws SQLException {
		Query<Salary> query = session.createNativeQuery(
				"SELECT * from salaries where emp_no=:empNo order by from_date desc limit 1", Salary.class);
		query.setParameter("empNo", empNo);
		
		return query.getSingleResult();
	}

}
