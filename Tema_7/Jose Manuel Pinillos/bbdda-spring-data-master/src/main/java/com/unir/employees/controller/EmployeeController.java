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

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeRepository employeeRepository;

	@GetMapping
	public List<Employee> getEmployees(@RequestParam(value = "firstName", required = false) String firstName,
									   @RequestParam(value = "lastName", required = false) String lastName,
									   @RequestParam (value = "gender", required = false) Gender gender) {

		if (StringUtils.hasText(firstName) && StringUtils.hasText(lastName)) {
			return employeeRepository.findByFirstNameAndLastName(firstName, lastName);
		} else if(StringUtils.hasText(firstName)){
			return employeeRepository.findByFirstName(firstName);
		} else if (StringUtils.hasText(lastName)) {
			return employeeRepository.findByLastName(lastName);
		} else if (StringUtils.hasText(gender.toString())) {
			return employeeRepository.findByGender(gender).subList(0, 10); //muestra solo los 10 primeros
		} else {
			return employeeRepository.findAll().subList(0, 20);
		}
	}
	@GetMapping("/orderByName/{name}")
	public List<Employee> getEmployeesByFirstNameOrderByBirth(@PathVariable String name) {
		return employeeRepository.findByFirstNameOrderByBirthDateDesc(name);
	}
}
