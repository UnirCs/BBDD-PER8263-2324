--Número de empleados por sexo
select gender, count(*) as total from employees group by gender order by total desc;

--Empleado con el sueldo más alto de un determinado departamento
select first_name, last_name, salary from employees
inner join salaries on employees.emp_no = salaries.emp_no
inner join dept_emp on employees.emp_no = dept_emp.emp_no
inner join departments on dept_emp.dept_no = departments.dept_no
where dept_name = 'Marketing'
order by salaries.salary desc
limit 1;

--Segundo empleado con el sueldo más alto de un determinado departamento
select first_name, last_name, salary from employees
inner join salaries on employees.emp_no = salaries.emp_no
inner join dept_emp on employees.emp_no = dept_emp.emp_no
inner join departments on dept_emp.dept_no = departments.dept_no
where dept_name = 'Marketing'
order by salaries.salary desc
limit 1 offset 1;

--Número de contratados en un mes en concreto
select count(*) as num_contratos from employees where month(hire_date)=8;