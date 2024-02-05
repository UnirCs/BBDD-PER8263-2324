# TEMA 7 - Transacciones con Spring y Spring Data JPA

Proyecto que da solución a la actividad del Tema 7 - Transacciones con Spring y Spring Data JPA.


## Desarrollo realizado
Se parte de la idea de que los empleados que actualmente siguen en la empresa son aquellos que tienen un puesto (TITLE) con fecha fin 9999-01-01 y que, por lo tanto, aquellas relaciones entre la entidad EMPLOYEE y las entidades SALARY, DEPARTMENT y TITLE que tengan una fecha fin distinta a 9999-01-01 son relaciones finalizadas.

A la hora de recuperar el puesto, salario y departamento actual de un empleado sólo se recuperará aquellos que tengan fecha fin 9999-01-01. En caso de que no haya ningún elemento con esta característica, no se devolverá nada (NULL).

Los *endpoint* creados para consultar estos datos son los siguientes:
- `/api/titles/{empNumber}/actualTitle`
- `/api/deptEmp/{empNumber}/actualDept`
- `/api/deptEmp/{empNumber}/actualSalary`

Se han incluido, además, los repositorios necesarios para cada una de las entidades utilizadas.

En el raíz del proyecto se incluye también el archivo de configuración de *test* para *Postman*.