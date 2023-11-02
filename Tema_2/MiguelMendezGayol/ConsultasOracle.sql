/*Mostrar el nombre y apellido de un empleado
  junto con el nombre de su departamento
 */

SELECT
    XMLELEMENT("empleados",
               XMLATTRIBUTES(e.first_name AS "nombre",
               e.last_name AS "apellidos",
               d.department_name AS "departamento"))
        AS empleados
FROM   employees e
JOIN departments d ON e.department_id=d.department_id;

/*
Mostrar el nombre, apellido, nombre de departamento,
  ciudad y pais de los empleados que son Managers
 */

SELECT
    XMLELEMENT("managers",
               XMLAGG(
                       XMLELEMENT("manager",
                                  XMLELEMENT("nombreCompleto",
                                             XMLELEMENT("nombre", e.first_name),
                                             XMLELEMENT("apellido", e.last_name)
                                  ),
                                  XMLELEMENT("department", d.department_name),
                                  XMLELEMENT("city", l.city ),
                                  XMLELEMENT("country",c.country_name )
                       )
               )
    )
  as  managers
  from employees e
  JOIN departments d ON e.department_id=d.department_id
  JOIN locations l ON d.location_id = l.location_id
  JOIN countries c ON l.country_id = c.country_id
  JOIN jobs j ON e.JOB_ID=j.JOB_ID
  where j.JOB_TITLE LIKE '%Manager';
