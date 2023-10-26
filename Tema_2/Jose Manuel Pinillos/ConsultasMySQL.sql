SELECT employees.gender, count(*)
FROM employees.employees
GROUP BY employees.gender

SELECT first_name as Nombre, last_name as Apellido, salary as Salario
FROM employees, salaries
WHERE employees.emp_no = salaries.emp_no
ORDER BY salary DESC
LIMIT 1

SELECT first_name as Nombre, last_name as Apellido, salary as Salario
FROM employees, salaries
WHERE employees.emp_no = salaries.emp_no
ORDER BY salary DESC
LIMIT 1,1

SELECT count(emp_no) as Empleados_contratados, MONTHNAME(hire_date) as Mes
FROM employees
where hire_date = date(19850101)