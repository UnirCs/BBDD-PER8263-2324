package com.unir.employees.controller;

import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.model.db.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	 * Obtener un empleado por número de empleado.
	 *
	 * @param empNo - número de empleado.
	 * @return empleado.
	 */
	@GetMapping("/{empNo}")
	public ResponseEntity<Employee> getEmployeeByEmpNo(@PathVariable("empNo") Integer empNo) {
		return ResponseEntity.ok(employeeRepository.findById(empNo).orElse(null));
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



}
