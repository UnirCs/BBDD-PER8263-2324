# DynamoDB

## 1. Ejercicio

### Parte I) Creación de la tabla desde la consola de AWS

Se ha creado la tabla Seguimiento_Fitness que contiene las siguientes claves:

- **Clave de partición**: contendrá un Hash MD5 del correo electrónico.
- **Clave de ordenación**: compuesta por el nombre y el apellido del usuario.

![Tabla](images/Tabla.png)

<center><i>Tabla "Seguimiento_Fitness"</i></center>



### Parte II) Inserta datos en la tabla

Hemos introducido datos en nuestra tabla siguiendo el siguiente patrón:

- **Primary Key (Compuesta)**:
  - **Clave de Partición (PK)**: Hash_ID
  - **Clave de Ordenación (SK)**: Nombre&Apellidos
- **Atributos adicionales**:
  - Actividades (compuesta por: Actividad y fecha de realización).
  - Email.
  - Género.   
  - Meta.
  - Nacimiento.



![Elementos](images/Elementos.png)

<center><i>Elementos de la tabla</i></center>



### Parte III) Crea algún índice GSI

![Indice](images/Indice.png)

<center><i>Índices de la tabla</i></center>



### Parte IV) Realiza operaciones de lectura.

#### Examen

![Examen](images/Examen.png)

<center><i>Resultado de la realización de un Scan de la tabla aplicando filtros</i></center>



#### Consulta

![Consulta_atributos_especificos](images/Consulta_atributos_especificos.png)

<center><i>Resultado de la consulta con atributos específicos</i></center>



![Consulta_todos_atributos](images/Consulta_todos_atributos.png)

<center><i>Resultado de la consulta con todos los atributos</i></center>