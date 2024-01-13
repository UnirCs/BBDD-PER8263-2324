package com.unir.employees.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.model.db.Employee;

import lombok.RequiredArgsConstructor;

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
	
	/**
	 * Crear un nuevo empleado.
	 *
	 * @param employee - empleado.
	 * @return empleado creado.
	 */
	@PostMapping("/")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}
	
	@GetMapping("/ByHireDate")
	public List<Employee> getEmployeesByHireDate(
			@RequestParam(value = "from", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date hireDateFrom,
			@RequestParam(value = "to", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date hireDateTo) {
		
		return employeeRepository.findAllByHireDateBetween(hireDateFrom, hireDateTo);
	}
}
