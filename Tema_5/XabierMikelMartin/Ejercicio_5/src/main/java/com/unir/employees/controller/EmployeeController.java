package com.unir.employees.controller;

import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
	@GetMapping("/getEmployeesByLastName")
	public List<Employee> getEmployeesByLastName(@RequestParam(value = "lastName", required = false) String lastName) {

		if(StringUtils.hasText(lastName)) {
			return employeeRepository.findByLastName(lastName);
		} else {
			return employeeRepository.findAll().subList(0, 20);
		}
	}

	/**
	 * Obtener empleados por nombre.

	 *
	 * @param firstName - nombre del empleado.
	 * @return lista de empleados.
	 */

	@GetMapping("/getEmployeeByName/{firstName}")
	public ResponseEntity<List<Employee>> getEmployeeByName(@PathVariable(value = "firstName", required = true) String firstName) {
		return ResponseEntity.ok(employeeRepository.findByFirstName(firstName));
	}


	/**
	 * Crear un nuevo empleado.
	 *
	 * @param employee - empleado.
	 * @return Empleado creado.
	 */
	@PostMapping("/createEmployee")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}

	/**
	 * Borrar un empleado
	 *
	 * @param empNo
	 * @return empleado borrado
	 */
	@DeleteMapping("/deleteEmployee/{empNo}")
	public boolean deleteEmployee(@PathVariable(value = "empNo", required = true) Integer empNo) {
		boolean deleted = false;

		if(employeeRepository.existsById(empNo)) {
			Optional<Employee> employee = employeeRepository.findById(empNo);
			Employee e= employee.stream().toList().get(0);
			employeeRepository.delete(e);
			deleted = true;
		}

		return deleted;
	}


	/**
	 * Número de empleados por género.
	 *
	 * @param gender - género del empleado (M o F).
	 * @return número de empleados por género.
	 */
	@GetMapping("/countByGender")
	public long countEmployeesByGender(@RequestParam Gender gender) {

		return employeeRepository.countByGender(gender);
	}


}
