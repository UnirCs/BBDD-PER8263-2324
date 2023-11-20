package com.unir.employees.controller;

import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping("/getDepartmentByName/{name}")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable("name") String deptName) {
        return ResponseEntity.ok(departmentRepository.findByDeptName(deptName).orElse(null));
    }

    /**
     * Crear un nuevo departamento.
     *
     * @param department - departamento.
     * @return departamento creado.
     */
    @PostMapping("/createDepartment")
    public Department createDepartment(@RequestBody Department department) {
        return departmentRepository.save(department);
    }


    /**
     * Obtener todos los departamentos.
     *
     *
     * @return departamento creado.
     */
    @GetMapping("/getDepartments")
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }


    /**
     * Borrar un departamento
     *
     * @param deptName nombre del departamento
     * @return empleado borrado
     */
    @DeleteMapping("/deleteDepartmentByName/{deptName}")
    public boolean deleteDepartmentByName(@PathVariable(value = "deptName", required = true) String deptName) {
        boolean deleted = false;
       Optional<Department> department = departmentRepository.findByDeptName(deptName);

        if(department.isPresent()) {
            Department d= department.stream().toList().get(0);
            departmentRepository.delete(d);
            deleted = true;
        }

        return deleted;
    }


    /**
     *Buscar departamentos con más empleados de un numero dado.
     *
     * @param limit - cantidad de empleados.
     * @return lista de departamentos.
     */
    @GetMapping("/moreThan")
    public List<Department> findDepartmentsWithMoreThanXEmployees(@RequestParam int limit) {
        return departmentRepository.findDepartmentsWithMoreThanXEmployees(limit);
    }

    /**
     * Departamentos en orden por id.
     *
     * @return lista de departamentos.
     */
    @GetMapping("/allByIdAsc")
    public List<Department> findAllByOrderByDeptNoAsc() {
        return departmentRepository.findAllByOrderByDeptNo();
    }

    /**
     * Departamentos en orden alfabético por nombre.
     *
     * @return lista de departamentos.
     */
    @GetMapping("/allByNameAsc")
    public List<Department> findAllByOrderByDeptNameAsc() {
        return departmentRepository.findAllByOrderByDeptNameAsc();
    }
}
