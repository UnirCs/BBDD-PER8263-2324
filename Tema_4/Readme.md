JPA y Hibernate
==============================================================

Una vez que hemos jugado con las consultas y conocemos una parte considerable de los esquemas vamos a pasar a introducir información en ellos.

Los objetivos de este ejercicio son:
- Analizar las diferentes relaciones existentes en el esquema ``employees`` de MySQL.
- Asimilar los conceptos principales de JPA y Hibernate.
- Completar la definición de entidades JPA.
- Realizar DAOs para las entidades JPA y alguna que otra consulta.

Dado que trabajaremos con dos bases de datos diferentes, MySQL y Oracle, necesitaremos usar dos drivers diferentes, uno para cada base de datos.
Cada driver nos permitirá obtener una conexión a la base de datos, y a partir de ahí, ejecutar consultas SQL.

Puedes encontrar el código de apoyo que cuenta con estas dependencias en el siguiente [repositorio de GitHub](https://github.com/UnirCs/bbdda-jdbc).

## 1. JPA

JPA (Java Persistence API), es una especificación estándar de Java que describe la gestión de la persistencia y el mapeo objeto-relacional en aplicaciones Java. En otras palabras, JPA facilita la interacción entre objetos Java en tu aplicación y datos en una base de datos relacional.

En el desarrollo tradicional de aplicaciones Java que utilizan bases de datos relacionales, los desarrolladores deben escribir consultas SQL manualmente y manejar la conversión de datos entre las representaciones de tablas de la base de datos y los objetos de la aplicación (un proceso conocido como "mapeo objeto-relacional" o ORM). JPA busca simplificar este proceso al permitir que los desarrolladores trabajen directamente con objetos en lugar de con SQL directo.

Características clave de JPA:
- ORM (Mapeo Objeto-Relacional): JPA permite definir cómo se mapea una clase de Java a una tabla de base de datos, cómo se mapean los atributos de la clase a las columnas de la tabla, y cómo se manejan las relaciones entre entidades.
- Entidades: En JPA, una entidad es una clase Java ligera y simple que está mapeada a una tabla de base de datos. Cada instancia de una entidad corresponde a una fila en la tabla.
- EntityManager: Esta es la clase principal de JPA que gestiona el ciclo de vida de las entidades, incluyendo operaciones como crear, leer, actualizar y borrar (CRUD).

## 2. Hibernate
Hibernate es un marco de trabajo (framework) para el mapeo objeto-relacional (ORM) que facilita el desarrollo de aplicaciones Java para interactuar con bases de datos relacionales. Es una de las implementaciones más populares de la especificación de la Java Persistence API (JPA) y proporciona una manera de mapear "entidades" Java a tablas de bases de datos y viceversa, permitiendo a los desarrolladores manejar datos persistidos como objetos de Java, lo que simplifica mucho el desarrollo de la capa de acceso a datos.

## 3. Ejercicio
1. Revisa el [código de apoyo](https://github.com/UnirCs/bbdda-hibernate) y la clase en la que se explica, la [documentación de JPA](https://jakarta.ee/specifications/persistence/3.0/) y la de [Hibernate](https://docs.jboss.org/hibernate/orm/6.3/introduction/html_single/Hibernate_Introduction.html).
2. Completa las entidades JPA que faltan en el proyecto de apoyo. Deberás añadir el código correspondiente para las tablas ``Salary`` y ``Title`` así como modificar otras si lo crees conveniente.
3. Modifica los DAOs ``EmployeesDAO`` y ``DepartmentsDAO``. Incluye todas las consultas que realizaste en la actividad 2 (para MySQL) y comprueba que devuelven los mismos resultados que si se ejecutasen en DataGrip.

## 3. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_4``. Deberás incluir :

- El código del proyecto Java utilizado para realizar los puntos 2 y 3 del ejercicio.
