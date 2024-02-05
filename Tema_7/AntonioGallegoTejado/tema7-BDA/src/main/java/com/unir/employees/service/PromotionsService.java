package com.unir.employees.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.employees.data.DepartmentRepository;
import com.unir.employees.data.DeptEmpRepository;
import com.unir.employees.data.EmployeeRepository;
import com.unir.employees.data.SalaryRepository;
import com.unir.employees.data.TitleRepository;
import com.unir.employees.model.db.Department;
import com.unir.employees.model.db.DeptEmp;
import com.unir.employees.model.db.Employee;
import com.unir.employees.model.db.Salary;
import com.unir.employees.model.db.Title;
import com.unir.employees.model.request.PromotionRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionsService {

	private final DepartmentRepository departmentRepository;
	private final EmployeeRepository employeeRepository;
	private final TitleRepository titleRepository;
	private final SalaryRepository salaryRepository;
	private final DeptEmpRepository deptEmpRepository;
	private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private final String finalDate = "9999-01-01";

	@Transactional
	public String promote(PromotionRequest promotionRequest) throws ParseException {
		//1.-Obtendremos una referencia al empleado y al nuevo departamento. Si alguno de 
		//los dos no existe, el método termina dado que los datos de entrada no son correctos.
		
		Optional<Employee> employeeOpt = employeeRepository.findById(promotionRequest.getEmployeeId());
		if(employeeOpt.isEmpty()) {
			throw new RuntimeException("ERROR: El empleado con el id "+promotionRequest.getEmployeeId()+" NO existe.");
		}
		Employee employee = employeeOpt.get();
		
		Optional<Department> departmentOpt = departmentRepository.findById(promotionRequest.getDepartmentId());
		if(departmentOpt.isEmpty()) {
			throw new RuntimeException("ERROR: El departamento con el id "+promotionRequest.getDepartmentId()+" NO existe.");
		}
		Department department = departmentOpt.get();

		//2.-Obtendremos una referencia al título, salario y departamento actual del empleado. 
		//Si alguno de los tres no existe, el método termina dado que los datos de entrada no son correctos.
		
		Title actualTitle = titleRepository.getEmployeeActualTitle(employee);
		if(actualTitle==null) {
			throw new RuntimeException("ERROR: No se ha encontrado un puesto activo para el empleado con el id "+promotionRequest.getDepartmentId()+".");
		}
		
		DeptEmp actualDeptEmp = deptEmpRepository.getEmployeeActualDept(employee);
		if(actualDeptEmp==null) {
			throw new RuntimeException("ERROR: No se ha encontrado un departamento activo para el empleado con el id "+promotionRequest.getDepartmentId()+".");
		}
		
		Salary actualSalary = salaryRepository.getEmployeeActualSalary(employee);
		if(actualSalary==null) {
			throw new RuntimeException("ERROR: No se ha encontrado un salario activo para el empleado con el id "+promotionRequest.getDepartmentId()+".");
		}

		//3.-Actualizamos el atributo toDate del título, salario y departamento actuales con el 
		//valor recibido en el atributo fromDate de la entrada del método. Actualizamos dichos registros en la base de datos.
		actualTitle.setToDate(promotionRequest.getFromDate());
		titleRepository.save(actualTitle);
		
		actualDeptEmp.setToDate(promotionRequest.getFromDate());
		deptEmpRepository.save(actualDeptEmp);
		
		actualSalary.setToDate(promotionRequest.getFromDate());
		salaryRepository.save(actualSalary);
		
		//4.-Tras la actualización, incluiremos una validación, y es que el salario no puede modificarse
		//más de un 15% en una promoción. En el caso de que esto no se cumpla se lanzará una nueva excepción, usando throw new RuntimeException().
		if(promotionRequest.getSalary() > (actualSalary.getSalary()*1.15)) {
			throw new RuntimeException("ERROR: No se puede incrementar el salario más de un 15%.");
		}

		
		//5.-Creamos nuevos objetos que representen el nuevo título, salario y departamento del empleado. 
		//Usaremos para el atributo toDate el mismo valor que había previamente en los registros que modificamos.
		Title newTitle = new Title(employee, promotionRequest.getTitle(), promotionRequest.getFromDate(), formatter.parse(finalDate));
		Salary newSalary= new Salary(employee, promotionRequest.getSalary(), promotionRequest.getFromDate(), formatter.parse(finalDate));
		DeptEmp newDeptEmp= new DeptEmp(employee, department, promotionRequest.getFromDate(), formatter.parse(finalDate));
		
		//6.-Guardamos los nuevos objetos en la base de datos.
		titleRepository.save(newTitle);
		salaryRepository.save(newSalary);
		deptEmpRepository.save(newDeptEmp);
		
		return "Promocionado :D";
	}
}