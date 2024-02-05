//Ejercicio Tema7
package com.unir.employees.service;

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
    public String promote(PromotionRequest promotionRequest) throws Exception {
        // Obtenemos una referencia al empleado y al nuevo departamento. Si alguno de los dos no existe, el método termina dado que los datos de entrada no son correctos.
        Employee employee = employeeRepository.findById(promotionRequest.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("El empleado no existe"));

        Department newDepartment = departmentRepository.findById(promotionRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("El departamento no existe"));

        // Buscamos el último deptemp ordenado por ToDate descendente. Al ser una promoción puede ser null ya que puede que sea un nuevo departamento al que va la persona
        DeptEmp deptEmp = employee.getDeptEmps().stream()
                .filter(deptEmpOld -> deptEmpOld.getDepartment().equals(newDepartment) && deptEmpOld.getToDate().equals(getMaxDate()))
                .findFirst().orElse(null);

        // Obtenemos el último salario y título
        Salary salary = employee.getSalary().stream()
                .filter(salaryOld -> salaryOld.getToDate().equals(getMaxDate()))
                .findFirst().orElseThrow(() -> new RuntimeException("El empleado no tiene un salario previo, por lo tanto no es una promoción"));

        Title title = employee.getTitle().stream()
                .filter(titleOld -> titleOld.getToDate().equals(getMaxDate()))
                .findFirst().orElseThrow(() -> new RuntimeException("El empleado no tiene un título previo, por lo tanto no es una promoción"));

        // Actualizamos el atributo toDate del título, salario y departamento actuales con el valor recibido en el atributo fromDate de la entrada del método. Actualizamos dichos registros en la base de datos.
        title.setToDate(promotionRequest.getFromDate());
        titleRepository.save(title);

        salary.setToDate(promotionRequest.getFromDate());
        salaryRepository.save(salary);

        // Solo si el empleado ya pertenece al departamento actualizamos la fecha de fin del registro de departamento actual.
        if (deptEmp != null) {
            deptEmp.setToDate(promotionRequest.getFromDate());
            deptEmpRepository.save(deptEmp);
        }

        // Validamos que el salario no se modifica más de un 15%
        if (isSalaryIncreaseGreaterThanLimit(promotionRequest.getSalary(), salary.getSalary())) {
            throw new RuntimeException("El salario no puede modificarse más de un 15% en una promoción");
        }

        // Creamos nuevos objetos que representen el nuevo título, salario y departamento del empleado. Usaremos para el atributo toDate el mismo valor que había previamente en los registros que modificamos.
        Date dateTo = getMaxDate();

        Title newTitle = new Title(employee, promotionRequest.getTitle(), promotionRequest.getFromDate(), dateTo);
        Salary newSalary = new Salary(employee, promotionRequest.getSalary(), promotionRequest.getFromDate(), dateTo);
        DeptEmp newDeptEmp = new DeptEmp(employee, newDepartment, promotionRequest.getFromDate(), dateTo);

        // Guardamos los nuevos registros en la base de datos.
        titleRepository.saveAndFlush(newTitle);
        salaryRepository.saveAndFlush(newSalary);
        deptEmpRepository.saveAndFlush(newDeptEmp);

        return "Promocionado :D";
    }

    private boolean isSalaryIncreaseGreaterThanLimit(BigDecimal newSalary, BigDecimal currentSalary) {
        BigDecimal salaryIncrease = newSalary.subtract(currentSalary);
        BigDecimal salaryIncreasePercentage = salaryIncrease.divide(currentSalary, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return salaryIncreasePercentage.abs().compareTo(new BigDecimal(15)) > 0;
    }

    private Date getMaxDate() {
        try {
            return formatter.parse("9999-01-01");
        } catch (ParseException e) {
            throw new RuntimeException("Error al parsear la fecha máxima", e);
        }
    }
}
