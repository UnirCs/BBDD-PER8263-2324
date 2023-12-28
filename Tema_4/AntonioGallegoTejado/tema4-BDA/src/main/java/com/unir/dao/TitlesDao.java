package com.unir.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.unir.model.mysql.Salary;
import com.unir.model.mysql.Title;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class TitlesDao {

	private final Session session;

	/**
	 * Consulta de todos los puestos de todos los empleados la empresa
	 * 
	 * @return Lista de puestos
	 * @throws SQLException Excepción en caso de error
	 */
	public List<Title> findAll() throws SQLException {
		List<Title> titles = session.createQuery("from titles", Title.class).list();

		return titles;
	}
	
	/**
	 * Recupera la lista de puestos, ordenados por fecha descendente, del empleado recibido como parámetro
	 * 
	 * @param Número de empleado
	 * @return Lista de puestos
	 * @throws SQLException Excepción en caso de error
	 */
	public List<Title> findByEmpNo(Integer empNo) throws SQLException {
		Query<Title> query = session.createNativeQuery(
				"SELECT * from titles where emp_no=:empNo order by from_date desc", Title.class);
		query.setParameter("empNo", empNo);

		return query.list();
	}
	
	public Title getActualTitle(Integer empNo) throws SQLException {
		Query<Title> query = session.createNativeQuery(
				"SELECT * from titls where emp_no=:empNo order by from_date desc limit 1", Title.class);
		query.setParameter("empNo", empNo);
		
		return query.getSingleResult();
	}

}
