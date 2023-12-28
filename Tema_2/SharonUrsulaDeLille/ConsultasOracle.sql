/*Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.
Cada resultado XML devuelto por la consulta (la consulta debe devolver 1 registro por empleado)*/
SELECT XMLELEMENT("empleados",
    XMLATTRIBUTES (e.FIRST_NAME as "nombre",
                  e.LAST_NAME as "apellidos",
                  d.DEPARTMENT_NAME as "departamento")) as employee
from EMPLOYEES e
join DEPARTMENTS d on d.DEPARTMENT_ID = e.DEPARTMENT_ID;

/*Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers. 
El XML devuelto por la consulta (debe devolver un único registro, con todos los managers) */
SELECT XMLELEMENT("managers",
    XMLATTRIBUTES (e.FIRST_NAME as "nombre",
                  e.LAST_NAME as "apellido",
                  d.DEPARTMENT_NAME as "departamento",
                  l.CITY as "city",
                  c.COUNTRY_NAME as "country")
       )as manager
from EMPLOYEES e
join DEPARTMENTS d on d.DEPARTMENT_ID = e.DEPARTMENT_ID
join LOCATIONS l on d.LOCATION_ID = l.LOCATION_ID
join COUNTRIES c on l.COUNTRY_ID = c.COUNTRY_ID
where e.MANAGER_ID IN (SELECT DISTINCT manager_id FROM employees);
