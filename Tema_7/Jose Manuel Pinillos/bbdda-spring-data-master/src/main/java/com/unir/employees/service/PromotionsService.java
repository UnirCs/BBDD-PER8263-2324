package com.unir.employees.service;

import com.unir.employees.data.*;
import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;
import com.unir.employees.model.db.Title;
import com.unir.employees.model.request.PromotionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionsService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TitleRepository titleRepository;
    private final SalaryRepository salaryRepository;
    private final DeptEmpRepository deptEmpRepository;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    // Método para promocionar a un empleado
    @Transactional
    public String promote(PromotionRequest promotionRequest) throws ParseException {

        int empNo;
        Optional<Employee> employee;
        String depNo;
        Optional<Department> department;
        Title title;
        Salary salary;

        // Se comprueba que el empleado existe
        try {
            employee = employeeRepository.findById(promotionRequest.getEmployeeId());

            if (employee == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new RuntimeException("El empleado no existe");
        }

        // Se comprueba que el departamento existe
        try {
            depNo = promotionRequest.getDepartmentId();
            department = departmentRepository.findById(depNo);

            if (department == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new RuntimeException("El departamento no existe");
        }



        return "Promocionado :D";
    }
}