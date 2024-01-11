# 1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
SELECT gender, COUNT(*) as cantidad
FROM employees
GROUP BY gender
ORDER BY cantidad DESC;

# 2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
SELECT first_name, last_name, salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp d ON e.emp_no = d.emp_no
WHERE d.dept_no = 'd005'
ORDER BY salary DESC
LIMIT 1;

# 3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
SELECT first_name, last_name, salary
FROM employees e
         JOIN salaries s ON e.emp_no = s.emp_no
         JOIN dept_emp d ON e.emp_no = d.emp_no
WHERE d.dept_no = 'd005'
ORDER BY salary DESC
LIMIT 1 OFFSET 1;

# 4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
SELECT COUNT(*) as numero_empleados_contratados
FROM employees
WHERE MONTH(hire_date) = ?;


