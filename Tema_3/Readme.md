Inserciones simples y cargas de datos desde CSV con JDBC
==============================================================

Una vez que hemos jugado con las consultas y conocemos una parte considerable de los esquemas vamos a pasar a introducir información en ellos.

Los objetivos de este ejercicio son:
- Recordar conceptos fundamentales a la hora de ejecutar INSERTs o UPDATEs.
- Ser conscientes de la pérdida de rendimiento que supone ejecutar una consulta por cada inserción.
- Ser conscientes de que MERGE es una operación atómica pero que puede ser muy costosa.
- Usar JDBC para insertar o actualizar registros de forma simple y a través de CSV desde una aplicación Java.
- Usar ChatGPT para generar datos de prueba.

Dado que trabajaremos con dos bases de datos diferentes, MySQL y Oracle, necesitaremos usar dos drivers diferentes, uno para cada base de datos.
Cada driver nos permitirá obtener una conexión a la base de datos, y a partir de ahí, ejecutar consultas SQL.

Puedes encontrar el código de apoyo que cuenta con estas dependencias en el siguiente [repositorio de GitHub](https://github.com/UnirCs/bbdda-jdbc).

## 1. Inserción simple

Elige una tabla al azar de la base de datos ``hr`` de Oracle o de la base de datos ``employees`` de MySQL. Inserta un único registro en ella utilizando JDBC.

## 2. Carga de datos desde CSV
Trabajaremos con MySQL. En el código de apoyo verás que se encuentra el fichero ``unirEmployees.csv``. Contiene información de empleados de prueba. La aplicación Java lee dicha información y la transforma en sentencias INSERT o UPDATE en función de si los registros existen o no en la base de datos.

Realiza las siguientes tareas:

1. Utilizando [ChatGPT](https://chat.openai.com/), crea un archivo CSV que contenga más departamentos (puedes inventarte los nombres). Al menos 10.
   
2. Utilizando [ChatGPT](https://chat.openai.com/), crea un archivo CSV que contenga más empleados, pero deben ser obligatoriamente de los departamentos que has incluido en el CSV del paso anterior. Al menos 100.
3. Crea una aplicación en Java que ingeste ambos archivos CSV en la base de datos (primero el de departamentos y luego el de empleados). Deberás utilizar JDBC para ello. La aplicación debe ser capaz de detectar si los registros ya existen en la base de datos y en ese caso, actualizarlos. Si no existen, deberá insertarlos. Además, el commit debe realizarse una vez que se hayan procesado todos los registros **de cada archivo CSV**.
4. Haz dos capturas de pantalla de algunos de los datos incluidos en la tabla de empleados y de departamentos.

## 3. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_3``. Deberás incluir :

- Dos archivos SQL, ``InsercionesEmployees.sql`` y ``InsercionesDepartments.sql``.
- El código del proyecto Java utilizado para realizar las inserciones con JDBC (debe incluir los ficheros CSV).