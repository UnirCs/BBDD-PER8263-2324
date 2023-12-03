package com.unir.employees.controller;

import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeRepository employeeRepository;

	// ... (métodos existentes)

	/**
	 * Buscar empleados por nombre y apellido.
	 *
	 * @param firstName - nombre del empleado.
	 * @param lastName  - apellido del empleado.
	 * @return lista de empleados que coinciden con el nombre y apellido especificados.
	 */
	@GetMapping("/search")
	public List<Employee> searchEmployees(
			@RequestParam(value = "firstName") String firstName,
			@RequestParam(value = "lastName") String lastName) {
		return employeeRepository.findByFirstNameAndLastName(firstName, lastName);
	}

	/**
	 * Buscar empleados por fecha de contratación posterior a una fecha dada.
	 *
	 * @param hireDate - fecha de contratación.
	 * @return lista de empleados contratados después de la fecha especificada.
	 */
	@GetMapping("/hireDateAfter")
	public List<Employee> getEmployeesHiredAfter(@RequestParam(value = "hireDate") Date hireDate) {
		return employeeRepository.findByHireDateAfter(hireDate);
	}

	/**
	 * Contar el número de empleados por género.
	 *
	 * @param gender - género del empleado (M o F).
	 * @return número de empleados por género.
	 */
	@GetMapping("/countByGender")
	public long countEmployeesByGender(@RequestParam(value = "gender") Gender gender) {
		return employeeRepository.countByGender(gender);
	}

	/**
	 * Obtener todos los empleados ordenados por fecha de contratación de forma descendente.
	 *
	 * @return lista de empleados.
	 */
	@GetMapping("/all")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAllByOrderByHireDateDesc();
	}
}