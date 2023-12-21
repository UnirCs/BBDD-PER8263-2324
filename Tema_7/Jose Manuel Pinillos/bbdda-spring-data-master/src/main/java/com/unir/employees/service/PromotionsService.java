package com.unir.employees.service;

import com.unir.employees.data.*;
import com.unir.employees.model.db.*;
import com.unir.employees.model.request.PromotionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public String promote(PromotionRequest promotionRequest) throws Exception {

        // Creamos las fecha  que representará a to_date
        Date lastDate = formatter.parse("9999-01-01");

        int empNo;
        Employee employee;
        String depNo;
        Department department;

        // Creamos los objetos que representarán el antiguo título, salario y departamento.
        Title oldTitle;
        Salary oldSalary;
        DeptEmp oldDeptEmp;

        // Creamos los objetos que representarán el nuevo título, salario y departamento.
        Title newTitle = new Title();
        Salary newSalary = new Salary();
        DeptEmp newDeptEmp = new DeptEmp();

        // Se comprueba que el empleado existe
        empNo = promotionRequest.getEmployeeId();
        employee = employeeRepository.findByEmpNo(empNo);

        if (employee == null) {
            throw new RuntimeException("El empleado no existe");
        }

        // Se comprueba que el departamento existe
        depNo = promotionRequest.getDepartmentId();
        department = departmentRepository.findByDeptNo(depNo);

        if (department == null) {
            throw new RuntimeException("El departamento no existe");
        }

        // Se comprueba que el empleado tiene título, salario y departamento
        oldTitle = titleRepository.findTitleByEmpNoAndToDate(employee,lastDate);
        oldSalary = salaryRepository.findSalaryByEmpNoAndToDate(employee,lastDate);
        oldDeptEmp = deptEmpRepository.findDeptEmpByEmployeeAndToDate(employee, lastDate);

        if (oldTitle == null || oldSalary == null || oldDeptEmp == null) {
            throw new RuntimeException("El empleado no puede promocionar");
        }

        //Actualizamos el atributo toDate del título
        oldTitle.setToDate(promotionRequest.getFromDate());
        titleRepository.save(oldTitle);

        // Actualizamos el atributo toDate del salario
        oldSalary.setToDate(promotionRequest.getFromDate());
        salaryRepository.save(oldSalary);

        // Actualizamos el atributo toDate del deptartamento
        oldDeptEmp.setToDate(promotionRequest.getFromDate());
        deptEmpRepository.save(oldDeptEmp);

        if (promotionRequest.getSalary() > (oldSalary.getSalary() * 1.15)) {
            throw new RuntimeException("El nuevo salario no puede superar un 15% del anterior");
        }

        // Actualizamos el objeto que representa al nuevo título
        newTitle.setEmpNo(employee);
        newTitle.setTitle(promotionRequest.getTitle());
        newTitle.setFromDate(promotionRequest.getFromDate());
        newTitle.setToDate(lastDate);

        // Actualizamos el objeto que representa al nuevo salario
        newSalary.setEmpNo(employee);
        newSalary.setSalary(promotionRequest.getSalary());
        newSalary.setFromDate(promotionRequest.getFromDate());
        newSalary.setToDate(lastDate);

        // Actualizamos el objeto que representa al nuevo departamento
        newDeptEmp.setEmployee(employee);
        newDeptEmp.setDepartment(department);
        newDeptEmp.setFromDate(promotionRequest.getFromDate());
        newDeptEmp.setToDate(lastDate);

        // Guardamos los nuevos registros en la base de datos.
        titleRepository.save(newTitle);
        salaryRepository.save(newSalary);
        deptEmpRepository.save(newDeptEmp);

        return "Promocionado :D";
    }
}