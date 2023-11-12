/* Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento. */

select XMLELEMENT ("empleados",
        XMLATTRIBUTES(
           e.FIRST_NAME as "nombre",
           e.LAST_NAME as "apellidos",
           d.DEPARTMENT_NAME as "departamento" )
       ) as empleados
from employees e , DEPARTMENTS d
    where e.DEPARTMENT_ID = d.DEPARTMENT_ID;

/* Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers. */

SELECT XMLELEMENT ("managers",
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
from EMPLOYEES e, JOBS j, DEPARTMENTS d, LOCATIONS l, COUNTRIES c
    where j.JOB_ID = e.JOB_ID
    and e.DEPARTMENT_ID = e.DEPARTMENT_ID
    and d.LOCATION_ID = l.LOCATION_ID
    and l.COUNTRY_ID = c.COUNTRY_ID
    and j.JOB_TITLE like '%Manager%';

/*Select * from  JOBS where JOB_TITLE like '%Manager%';*/