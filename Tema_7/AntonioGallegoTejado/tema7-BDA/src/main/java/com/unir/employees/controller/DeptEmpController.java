package com.unir.employees.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.employees.data.DeptEmpRepository;
import com.unir.employees.model.db.DeptEmp;
import com.unir.employees.model.db.Employee;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deptEmp")
@RequiredArgsConstructor
public class DeptEmpController {

	private final DeptEmpRepository deptEmpRepository;

	
	/**
	 *  Obtener el departamento actual del empleado dado
	 *
	 * @param empNo - número de empleado.
	 * @return lista de puestos.
	 */
	@GetMapping("/{empNo}/actualDept")
	public DeptEmp getEmployeeLastTitle(@PathVariable("empNo") Integer empNo) {
		if (null!=empNo) {
			Employee employee= new Employee();
			employee.setEmpNo(empNo);
			return deptEmpRepository.getEmployeeActualDept(employee);
		} else {
			return null;
		}
	}
}
