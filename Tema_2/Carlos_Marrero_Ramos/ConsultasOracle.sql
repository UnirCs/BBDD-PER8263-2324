SELECT XMLELEMENT("empleados",
    XMLATTRIBUTES(  em.FIRST_NAME AS "nombre", 
                    em.LAST_NAME AS "apellidos", 
                    de.DEPARTMENT_NAME AS "departamento")
) AS EmpleadosXML
FROM EMPLOYEES em
JOIN DEPARTMENTS de ON em.DEPARTMENT_ID = de.DEPARTMENT_ID 




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
