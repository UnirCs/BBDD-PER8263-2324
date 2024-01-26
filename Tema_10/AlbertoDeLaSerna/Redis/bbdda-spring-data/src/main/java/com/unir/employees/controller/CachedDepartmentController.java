package com.unir.employees.controller;

import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.model.db.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CachedDepartmentController {

    private final DepartmentRepository departmentRepository;

    @GetMapping("/api-cached/departments/{name}")
    @Cacheable(value = "department", key = "#deptName")
    public Department getDepartmentByName(@PathVariable("name") String deptName) {
        return departmentRepository.findByDeptName(deptName).orElse(null);
    }
}

