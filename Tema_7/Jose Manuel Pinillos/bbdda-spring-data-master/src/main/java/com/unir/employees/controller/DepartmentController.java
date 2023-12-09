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
/*
    @GetMapping("/lessThanEmployees")
    public List<Department> getDepartmentsWithLessThanXEmployees(@RequestParam int count) {
        return departmentRepository.findDepartmentsWithLessThanXEmployees(count);
    }
*/
}
