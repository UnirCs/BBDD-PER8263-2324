-- Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente
select gender as Sexo, count(*) as Cantidad from employees.employees group by gender order by cantidad desc;

-- Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
Select e.first_name,e.last_name,s.salary from employees.employees e
    inner join employees.salaries s on e.emp_no = s.emp_no
    inner join employees.dept_emp de on e.emp_no = de.emp_no
    inner join employees.departments d on de.dept_no = d.dept_no
where d.dept_name = 'Marketing'
order by s.salary desc Limit 1;

-- Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
Select e.first_name,e.last_name,s.salary from employees.employees e
    inner join employees.salaries s on e.emp_no = s.emp_no
    inner join employees.dept_emp de on e.emp_no = de.emp_no
    inner join employees.departments d on de.dept_no = d.dept_no
where d.dept_name = 'Marketing'
order by s.salary desc Limit 1 offset 1;

-- Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
select count(*) as Contrataciones from employees.employees where Month(hire_date) = 7
