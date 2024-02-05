package com.unir.employees.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.employees.data.SalaryRepository;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/salaries")
@RequiredArgsConstructor
public class SalaryController {

	private final SalaryRepository salaryRepository;

	/**
	 * Obtener la lista de salarios de un empleado dado.
	 *
	 * @param empNo - número de empleado.
	 * @return lista de salarios del empleado.
	 */
	@GetMapping("/{empNo}")
	public List<Salary> getSalaries(@PathVariable("empNo") Integer empNo) {
		if (null!=empNo) {
			Employee employee= new Employee();
			employee.setEmpNo(empNo);
			return salaryRepository.findByEmployee(employee);
		} else {
			return null;
		}
	}

	
	/**
	 *  Obtener el salario actual del empleado dado
	 *
	 * @param empNo - número de empleado.
	 * @return lista de puestos.
	 */
	@GetMapping("/{empNo}/actualSalary")
	public Salary getEmployeeLastTitle(@PathVariable("empNo") Integer empNo) {
		if (null!=empNo) {
			Employee employee= new Employee();
			employee.setEmpNo(empNo);
			return salaryRepository.getEmployeeActualSalary(employee);
		} else {
			return null;
		}
	}
}
