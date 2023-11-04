select XMLELEMENT("empleados",
                  XMLATTRIBUTES(e.FIRST_NAME AS "nombre", e.LAST_NAME as "apellidos", d.DEPARTMENT_NAME as
                  "departamento")
       ) as empleados from EMPLOYEES e INNER JOIN DEPARTMENTS d on e.DEPARTMENT_ID = d.DEPARTMENT_ID;


select XMLELEMENT("managers",
        XMLAGG(
            XMLELEMENT("manager",
                XMLELEMENT("nombreCompleto",
                        xmlelement("nombre", e.FIRST_NAME),
                        xmlelement("apellido", e.LAST_NAME)
                ),
                XMLELEMENT("department", d.DEPARTMENT_NAME),
                XMLELEMENT("city",ed.CITY),
                xmlelement("country", ed.COUNTRY_NAME)
                )
        )
       ) as managers from EMPLOYEES e INNER JOIN DEPARTMENTS d on e.DEPARTMENT_ID = d.DEPARTMENT_ID
inner join EMP_DETAILS_VIEW ed on ed.EMPLOYEE_ID=e.EMPLOYEE_ID;
