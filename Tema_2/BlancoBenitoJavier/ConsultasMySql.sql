-- Consulta 1: Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente. 
SELECT gender as genero, count(gender) as numero_total
FROM employees
GROUP BY genero
ORDER BY numero_total desc;

-- Consulta 2: Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).*/
SELECT first_name as nombre, last_name as apellido, salary as salario
FROM employees, salaries, dept_emp
WHERE employees.emp_no = salaries.emp_no AND employees.emp_no = dept_emp.emp_no AND dept_no = 'd009' -- Se puede cambiar el numero de departamento por el que sea
ORDER BY salario desc
LIMIT 1;

-- Consulta 3: Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).*/
SELECT first_name as nombre, last_name as apellido, salary as salario
FROM employees, salaries, dept_emp
WHERE employees.emp_no = salaries.emp_no AND employees.emp_no = dept_emp.emp_no AND dept_no = 'd009' -- Se puede cambiar el numero de departamento por el que sea
ORDER BY salario desc
LIMIT 1, 1; -- Con limit 1,1 omito el primer resultado (que es el mas pagado del departamento d009)

-- Consulta 4: Mostrar el número de empleados contratados en un mes concreto (parámetro variable).
SELECT COUNT(*) as numero
FROM employees
WHERE Month(hire_date) = 6; -- La funcion Month extrae el mes dada una fecha ()