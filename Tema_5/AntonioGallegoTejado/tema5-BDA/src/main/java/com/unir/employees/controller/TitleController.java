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

//    /**
//     * Obtener la lista de s por nombre.
//     *
//     * @param deptName - nombre del departamento.
//     * @return departamento.
//     */
//    @GetMapping("/{name}")
//    public ResponseEntity<Department> getDepartmentByName(@PathVariable("name") String deptName) {
//        return ResponseEntity.ok(salaryRepository.findByDeptName(deptName).orElse(null));
//    }
//
//    /**
//     * Crear un nuevo departamento.
//     *
//     * @param department - departamento.
//     * @return departamento creado.
//     */
//    @PostMapping("/")
//    public Department createDepartment(@RequestBody Department department) {
//        return salaryRepository.save(department);
//    }
}
