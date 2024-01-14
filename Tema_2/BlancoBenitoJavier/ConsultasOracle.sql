-- Consulta 1: Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento
SELECT XMLELEMENT("empleado",
    XMLATTRIBUTES(employees.first_name AS "nombre",
                  employees.last_name AS "apellido",
                  departments.department_name AS "nombre_departamento")
       )
    AS "XML"
FROM employees, departments
WHERE employees.department_id = departments.department_id;

-- Consulta 2: Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers
SELECT XMLELEMENT("managers",
           XMLAGG(
               XMLELEMENT("manager",
                   XMLFOREST(
                       employees.FIRST_NAME AS "nombre",
                       employees.last_name AS "apellido",
                       employees.DEPARTMENT_ID AS "departamento",
                       locations.city AS "ciudad",
                       countries.COUNTRY_NAME AS "pais"
                   )
               )
           )
       ) AS "Resultado"
FROM employees,departments, locations, countries
WHERE employees.DEPARTMENT_ID = departments.DEPARTMENT_ID AND departments.LOCATION_ID = locations.LOCATION_ID
AND locations.COUNTRY_ID = countries.COUNTRY_ID