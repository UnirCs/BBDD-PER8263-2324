package com.unir.employees.service;

import com.unir.employees.data.*;
import com.unir.employees.model.db.*;
import com.unir.employees.model.request.PromotionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
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
    public String promote(PromotionRequest promotionRequest) throws ParseException {
        int empNo;
        String deptId;
        Optional<Employee> empleado;
        Optional<Department> departamento;

        Optional<Salary> oldSalary;
        Optional<Department> oldDepartment;
        Optional<Title> oldTitle;

        // Comprobación de que el departamento y el empleado existen
        try {
            empNo = promotionRequest.getEmployeeId();
            deptId = promotionRequest.getDepartmentId();
            departamento = departmentRepository.findByDeptNo(deptId);
            empleado = employeeRepository.findByEmpNo(empNo);

            // Comprobación de que el departamento y el empleado existen
            if (empleado.isEmpty() || departamento.isEmpty())
                throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException("Falta una referencia correcta al empleado y / o al nuevo departamento");
        }

        // Comprobación de que el empleado tenía un salario título y puesto anterior. No se puede promocionar a alguien que es la primera vez que trabaja
        try {
            oldSalary = salaryRepository.findLastSalary(empNo);
            oldTitle = titleRepository.findLastTitle(empNo);
            oldDepartment = departmentRepository.findByEmpNo(String.valueOf(empNo));

            // Comprobación de que el empleado tenía un salario título y puesto anterior.
            if (oldTitle.isEmpty() || oldSalary.isEmpty() || oldDepartment.isEmpty())
                throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException("Falta una referencia correcta al título, salario y / o departamento actual del empleado ");
        }

        // Si el salario cambia se actualiza
        if (oldSalary.get().getSalary() != promotionRequest.getSalary())
            salaryRepository.updateLastSalary(empNo, formatter.format(promotionRequest.getFromDate()));

        // Si el título cambia se actualiza
        if (!Objects.equals(oldTitle.get().getTitle(), promotionRequest.getTitle()))
            titleRepository.updateLastTitle(empNo, formatter.format(promotionRequest.getFromDate()));

        // Si el departamento cambia se actualiza
        if (!Objects.equals(oldDepartment.get().getDeptNo(), promotionRequest.getDepartmentId()))
            deptEmpRepository.updateLastDeptEmpt(empNo, formatter.format(promotionRequest.getFromDate()));

        // Condición de que el nuevo salario solo puede ser un 15% mayor
        if (oldSalary.get().getSalary() * 1.15 < promotionRequest.getSalary())
            throw new RuntimeException("Aumento de salario mayor al 15%");

        Date dateTo = formatter.parse("9999-01-01");

        // No creamos un nuevo salario si no se ha actualizado el anterior al no haber uno nuevo
        if (oldSalary.get().getSalary() != promotionRequest.getSalary()) {
            Salary nuevoSalario = new Salary(empleado.get(), promotionRequest.getFromDate(), dateTo, promotionRequest.getSalary());
            salaryRepository.save(nuevoSalario);
        }

        // No creamos un nuevo título si no se ha actualizado el anterior al no haber uno nuevo
        if (!Objects.equals(oldTitle.get().getTitle(), promotionRequest.getTitle())) {
            Title nuevoTitulo = new Title(empleado.get(), promotionRequest.getFromDate(), promotionRequest.getTitle(), dateTo);
            titleRepository.save(nuevoTitulo);
        }

        // No asignamos un nuevo departamento si no se ha actualizado el anterior al no haber uno nuevo
        if (!Objects.equals(oldDepartment.get().getDeptNo(), promotionRequest.getDepartmentId())) {
            DeptEmp nuevoDeptEmpt = new DeptEmp(empleado.get(), departamento.get(), promotionRequest.getFromDate(), dateTo);
            deptEmpRepository.save(nuevoDeptEmpt);
        }

        return "Promocionado :D";
    }
}