
-- 1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
SELECT COUNT(gender) AS genders_num, gender
FROM employees
GROUP BY gender
ORDER BY genders_num DESC;


-- 2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
SET @department := 'Marketing';

SELECT employees.first_name, employees.last_name, salaries.salary
FROM salaries
         JOIN employees ON salaries.emp_no = employees.emp_no
where salaries.emp_no IN (SELECT employees.emp_no
                          FROM employees
                                   JOIN dept_emp ON employees.emp_no = dept_emp.emp_no
                                   JOIN departments ON departments.dept_no = dept_emp.dept_no
                          WHERE departments.dept_name = @department)
ORDER BY salaries.salary DESC
LIMIT 1;


-- 3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
SET @department := 'Development';

SELECT employees.first_name, employees.last_name, salaries.salary
FROM salaries
         JOIN employees ON salaries.emp_no = employees.emp_no
where salaries.emp_no IN (SELECT employees.emp_no
                          FROM employees
                                   JOIN dept_emp ON employees.emp_no = dept_emp.emp_no
                                   JOIN departments ON departments.dept_no = dept_emp.dept_no
                          WHERE departments.dept_name = @department)
ORDER BY salaries.salary DESC
LIMIT 1 OFFSET 1;


-- 4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
SET @month := 11;
SELECT COUNT(*) AS numOfHiredEmployees
FROM employees
WHERE MONTH(employees.hire_date) = @month;