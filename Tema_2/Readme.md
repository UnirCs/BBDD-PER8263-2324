Consultas SQL y SQL/XML (SQLX) con JDBC
==============================================================

Una vez instaladas las bases de datos, vamos a empezar a trabajar con ellas.

Los objetivos de este ejercicio son:
- Volver a coger algo de experiencia con consultas a bases de datos relacionales.
- Trabajar con consultas SQL/XML y validaciones de XML con XML Schema.
- Uso de JDBC para ejecutar consultas SQL y SQL/XML desde una aplicación Java.

Dado que trabajaremos con dos bases de datos diferentes, MySQL y Oracle, necesitaremos usar dos drivers diferentes, uno para cada base de datos.
Cada driver nos permitirá obtener una conexión a la base de datos, y a partir de ahí, ejecutar consultas SQL.

Puedes encontrar el código de apoyo que cuenta con estas dependencias en el siguiente [repositorio de GitHub](https://github.com/UnirCs/bbdda-jdbc).

## 1. Consultas sobre MySQL

Utilizando DataGrip, ejecuta las siguientes consultas SQL en la base de datos ``employees`` de MySQL:
1. Obtener el número de hombres y mujeres de la base de datos. Ordenar de forma descendente.
2. Mostrar el nombre, apellido y salario de la persona mejor pagada de un departamento concreto (parámetro variable).
3. Mostrar el nombre, apellido y salario de la segunda persona mejor pagada de un departamento concreto (parámetro variable).
4. Mostrar el número de empleados contratados en un mes concreto (parámetro variable).

Una vez realizadas las consultas, utiliza el código de apoyo para ejecutarlas a través de JDBC.

Deberás entregar un archivo ``ConsultasMySQL.sql`` con las consultas realizadas y el proyecto Java.

## 2. Consultas sobre Oracle Database
Utilizando DataGrip, ejecuta las siguientes consultas [SQL/XML](https://oracle-base.com/articles/misc/sqlxml-sqlx-generating-xml-content-using-sql) en la base de datos ``hr`` de Oracle:
1. (Debes usar ``XMLELEMENT``) Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento. Cada resultado XML devuelto por la consulta (**la consulta debe devolver 1 registro por empleado**) debe ser válido frente a este XML Schema:

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <xs:schema
   	xmlns:xs="http://www.w3.org/2001/XMLSchema"
   	xmlns:xdb="http://xmlns.oracle.com/xdb">
   	<xs:element name="empleados">
   		<xs:complexType>
   			<xs:attribute name="nombre" type="xs:string" />
   			<xs:attribute name="apellidos" type="xs:string" />
   			<xs:attribute name="departamento" type="xs:string"/>
   		</xs:complexType>
   	</xs:element>
   </xs:schema> 
   ```

2. (Debes usar ``XMLELEMENT``, ``XMLAGG`` y ``XMLFOREST``) Mostrar el nombre, apellido, nombre de departamento, ciudad y pais de los empleados que son Managers. El XML devuelto por la consulta (**debe devolver un único registro, con todos los managers**) debe ser válido frente a este XML Schema:

   ```xml
   <?xml version = "1.0" encoding="UTF-8"?>
   <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
       xmlns:xdb="http://xmlns.oracle.com/xdb" elementFormDefault="qualified">
       <xs:element name="managers">
           <xs:complexType>
               <xs:sequence>
                   <xs:element name="manager" minOccurs="0" maxOccurs="unbounded">
                       <xs:complexType>
                           <xs:sequence>
                               <xs:element name ="nombreCompleto">
                                   <xs:complexType>
                                       <xs:sequence>
                                           <xs:element name = "nombre" type="xs:string"/>
                                           <xs:element name = "apellido" type="xs:string"/>
                                       </xs:sequence>
                                   </xs:complexType>
                               </xs:element>
                               <xs:element name = "department" type="xs:string"/>
                               <xs:element name = "city" type="xs:string"/>
                              <xs:element name = "country" type="xs:string"/>
                           </xs:sequence>
                       </xs:complexType>
                   </xs:element>
               </xs:sequence>
           </xs:complexType>
       </xs:element>
   </xs:schema>
   ```

    Una vez realizadas las consultas, utiliza el código de apoyo para ejecutarlas a través de JDBC.

    Deberás entregar un archivo ``ConsultasOracle.sql`` con las consultas realizadas y el proyecto Java.

## 2. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_2``. Deberás incluir dos archivos SQL, ``ConsultasOracle.sql`` y ``ConsultasMySQL.sql``, junto con el código del proyecto Java utilizado para realizar las consultas con JDBC.