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
    private final DeptEmptRepository deptEmpRepository;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    public String promote(PromotionRequest promotionRequest) throws ParseException, java.text.ParseException {
        //Obtendremos una referencia al empleado y al nuevo departamento. Si alguno de los dos no existe, el método termina dado que los datos de entrada no son correctos.
        Employee employee = employeeRepository.findByEmpNo(promotionRequest.getEmployeeId());
        Department department = departmentRepository.findByDeptNo(promotionRequest.getDepartmentId());
        if(employee == null || department == null){
            throw new IllegalArgumentException("ERROR! Empleado o/y departamento no conocido(s).");
        }

        //Obtendremos una referencia al título, salario y departamento actual del empleado. Si alguno de los tres no existe, el método termina dado que los datos de entrada no son correctos.
        Salary salaryActual = salaryRepository.findFirstByEmpNo_EmpNoOrderByToDateDesc(employee.getEmpNo());
        Title titleActual = titleRepository.findFirstByEmpNo_EmpNoOrderByToDateDesc(employee.getEmpNo());
        DeptEmp departmentActual = deptEmpRepository.findFirstByEmployeeEmpNoOrderByToDateDesc(employee.getEmpNo());

        if (salaryActual == null || titleActual == null || departmentActual == null){
            throw new IllegalArgumentException("ERROR! Título, salario o/y departamento actuel no conocido(s)");
        }

        //Actualizamos el atributo toDate del título, salario y departamento actuales con el valor recibido en el atributo fromDate de la entrada del método.
        salaryActual.setToDate(promotionRequest.getFromDate());
        titleActual.setToDate(promotionRequest.getFromDate());
        departmentActual.setToDate(promotionRequest.getFromDate());
        //Actualizamos dichos registros en la base de datos.
        salaryRepository.save(salaryActual);
        titleRepository.save(titleActual);
        deptEmpRepository.save(departmentActual);

        //Tras la actualización, incluiremos una validación, y es que el salario no puede modificarse más de un 15% en una promoción.
        //En el caso de que esto no se cumpla se lanzará una nueva excepción, usando throw new RuntimeException().
        if(salaryActual.getSalary()*1.15 < promotionRequest.getSalary()){
            throw new RuntimeException("Aumento de salario mayor a 15% no admitido");
        }

        //Creamos nuevos objetos que representen el nuevo título, salario y departamento del empleado. Usaremos para el atributo toDate el mismo valor que había previamente en los registros que modificamos.
        //Título
        Title titleNew = new Title();
        titleNew.setEmpNo(employee);
        titleNew.setTitle(promotionRequest.getTitle());
        titleNew.setFromDate(promotionRequest.getFromDate());
        titleNew.setToDate(formatter.parse("9999-01-01"));


        //Salario
        Salary salaryNew = new Salary();
        salaryNew.setEmpNo(employee);
        salaryNew.setFromDate(promotionRequest.getFromDate());
        salaryNew.setSalary(promotionRequest.getSalary());
        salaryNew.setToDate(formatter.parse("9999-01-01"));
        salaryNew.setTitle(promotionRequest.getTitle());

        //DeptEmp
        DeptEmp deptEmpNew = new DeptEmp();
        deptEmpNew.setEmployee(employee);
        deptEmpNew.setDepartment(department);
        deptEmpNew.setFromDate(promotionRequest.getFromDate());
        deptEmpNew.setToDate(formatter.parse("9999-01-01"));

        //Guardamos los nuevos objetos en la base de datos.
        salaryRepository.save(salaryNew);
        titleRepository.save(titleNew);
        deptEmpRepository.save(deptEmpNew);

        return "Promocionado :D";
    }
}
