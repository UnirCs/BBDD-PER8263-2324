# BBDDA-Spring-Data - Actividad 5

La verdad que me ha resultado muy amigable hacer el API con Spring Data, ya que es muy intuitivo y fácil de usar. Además, la integración con MySQL es muy sencilla y no requiere de mucho esfuerzo. En cuanto a la parte de los controladores, me ha resultado muy parecido a lo que ya habíamos hecho en la asignatura de Servicios y Procesos, por lo que no me ha resultado muy complicado. En cuanto a la parte de los repositorios, me ha resultado un poco más complicado, ya que no había trabajado con Spring Data antes y no sabía muy bien como funcionaba, pero al final he conseguido hacerlo funcionar.

En cuanto a los `repository` me he quedado sorprendido con la facilidad que tiene Spring Data para hacer consultas a la base de datos, ya que con solo poner el nombre del método, Spring Data es capaz de generar la consulta que necesitamos. Además, si necesitamos hacer una consulta más compleja, podemos usar la anotación `@Query` para escribir la consulta en SQL y que Spring Data la ejecute.

Pero claro, ¿y si tengo que hacer alguna consulta más compleja? He visto que Spring Data nos permite crear consultas personalizadas.

En el repositorio de departamentos `DepartmentRepository` he creado varias consultas más complejas, ya que necesitaba obtener el departamento con más empleados. Para ello, he usado la anotación `@Query` y he escrito la consulta en SQL. 

```java
    // Buscar departamentos que tienen más/menos de una cantidad específica de empleados
    @Query("SELECT d FROM Department d WHERE SIZE(d.deptEmps) > :count")
    List<Department> findDepartmentsWithMoreThanXEmployees(int count);

    @Query("SELECT d FROM Department d WHERE SIZE(d.deptEmps) < :count")
    List<Department> findDepartmentsWithLessThanXEmployees(int count);
```

## Probar el API con archivos tipo `.http`
Llevo un tiempo en el trabajo realizando pruebas de los `API` con ficheros `.http`. Yo trabajo en `.net` y uso `Visual Studio`, por lo que no puedo usar `Postman` para realizar las pruebas. 
Por ello, he estado buscando alternativas y he encontrado que `Visual Studio 2022` tiene integrado el poder realizar peticiones `HTTP` y `REST` desde el propio editor de código. 
Para ello, tenemos que crear un archivo con extensión `.http` y escribir las peticiones que queremos realizar.

Esto mismo se puede hacer también con `IntelliJ IDEA`.

He creado el paquete `http.test` donde he creado dos ficheros para las pruebas de los `API`:
- `employees.http`: En este fichero he creado las peticiones para probar los `Endpoint` de empleados.
- `departments.http`: En este fichero he creado las peticiones para probar los `Endpoint` de departamentos.

Este sería un ejemplo de una petición `GET` para obtener todos los empleados:

```http
GET http://localhost:8080/api/employees
Accept: application/json
```
