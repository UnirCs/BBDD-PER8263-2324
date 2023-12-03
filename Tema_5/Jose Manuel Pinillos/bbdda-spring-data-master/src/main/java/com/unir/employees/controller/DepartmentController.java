package com.unir.employees.controller;

import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    /*
    @GetMapping("/{name}")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable("name") String deptName) {
        return ResponseEntity.ok(departmentRepository.findByDeptName(deptName).orElse(null));
    }
*/
    @GetMapping
    public List<Department> getDepartments(@RequestParam(value = "deptNo", required = false) String deptNo,
                                           @RequestParam(value = "deptName", required = false) String deptName) {

        if (StringUtils.hasText(deptNo)){
            return departmentRepository.findByDeptNo(deptNo);
        } else if (StringUtils.hasText(deptName)) {
            return departmentRepository.findByDeptName(deptName);
        } else {
            return departmentRepository.findAll().subList(0, 5);
        }
    }

    @GetMapping("/allOrdered")
    public List<Department> getDepartmentsOrderByDeptNameAsc() {
        return departmentRepository.findAllByOrderByDeptNameAsc();
    }

    /**
     * Buscar departamentos que tienen menos de una cantidad específica de empleados.
     *
     * @param count - cantidad de empleados.
     * @return lista de departamentos.
     */
    @GetMapping("/lessThanEmployees")
    public List<Department> getDepartmentsWithLessThanXEmployees(@RequestParam int count) {
        return departmentRepository.findDepartmentsWithLessThanXEmployees(count);
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
