-- Número de hombre y mujeres
select count(*) as total, gender from employees group by gender order by total desc;

-- Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto
select first_name, last_name, salary from employees
    inner join dept_emp on employees.emp_no = dept_emp.emp_no
    inner join departments on dept_emp.dept_no = departments.dept_no and dept_emp.emp_no = employees.emp_no
    inner join salaries on employees.emp_no = salaries.emp_no
where dept_name = 'Development'
order by salary desc
limit 1;




-- Mostrar el nombre, apellido y salario de la segunda mejor pagada de un departamento concreo
select first_name, last_name, salary from employees
    inner join dept_emp on employees.emp_no = dept_emp.emp_no
    inner join departments on dept_emp.dept_no = departments.dept_no and dept_emp.emp_no = employees.emp_no
    inner join salaries on employees.emp_no = salaries.emp_no
where dept_name = 'Development'
order by salary desc
limit 1,1;

-- El limit 1,1 => salta el primer registro con el primer uno y con el segundo muestra el segundo registro.


-- Mostrar el número de empleados contratados en un mes concreto
select count(*) as numHireEmployees from employees where month(employees.hire_date) = 10;