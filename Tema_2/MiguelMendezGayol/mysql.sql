/*Obtener el número de hombres
  y mujeres de la base de datos.
  Ordenar de forma descendente.
 */

Select count(*)
 from employees.employees
 where gender='M';

Select count(*)
from employees.employees
where gender='F';

select first_name,last_name
from employees.employees
order by last_name;

/*
 Mostrar el nombre, apellido y salario
 de la persona mejor pagada de un
 departamento concreto (parámetro variable).
 */

 select  first_name,last_name,salary
 from  employees.employees
 INNER JOIN employees.salaries ON employees.emp_no = salaries.emp_no
 INNER  JOIN employees.dept_emp ON salaries.emp_no=dept_emp.emp_no
 where employees.dept_emp.dept_no='d009'
 order by salary desc
 LIMIT 1;

/*
 Mostrar el nombre, apellido y salario
 de la persona mejor pagada de un
 departamento concreto (parámetro variable).
 */
select first_name,last_name,salary
 from  employees.employees
 INNER JOIN employees.salaries ON employees.emp_no = salaries.emp_no
 INNER  JOIN employees.dept_emp ON salaries.emp_no=dept_emp.emp_no
 where employees.dept_emp.dept_no='d009'
 order by salary desc
 LIMIT 1 OFFSET 1;

/*Mostrar el número de empleados
 contratados en un mes concreto*/

 select first_name,last_name,hire_date
 from  employees.employees
 where hire_date BETWEEN '1989-10-01' AND '1989-10-31';
