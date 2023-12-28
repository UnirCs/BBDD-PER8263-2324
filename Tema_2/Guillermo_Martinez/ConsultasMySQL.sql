#Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
SELECT gender, COUNT(*) AS total
FROM employees
GROUP BY gender
ORDER BY total DESC;

#Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable)
SELECT e.first_name, e.last_name, s.salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp de ON e.emp_no = de.emp_no
WHERE de.dept_no = ?
      AND s.to_date = '9999-01-01'
ORDER BY s.salary DESC
LIMIT 1;

#Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
SELECT e.first_name, e.last_name, s.salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp de ON e.emp_no = de.emp_no
WHERE de.dept_no = ?
      AND s.to_date = '9999-01-01'
ORDER BY s.salary DESC
LIMIT 1 OFFSET 1;

#Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
SELECT COUNT(*) AS totalEmployees
FROM employees
WHERE MONTH(hire_date) = ?;
