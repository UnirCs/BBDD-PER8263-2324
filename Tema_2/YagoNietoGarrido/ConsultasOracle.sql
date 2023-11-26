SELECT XMLELEMENT("empleados", XMLATTRIBUTES(E.first_name AS "nombre", E.last_name AS "apellidos", D.DEPARTMENT_NAME AS
                  "departamento")) AS empleado
from EMPLOYEES E
         INNER JOIN DEPARTMENTS D on D.DEPARTMENT_ID = E.DEPARTMENT_ID;


Select XMLELEMENT("managers",
                  XMLAGG(
                          XMLFOREST(
                                  XMLFOREST(
                                          XMLFOREST(
                                                  E.FIRST_NAME AS "nombre",
                                                  E.LAST_NAME AS "apellido"
                                          ) AS "nombreCompleto",
                                          DEPARTMENT_NAME AS "department",
                                          CITY AS "city",
                                          COUNTRY_NAME AS "country"
                                  ) AS "manager"
                          )
                  )
       )
from ((((EMPLOYEES E inner join HR.JOBS J on J.JOB_ID = E.JOB_ID) INNER JOIN DEPARTMENTS D
        on E.DEPARTMENT_ID = D.DEPARTMENT_ID) INNER JOIN LOCATIONS L
       on L.LOCATION_ID = D.LOCATION_ID) INNER JOIN COUNTRIES C ON L.COUNTRY_ID = C.COUNTRY_ID)
Where J.JOB_TITLE like '%Manager%';