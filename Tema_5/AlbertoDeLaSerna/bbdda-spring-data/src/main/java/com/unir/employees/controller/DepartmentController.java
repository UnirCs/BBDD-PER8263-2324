package com.unir.employees.controller;

import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.model.db.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * Listar todos los departamentos en orden alfabético por nombre.
     *
     * @return lista de departamentos.
     */
    @GetMapping("/allOrdered")
    public List<Department> findAllByOrderByDeptNameAsc() {
        return departmentRepository.findAllByOrderByDeptNameAsc();
    }

    /**
     * Buscar departamentos por nombre.
     *
     * @param name - nombre.
     * @return lista de departamentos.
     */
    @GetMapping("/byName")
    public List<Department> findByDeptNameContaining(@RequestParam String name) {
        return departmentRepository.findByDeptNameContaining(name);
    }

    /**
     * Buscar departamentos que tienen más de una cantidad específica de empleados.
     *
     * @param count - cantidad de empleados.
     * @return lista de departamentos.
     */
    @GetMapping("/moreThanEmployees")
    public List<Department> findDepartmentsWithMoreThanXEmployees(@RequestParam int count) {
        return departmentRepository.findDepartmentsWithMoreThanXEmployees(count);
    }

    /**
     * Buscar departamentos que tienen menos de una cantidad específica de empleados.
     *
     * @param count - cantidad de empleados.
     * @return lista de departamentos.
     */
    @GetMapping("/lessThanEmployees")
    public List<Department> findDepartmentsWithLessThanXEmployees(@RequestParam int count) {
        return departmentRepository.findDepartmentsWithLessThanXEmployees(count);
    }

    /**
     * Buscar departamentos que no tienen ningún gerente asignado.
     *
     * @return lista de departamentos.
     */
    @GetMapping("/withoutManagers")
    public List<Department> findDepartmentsWithoutManagers() {
        return departmentRepository.findDepartmentsWithoutManagers();
    }

    /** Buscar departamentos con un número específico de empleados.
     *
     * @param count - cantidad de empleados.
     * @return lista de departamentos.
     */
    @GetMapping("/exactEmployees")
    public List<Department> findDepartmentsWithExactNumberOfEmployees(@RequestParam int count) {
        return departmentRepository.findDepartmentsWithExactNumberOfEmployees(count);
    }



}
