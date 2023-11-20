package com.unir.employees.controller;

import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.sql.Date;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeRepository employeeRepository;

	/**
	 * Obtener empleados por apellido.
	 * Si no se especifica apellido, se devuelven los primeros 20 empleados.
	 *
	 * @param lastName - apellido.
	 * @return lista de empleados.
	 */
	@GetMapping
	public List<Employee> getEmployees(@RequestParam(value = "lastName", required = false) String lastName) {

		if(StringUtils.hasText(lastName)) {
			return employeeRepository.findByLastName(lastName);
		} else {
			return employeeRepository.findAll().subList(0, 20);
		}
	}


	/*
     * Buscar empleados por rango de fechas de nacimiento
	 * @param startDate - fecha de inicio.
	 * @param endDate - fecha de fin.
	 * @return lista de empleados.
	 */
	@GetMapping("/birthDate")
	public List<Employee> getEmployeesByBirthDate(@RequestParam(value = "startDate", required = false) String startDate,
												  @RequestParam(value = "endDate", required = false) String endDate) {
		return employeeRepository.findByBirthDateBetween(Date.valueOf(startDate), Date.valueOf(endDate));
	}

	/*
	 * Buscar empleados por nombre
	 * @param name - nombre.
	 * @return lista de empleados.
	 */
	@GetMapping("/byFirstName")
	public List<Employee> findByFirstNameContaining(@RequestParam String name) {
		return employeeRepository.findByFirstNameContaining(name);
	}

	/*
	 * Buscar empleados por género
	 * @param gender - género.
	 * @return lista de empleados.
	 */
	@GetMapping("/byGender")
	public List<Employee> findByGender(@RequestParam Gender gender) {
		return employeeRepository.findByGender(gender);
	}

	/*
	 * Buscar empleados contratados después de una fecha específica
	 * @param date - fecha contratación inicial.
	 * @return lista de empleados.
	 */
	@GetMapping("/hiredAfter")
	public List<Employee> findByHireDateAfter(@RequestParam(value = "date", required = false) String date) {
		return employeeRepository.findByHireDateAfter(Date.valueOf(date));
	}

	//
	/*
	 * Buscar empleados por apellido y ordenarlos por fecha de contratación
	 * @param lastName - apellido.
	 * @return lista de empleados.
	 */
	@GetMapping("/byLastNameOrdered")
	public List<Employee> findByLastNameOrderByHireDateAsc(@RequestParam String lastName) {
		return employeeRepository.findByLastNameOrderByHireDateAsc(lastName);
	}





}
