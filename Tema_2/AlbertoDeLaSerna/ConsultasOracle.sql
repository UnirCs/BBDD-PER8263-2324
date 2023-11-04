-- Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
select XMLELEMENT("empleados",
             XMLATTRIBUTES(
           e.FIRST_NAME AS "nombre",
           e.LAST_NAME AS "apellidos",
           d.DEPARTMENT_NAME AS "departamento")
       ) as employee
from   EMPLOYEES e
    inner join DEPARTMENTS d on e.DEPARTMENT_ID = d.DEPARTMENT_ID
where d.DEPARTMENT_ID = 60;

-- Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers
SELECT XMLELEMENT("managers",
    XMLAGG(
        XMLELEMENT("manager",
            XMLELEMENT("nombreCompleto",
                XMLFOREST(
                    e.FIRST_NAME as "nombre",
                    e.LAST_NAME as "apellido"
                )),
            XMLELEMENT("department",d.DEPARTMENT_NAME),
            XMLELEMENT("city",l.CITY),
            XMLELEMENT("country",c.COUNTRY_NAME)
        )
    )
    ) AS managers
from EMPLOYEES e
    inner join JOBS j on j.JOB_ID = e.JOB_ID
    inner join DEPARTMENTS d on e.DEPARTMENT_ID = e.DEPARTMENT_ID
    inner join LOCATIONS l on d.LOCATION_ID = l.LOCATION_ID
    inner join COUNTRIES c on l.COUNTRY_ID = c.COUNTRY_ID
where j.JOB_TITLE like '%Manager%';

-- Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers
SELECT XMLELEMENT("managers",
    XMLAGG(
        XMLELEMENT("manager",
            XMLELEMENT("nombreCompleto",
                XMLELEMENT("nombre",e.FIRST_NAME),
                XMLELEMENT("apellido",e.LAST_NAME)
            ),
            XMLELEMENT("department",d.DEPARTMENT_NAME),
            XMLELEMENT("city",l.CITY),
            XMLELEMENT("country",c.COUNTRY_NAME)
        )
    )
    ) AS managers
from EMPLOYEES e
    inner join JOBS j on j.JOB_ID = e.JOB_ID
    inner join DEPARTMENTS d on e.DEPARTMENT_ID = e.DEPARTMENT_ID
    inner join LOCATIONS l on d.LOCATION_ID = l.LOCATION_ID
    inner join COUNTRIES c on l.COUNTRY_ID = c.COUNTRY_ID
where j.JOB_TITLE like '%Manager%';
