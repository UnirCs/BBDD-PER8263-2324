package com.unir.employees.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.model.db.Department;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

	private final DepartmentRepository departmentRepository;

	/**
	 * Obtener un departamento por nombre.
	 *
	 * @param deptName - nombre del departamento.
	 * @return departamento.
	 */
	@GetMapping("/{name}")
	public ResponseEntity<Department> getDepartmentByName(@PathVariable("name") String deptName) {
		return ResponseEntity.ok(departmentRepository.findByDeptName(deptName).orElse(null));
	}

	/**
	 * Crear un nuevo departamento.
	 *
	 * @param department - departamento.
	 * @return departamento creado.
	 */
	@PostMapping("/")
	public Department createDepartment(@RequestBody Department department) {
		return departmentRepository.save(department);
	}
	
	@GetMapping("/ByEmpNumber")
	public List<Department> findDepartmentsByEmployeeNumber(
			@RequestParam(value = "from", required = true) Integer from, 
			@RequestParam(value = "to", required = true)Integer to) {
		return departmentRepository.findAllByEmployeeNumber(from==null?0:from, to==null?0:to);
	}
}
