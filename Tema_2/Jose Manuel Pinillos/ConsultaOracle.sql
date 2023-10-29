/* 1. Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento. */
SELECT
    XMLELEMENT("empleados",
        XMLATTRIBUTES (
            FIRST_NAME AS "nombre",
            LAST_NAME AS "apellidos",
            DEPARTMENT_NAME AS "departamento"))
        AS empleados
FROM EMPLOYEES, DEPARTMENTS;

/* 2. Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers. */
SELECT
    XMLELEMENT("managers",
        XMLATTRIBUTES (
            FIRST_NAME AS "nombre",
            LAST_NAME AS "apellido",
            DEPARTMENT_ID AS "department",
            CITY AS "city",
            COUNTRY_NAME AS "country"))
        AS managers
FROM employees, locations, countries;

SELECT DISTINCT FIRST_NAME as Nombre, LAST_NAME as Apellido, DEPARTMENT_NAME as Departamento, CITY as Ciudad, COUNTRY_NAME as Pais
FROM EMPLOYEES, LOCATIONS, COUNTRIES, DEPARTMENTS, JOB_HISTORY, JOBS
WHERE EMPLOYEES.EMPLOYEE_ID = JOB_HISTORY.EMPLOYEE_ID and
      EMPLOYEES.DEPARTMENT_ID = JOB_HISTORY.DEPARTMENT_ID and
      JOB_HISTORY.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID and
      DEPARTMENTS.LOCATION_ID = LOCATIONS.LOCATION_ID and
      LOCATIONS.COUNTRY_ID = COUNTRIES.COUNTRY_ID and
      JOBS.JOB_TITLE like '%Manager%'