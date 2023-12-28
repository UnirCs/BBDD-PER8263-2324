SELECT gender, COUNT(*) AS count
                FROM employees.employees
                GROUP BY gender
                ORDER BY count DESC;

SELECT e.first_name, e.last_name, s.salary
                FROM employees.employees e
                JOIN employees.salaries s ON e.emp_no = s.emp_no
                JOIN employees.dept_emp de ON e.emp_no = de.emp_no
                WHERE de.dept_no = 'd008'
                ORDER BY s.salary DESC
                LIMIT 1;

SELECT e.first_name, e.last_name, s.salary
                FROM employees.employees e
                JOIN employees.salaries s ON e.emp_no = s.emp_no
                JOIN employees.dept_emp de ON e.emp_no = de.emp_no
                WHERE de.dept_no = 'd008'
                ORDER BY s.salary DESC
                LIMIT 1, 1;

SELECT COUNT(*) AS count
                FROM employees.employees
                WHERE MONTH(hire_date) = '11';