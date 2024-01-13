package com.unir.employees.controller;

import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.model.db.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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
	@GetMapping("/getEmployees")
	public List<Employee> getEmployees(@RequestParam(value = "lastName", required = false) String lastName) {

		if(StringUtils.hasText(lastName)) {
			return employeeRepository.findByLastName(lastName);
		} else {
			return employeeRepository.findAll().subList(0, 20);
		}
	}
	@GetMapping("/getBirthDate")
	public List<Employee> getBirthDate(@RequestParam(value = "birthDate", required = false) Date birthDate) {


			return employeeRepository.findByBirthDate(birthDate);


	}
	@GetMapping("/getBirthDateAfter")
	public List<Employee> getBirthDateAfter(@RequestParam(value = "birthDate", required = false) Date birthDate) {


		return employeeRepository.findByBirthDateAfter(birthDate).subList(0, 20);


	}
	@GetMapping("/paraJubilacion")
	public List<Employee> getParaJubilaciom() {

		return employeeRepository.findAllByOrderByHireDateAsc().subList(0, 20);
	}

}
