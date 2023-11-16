/* Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente. */
Select COUNT(emp_no) as numero,
       gender as sexo
    from employees.employees
    group by gender
    order by numero desc;

/* Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable). */
Select e.first_name as nombre, e.last_name as apellidos, s.salary salario
from employees.employees e, employees.dept_emp de, employees.departments d, employees.salaries s
where
e.emp_no = s.emp_no
and e.emp_no = de.emp_no
and de.dept_no = d.dept_no
and d.dept_name in ('Customer Service')
ORDER BY salario DESC
LIMIT 1;

/* Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable). */
Select e.first_name as nombre, e.last_name as apellidos, s.salary salario
from employees.employees e, employees.dept_emp de, employees.departments d, employees.salaries s
where
e.emp_no = s.emp_no
and e.emp_no = de.emp_no
and de.dept_no = d.dept_no
and d.dept_name in ('Customer Service')
ORDER BY s.salary DESC
LIMIT 1 OFFSET 1;

/* Mostrar el número de empleados contratados en un mes concreto (parámetro variable). */
Select COUNT(*) as numContratadosMes
    from employees.employees
    where month(hire_date) = 6;