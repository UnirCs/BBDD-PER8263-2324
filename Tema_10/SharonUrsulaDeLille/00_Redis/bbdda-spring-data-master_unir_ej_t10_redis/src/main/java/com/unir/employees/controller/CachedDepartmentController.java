package com.unir.employees.controller;

import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.model.db.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class CachedDepartmentController {

    private final DepartmentRepository departmentRepository;

    /**
     * Obtener un departamento por nombre.
     *
     * @param deptName - nombre del departamento.
     * @return departamento.
     */
    @GetMapping("/api-cached/departments/{name}")
    @Cacheable(value = "department", key = "#deptName")
    public Department getDepartmentByName(@PathVariable("name") String deptName) {
        return departmentRepository.findByDeptName(deptName).orElse(null);
    }

    /**
     * Obtener un departamento por id.
     * @param deptNo - id del empleado.
     * @return departamento.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Department> findDepartmentWithId(@PathVariable("id") String deptNo) {
        return ResponseEntity.ok(departmentRepository.findByDeptNo(deptNo).orElse(null));
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

    /**
     * Ordenar los departamentos en orden alfabetico
     * No hay parametro
     * @return lista de departamentos.
     */
    @GetMapping
    public List<Department> sortDepartments() {
        return departmentRepository.findAllByOrderByDeptName();
    }
}
