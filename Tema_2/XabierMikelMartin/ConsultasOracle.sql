--  Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
--  Cada resultado XML devuelto por la consulta (la consulta debe devolver 1 registro por empleado)
SELECT XMLELEMENT("empleados",
           XMLATTRIBUTES (e.FIRST_NAME as "nombre",
                        e.LAST_NAME as "apellidos",
                        d.DEPARTMENT_NAME as "departamento")) as empleados
FROM EMPLOYEES e inner join DEPARTMENTS d on e.DEPARTMENT_ID = d.DEPARTMENT_ID;


-- Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers.
-- El XML devuelto por la consulta (debe devolver un único registro, con todos los managers):
SELECT XMLELEMENT("managers",
    XMLAGG(
        XMLELEMENT("manager",
            XMLELEMENT("nombreCompleto",
                XMLFOREST(e.FIRST_NAME as "nombre", e.LAST_NAME as "apellido")
            ),
            XMLELEMENT("department", d.DEPARTMENT_NAME),
            XMLELEMENT("city", l.CITY),
            XMLELEMENT("country", c.COUNTRY_NAME)
        )
    )
)AS managers
FROM EMPLOYEES e
INNER JOIN DEPARTMENTS d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID
INNER JOIN LOCATIONS l ON d.LOCATION_ID = l.LOCATION_ID
INNER JOIN COUNTRIES c ON l.COUNTRY_ID = c.COUNTRY_ID
WHERE e.EMPLOYEE_ID IN (SELECT DISTINCT manager_id FROM employees);


