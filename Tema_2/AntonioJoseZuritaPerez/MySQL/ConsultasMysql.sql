-- 1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
SELECT gender, COUNT(*) as Total
FROM employees
GROUP BY gender
ORDER BY Total DESC;

-- 2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
SELECT e.first_name, e.last_name, s.salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp de ON e.emp_no = de.emp_no
WHERE de.dept_no  = ? -- parámetro variable
AND s.from_date <= current_date  -- Aseguran que se estén considerando solo los salarios y asignaciones actuales
AND s.to_date >= current_date
AND de.from_date <= current_date
AND de.to_date >= current_date
ORDER BY s.salary DESC
LIMIT 1;

-- 3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
SELECT e.first_name, e.last_name, s.salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp de ON e.emp_no = de.emp_no
WHERE de.dept_no  = ? -- parámetro variable
AND s.from_date <= current_date  -- Aseguran que se estén considerando solo los salarios y asignaciones actuales
AND s.to_date >= current_date
AND de.from_date <= current_date
AND de.to_date >= current_date
ORDER BY s.salary DESC
LIMIT 1 OFFSET 1; -- Selecciona el segundo registro después de ordenar los resultados por salario de forma descendente. OFFSET 1 omite el primer regfistro

-- 4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
SELECT COUNT(*) as Total
FROM employees
WHERE MONTH(hire_date) = ? -- parámetro variable 
AND YEAR(hire_date) = ?; -- parámetro variable
