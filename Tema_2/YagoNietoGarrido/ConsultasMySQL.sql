USE employees;

/*
Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
*/
SELECT gender, count(*) as 'cantidad'
from employees
GROUP BY gender
ORDER BY cantidad DESC;

/*
Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
*/
SELECT CONCAT(first_name, ' ', last_name) as 'Name', MAX(salary) as 'MaxSalary'
from ((employees INNER JOIN salaries ON salaries.emp_no = employees.emp_no)
    INNER JOIN dept_emp ON employees.emp_no = dept_emp.emp_no)
WHERE dept_emp.dept_no = 'd001'
group by Name
ORDER BY MaxSalary DESC
LIMIT 1;

/*
Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
*/
SELECT CONCAT(first_name, ' ', last_name) as 'Name', MAX(salary) as 'MaxSalary'
from ((employees INNER JOIN salaries ON salaries.emp_no = employees.emp_no)
    INNER JOIN dept_emp ON employees.emp_no = dept_emp.emp_no)
WHERE dept_emp.dept_no = 'd001'
group by Name
ORDER BY MaxSalary DESC
LIMIT 1 OFFSET 1;

/*
Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
*/
SELECT count(*)
from employees
Where Month(employees.hire_date) = 1;