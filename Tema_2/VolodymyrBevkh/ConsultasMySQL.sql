-- 1 Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.

SELECT gender,
    COUNT(gender)
FROM employees
GROUP BY gender
ORDER BY COUNT(gender) DESC;


-- 2 Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).

SELECT first_name AS nombre, last_name AS apellido, salary AS salario
FROM employees, salaries, dept_emp
WHERE employees.emp_no = salaries.emp_no AND employees.emp_no = dept_emp.emp_no AND dept_no = 'd002' -- Se puede cambiar el numero de departamento por el que sea
ORDER BY salario DESC
LIMIT 1;


-- 3 Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).

SELECT first_name AS nombre, last_name AS apellido, salary AS salario
FROM employees, salaries, dept_emp
WHERE employees.emp_no = salaries.emp_no AND employees.emp_no = dept_emp.emp_no AND dept_no = 'd002' -- Se puede cambiar el numero de departamento por el que sea
ORDER BY salario DESC
LIMIT 1,1;

-- 4 Mostrar el número de empleados contratados en un mes concreto (parámetro variable).

SELECT COUNT(*) AS contratados
FROM employees
WHERE Month(hire_date) = 2;