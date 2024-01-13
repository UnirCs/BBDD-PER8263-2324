package com.unir.app;

import java.util.List;

import org.hibernate.Session;

import com.unir.config.HibernateMySqlConfig;
import com.unir.config.LogbackConfig;
import com.unir.dao.DepartmentsDao;
import com.unir.dao.EmployeesDao;
import com.unir.dao.SalariesDao;
import com.unir.dao.TitlesDao;
import com.unir.model.mysql.Employee;
import com.unir.model.mysql.Salary;
import com.unir.model.mysql.Title;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySqlApplication {

	public static void main(String[] args) {

		// Configuramos Logback para que muestre las sentencias SQL que se ejecutan
		// unicamente.
		LogbackConfig.configureLogbackForHibernateSQL();

		// Try-with-resources. Se cierra la conexión automáticamente al salir del bloque
		// try
		try (Session session = HibernateMySqlConfig.getSessionFactory().openSession()) {

			log.info("Conexión establecida con la base de datos MySQL");

			// Creamos los DAOs que nos permitirán interactuar con la base de datos
			EmployeesDao employeesDao = new EmployeesDao(session);
			DepartmentsDao departmentsDao = new DepartmentsDao(session);
			SalariesDao salariesDao = new SalariesDao(session);
			TitlesDao titlesDao = new TitlesDao(session);

//			// EJEMPLO:Salaries
//			List<Salary> salaries = salariesDao.findByEmpNo(10001);
//			for (Salary salary : salaries) {
//				log.info("Emp. Número {} : su sueldo entre {} y {} es de {} euros", salary.getEmpNo(), salary.getFromDate(),
//						salary.getToDate(), salary.getSalary());
//			}
//			
//			Salary salary = salariesDao.getActualSalary(10001);
//			log.info("Sueldo actual del empleado nº " + salary.getEmpNo().getEmpNo() + " ("
//					+ salary.getEmpNo().getFirstName() + " " + salary.getEmpNo().getLastName() + ") = "
//					+ salary.getSalary() + "$");
//			
//			//EJEMPLO: Titles
//			List<Title> titles = titlesDao.findByEmpNo(10001);
//			for (Title title : titles) {
//				log.info("Emp. Número {} : su puesto entre {} y {} es {}", title.getEmpNo(), title.getFromDate(),
//						title.getToDate(), title.getTitle());
//			}
			
			
			//EJEMPLO Employee con Salarios y Puestos
			Employee employee = employeesDao.getById(10001);
			log.info("En la actualidad, {} {} cobra {}€ y ocupa el puesto de {}", employee.getFirstName(), employee.getLastName(), 
				((Salary)employee.getSalaries().toArray()[employee.getSalaries().size()-1]).getSalary(), 
				((Title)employee.getTitles().toArray()[employee.getTitles().size()-1]).getTitle());
			
			/***** CONSULTA Nº 1****/
			List<Employee> employeesM= employeesDao.findByGender(Employee.Gender.M);
			List<Employee> employeesF= employeesDao.findByGender(Employee.Gender.F);
			log.info("/******************** CONSULTA 1 **********************/");
			log.info("Nº de Emplead@s:    Mujeres: {}  - Hombres: {}", employeesF.size(), employeesM.size());
			log.info("/******************************************************/");
			
			/***** CONSULTA Nº 2****/
			String deptToSearch = "d001";
			List<Employee> employees= employeesDao.findByDeptSalarySorted(deptToSearch);
			Salary maxSalary = employees.get(0).getSalaries().stream().max((s1, s2) -> s1.getToDate().compareTo(s2.getToDate())).get();
			log.info("/******************** CONSULTA 2 **********************/");
			log.info("Empleado con mayor sueldo del depto {}: {} {} - {}€", deptToSearch, employees.get(0).getFirstName(), employees.get(0).getLastName(), maxSalary.getSalary());
			log.info("/******************************************************/");
			
			/***** CONSULTA Nº 3****/
			log.info("/******************** CONSULTA 3 **********************/");
			maxSalary = employees.get(1).getSalaries().stream().max((s1, s2) -> s1.getToDate().compareTo(s2.getToDate())).get();
			log.info("/******************** CONSULTA 2 **********************/");
			log.info("Empleado con mayor sueldo del depto {}: {} {} - {}€", deptToSearch, employees.get(1).getFirstName(), employees.get(1).getLastName(), maxSalary.getSalary());
			log.info("/******************************************************/");
			
			/***** CONSULTA Nº 4****/
			Integer hireMonth=8;
			employees= employeesDao.findByHireMonth(hireMonth);
			log.info("/******************** CONSULTA 4 **********************/");
			log.info("Nº de Emplead@s contratados en el mes {}:    {}.", hireMonth, employees.size());
			log.info("/******************************************************/");
			
		} catch (Exception e) {
			log.error("Error al tratar con la base de datos", e);
		}
	}
}
