# TEMA 4-BDA
Proyecto java que da solución a la actividad del tema 4-JPA y Hibernate.

Se han incluido las entidades JPA correspondientes a las tablas `salaries` y `title` y se ha modificado la entidad `Employee` para incluir las listas de salarios y puestos en la empresa.

```
	@OneToMany(mappedBy = "empNo")
	private Set<Salary> salaries;
	
	@OneToMany(mappedBy = "empNo")
	private Set<Title> titles;
```

##CONSULTAS
Para resolver las consultas que se crearon en el tema 2 se han creados 3 métodos nuevos en `EmployeesDao.java`. Estos métodos son servicios de búsqueda que devuelven una lista de empleados (`List<Employee>`) que pueden ser utlizados para más propósitos dentro de un hipotético sistema real. Posteriormente, y según la necesidad real de la consulta, el sistema realizarán un conteo del número de registros recuperados, o mostrará la información del registro que necesite.

- **Consulta 1**: Método `findByGender(Employee.Gender gender)` creado en `EmployeesDao.java`. Recupera la lista de empleados según su género. De esta forma, una vez obtenida la lista de empleados de un género concreto tendremos el número total a través del método `size()`.

- **Consulta 2 y 3**: Método `findByDeptSalarySorted(String departamentId)` creado en `EmployeesDao.java`. Recupera la lista de empleados de un departamento dado ordenados por salario (de mayor a menor). Accediendo a las posiciones 0 y 1 de la lista devuelta obtendremos la respuesta a las consultas 2 y 3.

- **Consulta 4**: Método `findByHireMonth(Integer hireMonth)` creado en `EmployeesDao.java`. Recupera la lista de empleados cuyo mes de la fecha de contratación sea el recibido como parámetro. De esta forma, una vez obtenida la lista de empleados tendremos el número total a través del método `size()`.
