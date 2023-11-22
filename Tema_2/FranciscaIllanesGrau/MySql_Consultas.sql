SELECT gender, COUNT(*) AS total
FROM employees
GROUP BY gender
ORDER BY total DESC;


SELECT e.first_name, e.last_name, s.salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
WHERE e.emp_no IN (
    SELECT de.emp_no
    FROM dept_emp de
    WHERE de.dept_no = 'd005'
)
ORDER BY s.salary DESC
LIMIT 1;

SELECT e.first_name, e.last_name, s.salary
FROM employees e
JOIN salaries s ON e.emp_no = s.emp_no
WHERE e.emp_no IN (
    SELECT de.emp_no
    FROM dept_emp de
    WHERE de.dept_no = 'd005'
)
ORDER BY s.salary DESC
LIMIT 1 OFFSET 1;


SELECT COUNT(*) AS total_empleados
FROM employees
WHERE MONTH(hire_date) = 5;