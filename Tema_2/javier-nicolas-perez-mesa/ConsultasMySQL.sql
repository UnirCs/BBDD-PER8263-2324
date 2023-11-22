-- NUMERO DE HOMBRES Y MUJERES
-- Agrupar todos los empleados por género y contar cuántos hay de cada uno. Mostrar el género y el total de empleados por género. Ordenar por el total de empleados de forma descendente.
SELECT employees.gender AS genero, COUNT(*) AS total FROM employees.employees GROUP BY gender ORDER BY total DESC;

-- EMPLEADO MEJOR PAGADO.
SET @nombre_departamento = 'Marketing';

-- Obtenemos los del empleado mejor pagado del departamento
SELECT
    employees.first_name AS nombre,
    employees.last_name AS apellidos,
    salaries.salary AS salario
FROM
    employees.employees
INNER
    JOIN employees.salaries
        ON
            employees.emp_no = salaries.emp_no
WHERE (
    -- solo los empleados que aun sigan cobrando
    salaries.to_date = '9999-01-01'
        AND
            -- Solo empleados del departamento elegido
            employees.emp_no IN (
                SELECT
                    dept_emp.emp_no
                FROM
                    employees.dept_emp
                WHERE
                    dept_no = (
                        SELECT
                            dept_no
                        FROM
                            employees.departments
                        WHERE
                            dept_name = @nombre_departamento
                    )
            )
    )
ORDER BY
    salaries.salary DESC
LIMIT 1;


-- SEGUNDO EMPLEADO MEJOR PAGADO
-- Obtenemos los del empleado mejor pagado del departamento
SELECT
    employees.first_name AS nombre,
    employees.last_name AS apellidos,
    salaries.salary AS salario
FROM
    employees.employees
INNER
    JOIN employees.salaries
        ON
            employees.emp_no = salaries.emp_no
WHERE (
    -- solo los empleados que aun sigan cobrando
    salaries.to_date = '9999-01-01'
        AND
            -- Solo empleados del departamento elegido
            employees.emp_no IN (
                SELECT
                    dept_emp.emp_no
                FROM
                    employees.dept_emp
                WHERE
                    dept_no = (
                        SELECT
                            dept_no
                        FROM
                            employees.departments
                        WHERE
                            dept_name = @nombre_departamento
                    )
            )
    )
ORDER BY
    salaries.salary DESC
LIMIT 1, 1;

-- VER LOS EMPLEADOS CONTRATADOS EN UN MES Y UN AÑO CONCRETOS
SET @mes = 1;
SET @anio = 1999;

SELECT
    employees.first_name AS nombre,
    employees.last_name AS apellidos,
    employees.hire_date AS fecha_contratacion
FROM
    employees.employees
WHERE
    MONTH(employees.hire_date) = @mes
        AND
            YEAR(employees.hire_date) = @anio
ORDER BY
    employees.hire_date DESC;