SELECT gender AS Genero, COUNT(gender) AS Cantidad 
FROM employees 
GROUP BY gender 
ORDER BY Cantidad DESC;

-- Customer Service
-- Development
-- Finance
-- Human Resources
-- Marketing
-- Production
-- Quality Management
-- Research
-- Sales

SET @departamento := 'Sales';

SELECT em.first_name, em.last_name, sa.salary 
FROM salaries as sa
JOIN employees as em ON sa.emp_no = em.emp_no 
where sa.emp_no IN (
	SELECT e.emp_no
	FROM employees as e
	JOIN dept_emp as de ON e.emp_no = de.emp_no
	JOIN departments as d ON d.dept_no = de.dept_no
	WHERE d.dept_name = @departamento)
ORDER BY sa.salary DESC
LIMIT 1;

SET @departamento := 'Sales';

SELECT em.first_name, em.last_name, sa.salary 
FROM salaries as sa
JOIN employees as em ON sa.emp_no = em.emp_no 
where sa.emp_no IN (
	SELECT e.emp_no
	FROM employees as e
	JOIN dept_emp as de ON e.emp_no = de.emp_no
	JOIN departments as d ON d.dept_no = de.dept_no
	WHERE d.dept_name = @departamento)
ORDER BY sa.salary DESC
LIMIT 1, 1;


SET @mes = 12

SELECT COUNT(*) as Cantidad
FROM employees as e
WHERE DATE_FORMAT(hire_date, '%m') = @mes

