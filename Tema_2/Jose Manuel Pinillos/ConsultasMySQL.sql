/* 1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente. */
SELECT gender as Genero, count(*) as Total
FROM employees
GROUP BY gender
ORDER BY Total desc

/* 2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable). */
SELECT first_name as Nombre, last_name as Apellido, salary as Salario
FROM employees, salaries, departments, dept_emp
WHERE employees.emp_no = salaries.emp_no and
      employees.emp_no = dept_emp.emp_no and
      departments.dept_no = dept_emp.dept_no and
      dept_emp.dept_no = 'd005'
ORDER BY salary DESC
LIMIT 1

/* 3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable). */
SELECT first_name as Nombre, last_name as Apellido, salary as Salario, dept_name as Departamento, dept_emp.dept_no
FROM employees, salaries, departments, dept_emp
WHERE employees.emp_no = salaries.emp_no and
      employees.emp_no = dept_emp.emp_no and
      departments.dept_no = dept_emp.dept_no and
      dept_emp.dept_no = 'd005'
ORDER BY salary DESC
LIMIT 1,1

/* 4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable). */
SELECT count(*) as Empleados_contratados
FROM employees
WHERE month(hire_date) = 3




