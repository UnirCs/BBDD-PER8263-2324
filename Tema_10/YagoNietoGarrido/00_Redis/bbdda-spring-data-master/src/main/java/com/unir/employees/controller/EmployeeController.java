package com.unir.employees.controller;

import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    /**
     * Obtener empleados por nombre.
     *
     * @param lastName - apellido.
     * @return lista de empleados.
     */
    @GetMapping("firstName/{firstName}")
    @Cacheable(value = "employee", key = "#lastName")
    public List<Employee> getEmployeesByFirstName(@PathVariable("firstName") String lastName) {
        return employeeRepository.findByFirstName(lastName).orElse(null);
    }

    /**
     * Obtener empleados por apellido.
     * Si no se especifica apellido, se devuelven los primeros 20 empleados.
     *
     * @param lastName - apellido.
     * @return lista de empleados.
     */
    @GetMapping
    @Cacheable(value = "employee", key = "#lastName")
    public List<Employee> getEmployeesByLastName(@RequestParam(value = "lastName", required = false) String lastName) {

        if (StringUtils.hasText(lastName)) {
            return employeeRepository.findByLastName(lastName);
        } else {
            return employeeRepository.findAll().subList(0, 20);
        }
    }

    /**
     * Obtener empleado con maximo salario del departamento.
     *
     * @param idDepartment - identificador del departamento.
     * @return lista de empleados.
     */
    @GetMapping("/maxSalary/department/{idDepartment}")
    @Cacheable(value = "employee", key = "#idDepartment")
    public List<Employee> getEmployeeMaxSalaryDepartment(@PathVariable("idDepartment") String idDepartment) {
        return employeeRepository.findMaxSalaryEmployeeByDepartment(idDepartment).orElse(null);
    }

    /**
     * Obtener empleado con maximo salario del departamento.
     *
     * @param date1 - fecha más pequeña.
     * @param date2 - fecha más grande.
     * @return lista de empleados.
     */
    @GetMapping("/hireDate/{date1}/{date2}")
    public List<Employee> getEmployeesHireDateBetween(@PathVariable("date1") Date date1, @PathVariable("date2") Date date2) {
        if (date1.before(date2)) {
            return employeeRepository.findEmployeesByHireDateBetween(date1, date2).orElse(null);
        } else {
            return null;
        }
    }

    /**
     * Crear un nuevo empleado.
     *
     * @param employee - empleado.
     * @return departamento creado.
     */
    @PostMapping("/")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }
}
