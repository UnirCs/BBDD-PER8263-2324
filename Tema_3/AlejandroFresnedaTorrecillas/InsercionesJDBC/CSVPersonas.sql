SELECT MAX(emp_no) + 1 AS new_emp_no FROM employees;
INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES (?, ?, ?, ?, ?, ?);