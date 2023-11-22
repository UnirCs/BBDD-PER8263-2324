/*
Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
*/
SELECT e.gender, COUNT(*) AS count
                FROM employees e
                GROUP BY e.gender
                ORDER BY count DESC;

/*
Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
*/
SELECT e.first_name, e.last_name, s.salary FROM salaries s
JOIN employees e on e.emp_no = s.emp_no
JOIN dept_emp de on e.emp_no = de.emp_no
where de.dept_no =  'd005'
order by s.salary DESC
LIMIT 1;

/*
Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
*/
SELECT e.first_name, e.last_name, s.salary FROM salaries s
JOIN employees e on e.emp_no = s.emp_no
JOIN dept_emp de on e.emp_no = de.emp_no
where de.dept_no =  'd005'
order by s.salary DESC
LIMIT 1 OFFSET 1;

/*
Mostrar el número de empleados contratados en un mes concreto (parámetro variable). 
*/
SELECT count(*) AS total_contract FROM employees e
where month(e.hire_date)  =  11;