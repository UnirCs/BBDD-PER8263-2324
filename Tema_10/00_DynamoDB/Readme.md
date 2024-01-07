# Uso de DynamoDB

Con este ejercicio buscamos crear una base de datos no relacional DynamoDB y realizar operaciones CRUD sobre ella.

Elige un caso de uso de tu elección y crea una base de datos DynamoDB que lo soporte. Para ello, deberás crear una tabla con los atributos que consideres necesarios. Además, deberás insertar datos de prueba en la tabla.

Para facilitar esta tarea, puedes hacer uso de ChatGPT como ya hicimos anteriormente para generar un archivo JSON con los datos de prueba.

## 1. Ejercicio

### Parte I) Creación de la tabla desde la consola de AWS

Crea una nueva tabla de DynamoDB desde la consola de AWS de una forma similar a como se hizo en clase.

### Parte II) Inserta datos en la tabla

Haciendo uso de ChatGPT genera datos de prueba (no es necesario que sean muchos) y añádelos a la tabla de DynamoDB.

### Parte III) Crea algún índice

Crea algún índice que te permita realizar consultas más eficientes sobre la tabla. Dado que no hemos creado ningún LSI en el momento de creación de la tabla, solo podemos crear índices secundarios globales.

### Parte IV) Realiza operaciones de lectura.

Realiza operaciones Scan y GetItem (Examen y Consulta) sobre la tabla. Utiliza la consola de AWS.

## 2. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_10``. Deberás incluir :

- Una carpeta ``dynamodb`` con capturas de pantalla de la tabla creada y los índices creados durante el ejercicio.