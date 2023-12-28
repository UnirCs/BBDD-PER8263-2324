package com.unir.employees.service;

import com.unir.employees.data.*;
import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.DeptEmp;
import com.unir.employees.model.db.Salary;
import com.unir.employees.model.db.Title;
import com.unir.employees.model.request.PromotionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PromotionsService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TitleRepository titleRepository;
    private final SalaryRepository salaryRepository;
    private final DeptEmpRepository deptEmpRepository;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Método que promociona a un empleado
     * @param promotionRequest
     * @return
     * @throws ParseException
     */
    @Transactional
    public String promote(PromotionRequest promotionRequest) throws Exception {
        // Obtenemos una referencia al empleado y al nuevo departamento. Si alguno de los dos no existe, el método termina dado que los datos de entrada no son correctos.
        var employee = employeeRepository.findById(promotionRequest.getEmployeeId()).orElse(null);
        if (employee == null) {
            throw new RuntimeException("El empleado no existe");
        }

        var department = departmentRepository.findById(promotionRequest.getDepartmentId()).orElse(null);
        if (department == null) {
            throw new RuntimeException("El departamento no existe");
        }


        var lastDate = formatter.parse("9999-01-01");

        // busco de employee el último deptemp ordenado por ToDate descendente. Al ser una promoción puede ser null ya que puede que sea un nuevo departamento al que va la persona
        var deptEmp = employee.getDeptEmps().stream().filter(
                deptEmpOld -> deptEmpOld.getDepartment().getDeptNo().toLowerCase().equals(department.getDeptNo().toLowerCase())
                    && deptEmpOld.getToDate().equals(lastDate)
        ).findFirst().orElse(null);


        // busco de employee el último salary ordenado por ToDate descendente.
        var salary = employee.getSalary().stream().filter(
                salaryOld ->
                        salaryOld.getToDate().equals(lastDate)
        ).findFirst().orElse(null);
        if (salary == null) {
            throw new RuntimeException("El empleado no tiene un salario previo, por lo tanto no es una promoción");
        }

        // busco de employee el último title ordenado por ToDate descendente.
        var title = employee.getTitle().stream().filter(
                titleOld1 ->
                        titleOld1.getToDate().equals(lastDate)
        ).findFirst().orElse(null);
        if (title == null) {
            throw new RuntimeException("El empleado no tiene un título previo, por lo tanto no es una promoción");
        }


        // Actualizamos el atributo toDate del título, salario y departamento actuales con el valor recibido en el atributo fromDate de la entrada del método. Actualizamos dichos registros en la base de datos.
        title.setToDate(promotionRequest.getFromDate());
        titleRepository.save(title);

        salary.setToDate(promotionRequest.getFromDate());
        salaryRepository.save(salary);

        // Sólo si el empleado ya pertenece al departamento actualizamos la fecha de fin del registro de departamento actual.
        if (deptEmp != null) {
            deptEmp.setToDate(promotionRequest.getFromDate());
            deptEmpRepository.save(deptEmp);
        }


        // Si el salario ya existe con los mismos datos que los recibidos en la entrada del método, el método termina dado que los datos de entrada no son correctos.
        var salaryIncreasePercentage = getSalaryIncreasePercentage(promotionRequest, salary, department);
        if (salaryIncreasePercentage > 15) {
            throw new RuntimeException("El salario no puede modificarse más de un 15% en una promoción");
        }

        // Creamos nuevos objetos que representen el nuevo título, salario y departamento del empleado. Usaremos para el atributo toDate el mismo valor que había previamente en los registros que modificamos.
        Date dateTo = formatter.parse("9999-01-01");
        var newTitle = new Title();
        newTitle.setEmployee(employee);
        newTitle.setTitle(promotionRequest.getTitle());
        newTitle.setFromDate(promotionRequest.getFromDate());
        newTitle.setToDate(dateTo);

        var newSalary = new Salary();
        newSalary.setEmployee(employee);
        newSalary.setSalary(promotionRequest.getSalary());
        newSalary.setFromDate(promotionRequest.getFromDate());

        newSalary.setToDate(dateTo);

        var newDeptEmp = new DeptEmp();
        newDeptEmp.setEmployee(employee);
        newDeptEmp.setDepartment(department);
        newDeptEmp.setFromDate(promotionRequest.getFromDate());
        newDeptEmp.setToDate(dateTo);

        // Guardamos los nuevos registros en la base de datos.
        titleRepository.saveAndFlush(newTitle);
        salaryRepository.saveAndFlush(newSalary);
        deptEmpRepository.saveAndFlush(newDeptEmp);



        return "Promocionado :D";
    }

    private static int getSalaryIncreasePercentage(PromotionRequest promotionRequest, Salary salary, Department department) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = salary.getFromDate(); // Tu primera fecha
        Date date2 = promotionRequest.getFromDate(); // Tu segunda fecha

        String strDate1 = sdf.format(date1);
        String strDate2 = sdf.format(date2);

        if (salary.getEmployee().getEmpNo().equals(promotionRequest.getEmployeeId())
                && salary.getSalary().equals(promotionRequest.getSalary())
                && strDate1.equals(strDate2)) {
            throw new RuntimeException("El salario ya existe con los mismos datos que los recibidos en la entrada del método");
        }

        // Validación, el salario no puede modificarse más de un 15% en una promoción. En el caso de que esto no se cumpla se lanzará una nueva excepción, usando throw new RuntimeException().
        var salaryIncrease = promotionRequest.getSalary() - promotionRequest.getSalary();
        var salaryIncreasePercentage = salaryIncrease / salary.getSalary() * 100;
        return salaryIncreasePercentage;
    }
}