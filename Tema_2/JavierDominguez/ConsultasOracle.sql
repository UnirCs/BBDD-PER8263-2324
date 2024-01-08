-- 1. Mostrar el nombre y apellido de un empleado
-- junto con el nombre de su departamento.
-- Consulta 01
SELECT
  XMLELEMENT("empleados",
    XMLATTRIBUTES(
      e.FIRST_NAME AS "nombre",
      e.LAST_NAME AS "apellidos",
      d.DEPARTMENT_NAME AS "departamento"))
  AS Empleados
FROM EMPLOYEES e
INNER JOIN DEPARTMENTS d USING (DEPARTMENT_ID);

-- 2. Mostrar el nombre, apellido, nombre de departamento,
-- ciudad y pais de los empleados que son Managers.
-- Consulta 02
SELECT XMLELEMENT("managers",
    XMLAGG(
      XMLELEMENT("manager",
        XMLELEMENT("nombreCompleto",
            XMLFOREST(E.FIRST_NAME AS "nombre",
                      E.LAST_NAME AS "apellido")),
        XMLFOREST(D.DEPARTMENT_NAME AS "department",
                  L.CITY AS "city",
                  C.COUNTRY_NAME AS "country"))
    )) AS Managers
FROM EMPLOYEES E
INNER JOIN HR.DEPARTMENTS D USING (DEPARTMENT_ID)
INNER JOIN HR.LOCATIONS L USING (LOCATION_ID)
INNER JOIN HR.COUNTRIES C USING (COUNTRY_ID)
WHERE E.EMPLOYEE_ID IN (
    SELECT M.MANAGER_ID
    FROM EMPLOYEES M
    GROUP BY M.MANAGER_ID);
