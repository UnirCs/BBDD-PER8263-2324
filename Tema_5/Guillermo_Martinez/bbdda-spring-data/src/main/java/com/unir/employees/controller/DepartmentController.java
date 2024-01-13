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
     * Obtener todos los departamentos.
     *
     * @return lista de departamentos.
     */
    @GetMapping("/all")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * Actualizar un departamento existente.
     *
     * @param name       - nombre del departamento a actualizar.
     * @param department - nuevo estado del departamento.
     * @return departamento actualizado.
     */
    @PutMapping("/{name}")
    public ResponseEntity<Department> updateDepartment(@PathVariable("name") String name,
                                                       @RequestBody Department department) {
        return ResponseEntity.ok(departmentRepository.findByDeptName(name)
                .map(existingDepartment -> {
                    existingDepartment.setDeptNo(department.getLocation());
                    return departmentRepository.save(existingDepartment);
                })
                .orElse(null));
    }

    /**
     * Eliminar un departamento por nombre.
     *
     * @param name - nombre del departamento a eliminar.
     * @return respuesta de éxito.
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("name") String name) {
        departmentRepository.findByDeptName(name).ifPresent(departmentRepository::delete);
        return ResponseEntity.ok("Department deleted successfully");
    }
}


