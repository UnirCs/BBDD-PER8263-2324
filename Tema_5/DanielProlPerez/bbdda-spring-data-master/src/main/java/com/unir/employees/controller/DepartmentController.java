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

    // ... (métodos existentes)

    /**
     * Obtener todos los departamentos ordenados por nombre de forma ascendente.
     *
     * @return lista de departamentos.
     */
    @GetMapping("/all")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAllByOrderByIdAsc();
    }

    /**
     * Contar el número de empleados en un departamento.
     *
     * @param deptName - nombre del departamento.
     * @return número de empleados en el departamento.
     */
    @GetMapping("/{name}/employeeCount")
    public long getEmployeeCountInDepartment(@PathVariable("name") String deptName) {
        return departmentRepository.countEmployeesByDeptName(deptName);
    }

    /**
     * Buscar departamentos por nombre que contengan una cadena específica.
     *
     * @param keyword - cadena a buscar en el nombre del departamento.
     * @return lista de departamentos que contienen la cadena especificada en el nombre.
     */
    @GetMapping("/search")
    public List<Department> searchDepartments(@RequestParam(value = "keyword") String keyword) {
        return departmentRepository.findByDeptNameContaining(keyword);
    }
}