package com.unir.employees.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.employees.data.TitleRepository;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Title;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/titles")
@RequiredArgsConstructor
public class TitleController {

	private final TitleRepository titleRepository;

	/**
	 *  Obtener la lista de puestos desempeñados por un empleado dado.
	 *
	 * @param empNo - número de empleado.
	 * @return lista de puestos.
	 */
	@GetMapping("/{empNo}")
	public List<Title> getTitles(@PathVariable("empNo") Integer empNo) {
		if (null!=empNo) {
			Employee employee= new Employee();
			employee.setEmpNo(empNo);
			return titleRepository.findByEmployee(employee);
		} else {
			return null;
		}
	}
	
	/**
	 *  Obtener el puesto actual del empleado dado
	 *
	 * @param empNo - número de empleado.
	 * @return lista de puestos.
	 */
	@GetMapping("/{empNo}/actualTitle")
	public Title getEmployeeLastTitle(@PathVariable("empNo") Integer empNo) {
		if (null!=empNo) {
			Employee employee= new Employee();
			employee.setEmpNo(empNo);
			return titleRepository.getEmployeeActualTitle(employee);
		} else {
			return null;
		}
	}

}
