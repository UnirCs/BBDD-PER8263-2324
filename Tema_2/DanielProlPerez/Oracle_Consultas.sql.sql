SELECT XMLELEMENT(
         "empleados",
         XMLATTRIBUTES(
             e.FIRST_NAME AS "nombre",
             e.LAST_NAME AS "apellidos",
             d.DEPARTMENT_NAME AS "departamento"
         )
       ) AS "EmpleadoXML"
FROM EMPLOYEES e
JOIN DEPARTMENTS d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID;


SELECT
    XMLELEMENT("managers",
        XMLAGG(
            XMLELEMENT("manager",
                XMLELEMENT("nombreCompleto",
                    XMLELEMENT("nombre", e.first_name),
                    XMLELEMENT("apellido", e.last_name)
                ),
                XMLELEMENT("department", d.department_name),
                XMLELEMENT("city", l.city),
                XMLELEMENT("country", c.country_name)
            )
        )
    ) AS ManagersXml
FROM employees e
JOIN departments d ON e.employee_id = d.manager_id
JOIN locations l ON d.location_id = l.location_id
JOIN countries c ON l.country_id = c.country_id
WHERE e.job_id LIKE '%MAN%';