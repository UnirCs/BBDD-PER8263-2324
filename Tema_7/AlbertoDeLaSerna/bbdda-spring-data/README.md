# BBDDA-Spring-Data - Actividad 7

He tenido problemas de uso compartido de memoria en `DeptEmp`, por lo que he tenido que quitar de la clase Departamentos la relación con DeptEmp.
Es debido a que `Employee` también tiene una relación con `DeptEmp` y al tener una relación bidireccional, se produce un error.

Para resolver el ejercicio he decido intentar llevar toda la lógica al servicio, y dejar en los repositorios lo más sencillo posible.

He creado en el paquete `http.test` un test para probar el nuevo endpoint. El test se llama `promotion.http` y se puede ejecutar desde el IDE.

