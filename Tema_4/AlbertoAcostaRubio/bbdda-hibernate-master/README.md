# bbdda-hibernate
Proyecto java utilizando JPA con Hibernate para acceder a bases de datos relacionales

# Problemas...
Posiblemente sea un problema de concepto pero estoy intentando que me funciona la consulta2 mejorPagado usando la consulta nativa.
 Query<Employee> query = session.createNativeQuery(sqlQuery, Employee.class);
 Sin embargo desde mi punto de vista parece que debo tener algún problema con el mapeo a la BBDD.
 He visto que si modifico los FetchType a EAGER ya me falla en la consulta de findByDepartment por eso deduzco que mi problema se origina en los modelos o en el config.
 