--Pregunta 1 

SELECT gender, COUNT(*) AS cantidad
FROM employees.employees
GROUP BY gender
ORDER BY cantidad DESC;


--Pregunta 2

SELECT e.first_name, e.last_name, s.salary, d.dept_no
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp de ON e.emp_no = de.emp_no
JOIN departments d ON de.dept_no = d.dept_no
WHERE d.dept_no = 'd005'
ORDER BY s.salary DESC
LIMIT 1;

--Pregunta 3

SELECT e.first_name, e.last_name, s.salary, d.dept_name
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
JOIN dept_emp de ON e.emp_no = de.emp_no
JOIN departments d ON de.dept_no = d.dept_no
WHERE d.dept_no = 'd005'
ORDER BY s.salary DESC
LIMIT 1 OFFSET 1;

--Pregunga 4

SELECT COUNT(*) AS numero_empleados_contratados
FROM employees
WHERE MONTH(hire_date) = 12;