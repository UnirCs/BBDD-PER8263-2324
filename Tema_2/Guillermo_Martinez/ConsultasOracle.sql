/*1*/
SELECT XMLELEMENT("empleados",
                  XMLATTRIBUTES(emp.first_name AS "nombre",
                                emp.last_name AS "apellidos",
                                dep.department_name AS "departamento"))
       AS empleadosXml
FROM employees emp
JOIN departments dep ON emp.department_id = dep.department_id;

/*2*/
SELECT XMLELEMENT("managers",
                  XMLAGG(XMLELEMENT("manager",
                                   XMLFOREST(XMLELEMENT("nombreCompleto",
                                                       XMLFOREST(emp.first_name AS "nombre",
                                                                 emp.last_name AS "apellido")),
                                             XMLELEMENT("department", dep.department_name),
                                             XMLELEMENT("city", loc.city),
                                             XMLELEMENT("country", co.country_name)))))
       AS managersXml
FROM employees emp
JOIN departments dep ON emp.department_id = dep.department_id
JOIN locations loc ON dep.location_id = loc.location_id
JOIN countries co ON loc.country_id = co.country_id
WHERE emp.job_id LIKE '%MAN%';
