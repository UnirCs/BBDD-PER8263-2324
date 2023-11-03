-- CREAR UN XML CON LOS EMPLEADOS Y SUS DEPARTAMENTOS
SELECT
    XMLELEMENT(
        NAME "empleados",
        XMLATTRIBUTES (
            EMPLOYEES.FIRST_NAME  AS "nombre",
            EMPLOYEES.LAST_NAME   AS "apellidos",
            DEPARTMENTS.DEPARTMENT_NAME AS "departamento"
        )
    ) AS "xml"
FROM
    HR.EMPLOYEES
INNER JOIN
    HR.DEPARTMENTS
ON
    EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID;

-- CREAR UN XML CON LOS EMPLEADOS Y SUS DEPARTAMENTOS Y SUS LOCALIZACIONES
SELECT
    XMLELEMENT(
        NAME "managers",
        XMLAGG (
                XMLELEMENT(
                    NAME "manager",
                    XMLFOREST(
                        XMLFOREST(
                            EMPLOYEES.FIRST_NAME AS "nombre",
                            EMPLOYEES.LAST_NAME AS "apellido"
                        ) AS "nombreCompleto",
                        DEPARTMENTS.DEPARTMENT_NAME AS "department",
                        LOCATIONS.CITY AS "city",
                        COUNTRIES.COUNTRY_NAME AS "country"
                    )
                )
            )
    ) AS "xml"
FROM
  HR.EMPLOYEES
INNER JOIN
  HR.DEPARTMENTS
ON
  EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID
INNER JOIN
  HR.LOCATIONS
ON
  DEPARTMENTS.LOCATION_ID = LOCATIONS.LOCATION_ID
INNER JOIN
  HR.COUNTRIES
ON
  LOCATIONS.COUNTRY_ID = COUNTRIES.COUNTRY_ID;
