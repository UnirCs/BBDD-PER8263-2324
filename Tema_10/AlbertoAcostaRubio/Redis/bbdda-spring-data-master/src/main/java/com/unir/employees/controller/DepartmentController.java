package com.unir.employees.controller;
import java.util.List;
import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.model.db.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Cacheable(value = "department", key = "#deptName")
    @GetMapping("/{name}")
    public Department getDepartmentByName(@PathVariable("name") String deptName) {
        return departmentRepository.findByDeptName(deptName).orElse(null);
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
    @GetMapping("/departamentos")
    public List<Department> findAllByOrderByDeptNameAsc() {
        return departmentRepository.findAllByOrderByDeptNameAsc();
    }
}
