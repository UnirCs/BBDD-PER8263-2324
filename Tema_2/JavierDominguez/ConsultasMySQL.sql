-- Tema 2. Consultas sobre MySQL
USE employees;

-- 1. Obtener el número de hombres y mujeres de la base de datos.
--    Ordenar de forma descendente.
-- Consulta 01
SELECT e.gender, COUNT(e.gender) count
FROM employees e
GROUP BY e.gender
ORDER BY count DESC;

-- 2. Mostrar el nombre, apellido y salario de la persona mejor pagada
--    de un departamento concreto.
-- Consulta 02
SELECT e.first_name, e.last_name, s.salary max_salary
FROM employees e
INNER JOIN salaries s ON e.emp_no = s.emp_no
INNER JOIN dept_emp dr ON e.emp_no = dr.emp_no
WHERE dr.dept_no = 'd007'
ORDER BY max_salary DESC
LIMIT 1;

-- 3. Mostrar el nombre, apellido y salario de la segunda persona
--    mejor pagada de un departamento concreto.
-- Consulta 03
SELECT e.first_name, e.last_name, MAX(s.salary) max_salary
FROM employees e
INNER JOIN salaries s ON e.emp_no = s.emp_no
INNER JOIN dept_emp dr ON e.emp_no = dr.emp_no
WHERE dr.dept_no = 'd007'
GROUP BY e.emp_no
ORDER BY max_salary DESC
LIMIT 1 OFFSET 1;

-- 4. Mostrar el número de empleados contratados en un mes concreto
-- Consulta 04
SELECT MONTH(e.hire_date) month, COUNT(e.emp_no)
FROM employees e
WHERE MONTH(e.hire_date)=11
GROUP BY month;