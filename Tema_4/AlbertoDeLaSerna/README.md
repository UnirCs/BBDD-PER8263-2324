# bbdda-hibernate
Proyecto de ejemplo de Hibernate para realizar la actividad del tema 4.

## Modelos creados
- `Salary.java`: representa la tabla `salaries` de la base de datos.
- `SalaryId.java`: representa la clave primaria de la tabla `salaries` de la base de datos.
- `Title.java`: representa la tabla `titles` de la base de datos.
- `TitleId.java`: representa la clave primaria de la tabla `titles` de la base de datos.

## Modelo `Employee.java` modificado
He modificado el modelo `Employee.java` para incluir las relaciones con los modelos `Salary.java` y `Title.java`.

```java
    @OneToMany(mappedBy = "empNo")
    private Set<Salary> salaries;

    @OneToMany(mappedBy = "empNo")
    private Set<Title> titles;
```

## DAO EmployeeDAO modificado para incluir los salarios y títulos

He incluido en el méntodo `findAll` la relación de sus salarios y títulos.

```java
    public List<Employee> findAll() throws SQLException {
        // List<Employee> employees = session.createNativeQuery("SELECT * FROM employees", Employee.class).list();
        List<Employee> employees = session.createQuery("FROM Employee", Employee.class).list();
        log.debug("Número de empleados: {}", employees.size());
        
        for(Employee employee : employees) {
            Set<Salary> salaries = employee.getSalaries();
            log.debug("Empleado: {} tiene {} salarios", employee.getFirstName(), salaries.size());
            Set<Title> titles = employee.getTitles();
            log.debug("Empleado: {} tiene {} títulos", employee.getFirstName(), titles.size());
        }
        
        return employees;
    }
```

## Consultas Actividad 2

**Consulta 01**

He creado en el DAO de empleados el método `numberOfEmployeesGroupByGenere()` que me devuelve una lista de arrays de objetos con el género y total


**Consulta 02 y 03**

He creado en el DAO de empleados el método `getTheBestSalaryEmployees(String department)` y `getTheSecondeBestSalaryEmployees(String department)` en ambos casos recupero los salarios y los filtro por el salario máximo. 
De tal forma, que en empleado sólo me quedo con los que tienen el salario máximo.

**Consulta 04**

He creado en el DAO de empleados el método `getTotalOfEmployeesHired(Integer month)` que me devuelve el total de empleados contratados en un mes determinado.

