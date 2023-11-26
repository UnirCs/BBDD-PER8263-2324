--Pregunta 1

SELECT
    XMLELEMENT("empleados",
               XMLATTRIBUTES (
               FIRST_NAME AS "nombre",
               LAST_NAME AS "apellidos",
               DEPARTMENT_NAME AS "departamento"
        )
    ) AS empleados
FROM EMPLOYEES, DEPARTMENTS
WHERE EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID
