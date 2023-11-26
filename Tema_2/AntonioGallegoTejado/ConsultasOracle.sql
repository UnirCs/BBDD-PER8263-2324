--Nombre y apellidos más departamento de los empleados del departamento de Ventas
select XMLELEMENT("empleados",
                  XMLATTRIBUTES(
           empleados.FIRST_NAME AS "nombre",
           empleados.LAST_NAME AS "apellidos",
           departamento.DEPARTMENT_NAME AS "departamento")
       ) as EMPLEADO
from   EMPLOYEES empleados
    inner join DEPARTMENTS departamento on empleados.DEPARTMENT_ID = departamento.DEPARTMENT_ID
where departamento.DEPARTMENT_ID = 80;

--Nombre y apellidos, nombre del depto y ciudad y país de empleados Manager
SELECT XMLELEMENT("managers",
    XMLAGG(
        XMLELEMENT("manager",
            XMLELEMENT("nombreCompleto",
                XMLFOREST(
                    empleados.FIRST_NAME as "nombre",
                    empleados.LAST_NAME as "apellido"
                )),
            XMLELEMENT("department",departamento.DEPARTMENT_NAME),
            XMLELEMENT("city",location.CITY),
            XMLELEMENT("country",pais.COUNTRY_NAME)
        )
    )
    ) AS MANAGER
from EMPLOYEES empleados
    inner join DEPARTMENTS departamento on empleados.DEPARTMENT_ID = empleados.DEPARTMENT_ID
    inner join JOBS jobs on jobs.JOB_ID = empleados.JOB_ID
    inner join LOCATIONS location on departamento.LOCATION_ID = location.LOCATION_ID
    inner join COUNTRIES pais on location.COUNTRY_ID = pais.COUNTRY_ID
where jobs.JOB_TITLE like '%Manager%';