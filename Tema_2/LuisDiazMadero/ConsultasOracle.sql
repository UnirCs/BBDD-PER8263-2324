-- 1. Mostrar el nombre y apellidos de un empleado junto con el nombre de su departamento.

SELECT XMLELEMENT("empleados",
                  XMLATTRIBUTES(e.first_name AS "nombre",
                  e.last_name AS "apellidos",
                  d.department_name AS "departamento"))
FROM employees e
         JOIN departments d ON e.department_id = d.department_id;

-- 2. Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son manager
SELECT XMLELEMENT("managers",
    XMLAGG(
          XMLELEMENT("manager",
             XMLELEMENT("nombreCompleto",
                XMLFOREST(
                        e.first_name AS "nombre",
                        e.last_name AS "apellido"
                )
             ),
             XMLELEMENT("department", d.department_name),
             XMLELEMENT("city", l.city),
             XMLELEMENT("country", c.COUNTRY_NAME)
            )
    )
) AS managers
FROM employees e
         JOIN departments d ON e.department_id = d.department_id
         JOIN locations l ON d.location_id = l.location_id
         JOIN COUNTRIES c ON l.COUNTRY_ID = c.COUNTRY_ID
WHERE e.employee_id IN (SELECT DISTINCT manager_id FROM employees);

