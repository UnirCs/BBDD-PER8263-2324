package com.unir.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.unir.model.mysql.Employee;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class EmployeesDao {

	private final Session session;

	/**
	 * Consulta de todos los empleados de la base de datos Se ofrecen dos versiones
	 * 1. Con SQL nativo 2. Con HQL:
	 * https://docs.jboss.org/hibernate/orm/3.5/reference/es-ES/html/queryhql.html
	 * 
	 * @throws SQLException Excepción en caso de error
	 */
	public List<Employee> findAll() throws SQLException {
		List<Employee> employees = session.createNativeQuery("SELECT * FROM employees", Employee.class).list();
		session.createQuery("FROM Employee", Employee.class).list();
		log.debug("Número de empleados: {}", employees.size());

		return employees;
	}

	/**
	 * Consulta de todos los empleados de un departamento
	 * 
	 * @param departmentId Identificador del departamento
	 * @return Lista de empleados
	 * @throws SQLException Excepción en caso de error
	 */
	public List<Employee> findByDepartment(String departmentId) throws SQLException {
		Query<Employee> query = session.createNativeQuery("SELECT e.*\n" + "FROM employees.employees e\n"
						+ "JOIN employees.dept_emp de ON e.emp_no = de.emp_no\n"
						+ "JOIN employees.departments d ON de.dept_no = d.dept_no\n"
						+ "WHERE d.dept_no = :deptNo",
				Employee.class);
		query.setParameter("deptNo", departmentId);

		return query.list();
	}

	/**
	 * Obtención de un empleado por su identificador.
	 * 
	 * @param id - Identificador del empleado.
	 * @return Empleado.
	 * @throws SQLException - Excepción en caso de error.
	 */
	public Employee getById(Integer id) throws SQLException {
		return session.get(Employee.class, id);
	}

//	/**
//	 * Carga los títulos y salarios del empleado y los valores "actuales"
//	 * 
//	 * @param employee Empleado
//	 * @return Empleado con datos de salarios y puestos
//	 */
//	private Employee loadActualSalaryAndTitle(Employee employee) {
//		// Cargar datos de sueldos y puestos en la empresa
//		Set<Salary> salaries = employee.getSalaries();
//		Salary actualSalary = salaries.stream().max((s1, s2) -> s1.getToDate().compareTo(s2.getToDate())).get();
//		employee.setActualSalary(actualSalary.getSalary());
//
//		Set<Title> titles = employee.getTitles();
//		Title actualTitle = titles.stream().max((s1, s2) -> s1.getToDate().compareTo(s2.getToDate())).get();
//		employee.setActualTitle(actualTitle.getTitle());
//
//		return employee;
//	}

	/**
	 * Elimina un empleado de la base de datos.
	 * 
	 * @param employee - Empleado a eliminar.
	 * @return true si se ha eliminado correctamente.
	 * @throws SQLException - Excepción en caso de error.
	 */
	public Boolean remove(Employee employee) throws SQLException {
		session.remove(employee);
		return true;
	}

	/**
	 * Recupera a lista de empleados según el género recibido
	 * 
	 * @param Género a recuperar (F/M)
	 * @return Lista de empleados
	 * @throws SQLException Excepción en caso de error
	 */
	public List<Employee> findByGender(Employee.Gender gender) throws SQLException {
		Query<Employee> query = session.createNativeQuery("SELECT * FROM employees " + "WHERE gender=:gender",
				Employee.class);
		query.setParameter("gender", gender.toString());
		return query.list();
	}

	/**
	 * Recupera la lista de empleados de un departamento dado ordenados por sueldo (mayor a menor)
	 * @param departamentId
	 * @return Lista de empleados 
	 * @throws SQLException
	 */
	public List<Employee> findByDeptSalarySorted(String departamentId) throws SQLException {
		Query<Employee> query = session.createNativeQuery("select employees.* from employees "
				+ "inner join salaries on employees.emp_no = salaries.emp_no "
				+ "inner join dept_emp on employees.emp_no = dept_emp.emp_no "
				+ "inner join departments on dept_emp.dept_no = departments.dept_no "
				+ "where dept_emp.dept_no=:deptNo " + "order by salaries.salary desc", Employee.class);

		query.setParameter("deptNo", departamentId);
		return query.list();
	}
	
	/**
	 * Recupra la lista de empleados cuya fecha de contratación sea la del mes pasado como parámetro
	 * @param hireMonth
	 * @return Lista de empleados
	 * @throws SQLException
	 */
	public List<Employee> findByHireMonth(Integer hireMonth) throws SQLException {
		Query<Employee> query = session.createNativeQuery("select * from employees where month(hire_date)=:hireMonth", Employee.class);

		query.setParameter("hireMonth", hireMonth);
		return query.list();
	}
}
