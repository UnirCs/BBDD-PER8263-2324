package com.unir.employees.controller;

import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.model.db.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    /**
     * Obtener un departamento por nombre del empleado.
     *
     * @param idEmp - id del empleado del departamento.
     * @return departamento.
     */
    @GetMapping("/employee/{empNo}")
    public ResponseEntity<Department> getDepartmentsByIdEmp(@PathVariable("empNo") String idEmp) {
        return ResponseEntity.ok(departmentRepository.findByEmpNo(idEmp).orElse(null));
    }
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
}
