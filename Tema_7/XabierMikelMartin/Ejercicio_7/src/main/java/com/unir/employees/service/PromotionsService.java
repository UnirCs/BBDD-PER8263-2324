package com.unir.employees.service;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.data.SalaryRepository;
import com.unir.employees.data.TitleRepository;
import com.unir.employees.data.DeptEmpRepository;
import com.unir.employees.model.db.*;
import com.unir.employees.model.request.PromotionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
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

    @Transactional
    public String promote(PromotionRequest promotionRequest) throws ParseException {


        //Obtendremos una referencia al empleado y al nuevo departamento. Si alguno de los dos no existe, el método termina dado que los datos de entrada no son correctos.
        Optional<Employee> empleado;
        Optional<Department> departamento;
        try {
            departamento = departmentRepository.findByDeptNo(promotionRequest.getDepartmentId());
            empleado = employeeRepository.findByEmpNo(promotionRequest.getEmployeeId());

            // Comprobación de que el departamento y el empleado existen
            if (empleado.isEmpty() || departamento.isEmpty())
                throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException("Referencia incorrecta del empleado o departamento");
        }

        //Obtendremos una referencia al título, salario y departamento actual del empleado. Si alguno de los tres no existe, el método termina dado que los datos de entrada no son correctos.
        Title latestTitle;
        Salary latestSalary;
        DeptEmp  latestDepartment;
        try
        {
            latestTitle = titleRepository.findTopByEmployeeEmpNoOrderByToDateDesc(promotionRequest.getEmployeeId());
            latestSalary = salaryRepository.findTopByEmployeeEmpNoOrderByToDateDesc(promotionRequest.getEmployeeId());
            latestDepartment = deptEmpRepository.findTopByEmployeeEmpNoOrderByToDateDesc(promotionRequest.getEmployeeId());
            if (latestSalary == null || latestDepartment == null || latestTitle == null)
                throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException("Referencai incorrecta del Salario, departamento o título ");
        }

        //Actualizamos el atributo toDate del título, salario y departamento actuales con el valor recibido en el atributo fromDate de la entrada del método.
        latestTitle.setToDate(promotionRequest.getFromDate());
        latestSalary.setToDate(promotionRequest.getFromDate());
        latestDepartment.setToDate(promotionRequest.getFromDate());
        //Actualizamos dichos registros en la base de datos.
        titleRepository.save(latestTitle);
        salaryRepository.save(latestSalary);
        deptEmpRepository.save(latestDepartment);

        //Tras la actualización, incluiremos una validación, y es que el salario no puede modificarse más de un 15% en una promoción.
        // En el caso de que esto no se cumpla se lanzará una nueva excepción, usando throw new RuntimeException().
        if (latestSalary.getSalary() * 1.15 < promotionRequest.getSalary())
            throw new RuntimeException("No se puede Aumentar el salario más del 15%");

        //Creamos nuevos objetos que representen el nuevo título, salario y departamento del empleado.
        // Usaremos para el atributo toDate el mismo valor que había previamente en los registros que modificamos.
        Title newTitle = new Title(empleado.get(),promotionRequest.getFromDate(),promotionRequest.getTitle(),formatter.parse("9999-01-01"));
        Salary newSalary = new Salary(empleado.get(),promotionRequest.getFromDate(),promotionRequest.getSalary(),formatter.parse("9999-01-01"));
        DeptEmp newDeptEmp = new DeptEmp(empleado.get(),departamento.get(),promotionRequest.getFromDate(),formatter.parse("9999-01-01"));

        //Guardamos los nuevos objetos en la base de datos.
        titleRepository.save(newTitle);
        salaryRepository.save(newSalary);
        deptEmpRepository.save(newDeptEmp);

        return "Promocionado :D";
    }
}
