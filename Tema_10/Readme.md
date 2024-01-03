# Bases de datos no relacionales

## 1. Elasticsearch

Con [este ejercicio](https://github.com/UnirCs/BBDD-PER8263-2324/blob/master/Tema_10/00_Elasticsearch/Readme.md) trataremos de asimilar los conceptos estudiados en clase sobre los tipos de datos y operaciones en Elasticsearch.
Deberás crear un clúster de prueba tal como se indica en [estas instrucciones](https://github.com/UnirCs/elasticsearch-operations-postman) e insertar los datos de prueba que se presentan. Encontrarás también una colección de Postman que puede ser de gran ayuda a la hora de realizar el ejercicio y trabajar con Elasticsearch en general.
Recuerda hacer uso de la [documentación](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl.html).

## 2. Redis

Con [este ejercicio](https://github.com/UnirCs/BBDD-PER8263-2324/blob/master/Tema_10/00_Redis/Readme.md) trataremos de asimilar los conceptos estudiados en clase sobre los tipos de datos y operaciones en Redis.
Vamos a continuar con el código del ejercicio opcional del Tema 5 y añadiremos una caché de Redis para almacenar los datos de los empleados. De esta forma, cuando se realice una petición GET a la API, se comprobará si el dato solicitado se encuentra en la caché. En caso afirmativo, se devolverá el dato almacenado en la caché. En caso negativo, se consultará a la base de datos y se almacenará el dato en la caché para futuras peticiones.

## 3. Amazon DynamoDB

Con [este ejercicio](https://github.com/UnirCs/BBDD-PER8263-2324/blob/master/Tema_10/00_DynamoDB/Readme.md) trataremos de asimilar los conceptos estudiados en clase sobre DynamoDB.
Es un ejercicio sencillo, de nivel introductorio, en el que se pide crear una tabla de DynamoDB y realizar operaciones CRUD sobre ella. Se busca obtener algo de práctica con la consola de AWS y familiarizarse con los conceptos básicos de DynamoDB.