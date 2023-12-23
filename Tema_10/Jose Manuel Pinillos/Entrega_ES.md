# Consultas con Elasticsearch

Con este ejercicio trataremos de asimilar los conceptos estudiados en clase sobre los tipos de datos y operaciones en Elasticsearch.
Deberás crear un clúster de prueba tal como se indica en [estas instrucciones](elasticsearch-operations-postman/Readme.md) e insertar los datos de prueba que se presentan. Encontrarás también una colección de Postman que puede ser de gran ayuda a la hora de realizar el ejercicio y trabajar con Elasticsearch en general.
Recuerda hacer uso de la [documentación](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl.html).

## 1. Ejercicio

<b> Para cada operación solicitada, incluye el comando cURL que se obtiene de Postman </b> en un archivo Entrega_ES.md

### Parte I) Generar un alias
- 1. ##### Genera un alias para el indice employees, que se llamará ``employees-alias``. A partir de ahora realizaremos las consultas siempre sobre este alias y no sobre el índice original

     Tenemos dos formas de hacerlo:

     - A través del comando `POST` con las instrucciones en JSON:

       ```http
       POST {{elasticsearch-host}}/_aliases
       ```

       ```json
       {
         "actions": [
           {
               "add": {
               "index": "employees",
               "alias": "employees-alias"
               }
           }
         ]
       }
       ```

       

       **<u>Resultado</u>**:

       ```json
       {
           "acknowledged": true
       }
       ```

       

     - Podemos usar un comando `PUT` para crear o actualizar un alias:

       ```http
       PUT {{elasticsearch-host}}/employees/_alias/employees-alias
       ```

       

       **<u>Resultado</u>**:

       ```json
       {
           "acknowledged": true
       }
       ```



### Parte II) Inserción de elementos
- 1. ##### Inserta un nuevo elemento en el índice utilizando tus datos (puedes inventartelos si lo consideras). Guarda el ID de documento que has obtenido tras la creación del elemento

     Para insertar un nuevo elemento en nuestro indice lo haremos a través de una petición `POST` y con los datos en JSON: 

     ```http
     POST {{elasticsearch-host}}/employees/_doc
     ```

     ```json
     {
         "FirstName":"Jose Manuel",
         "LastName":"Pinillos Rubio",
         "Address":"Orense 15, Logroño",
         "Age":39,
         "DateOfJoining":"1984-03-27",
         "Designation":"Electricista",
         "Gender":"Hombre",
         "Interests":"Música, deporte, naturaleza, animales",
         "Salary":2000
     }
     ```

     

     **<u>Resultado</u>**:

     ```json
     {
         "_index": "employees",
         "_type": "_doc",
         "_id": "q50Yk4wB_L614K24Tg2-",
         "_version": 1,
         "result": "created",
         "_shards": {
             "total": 2,
             "successful": 2,
             "failed": 0
         },
         "_seq_no": 9999,
         "_primary_term": 1
     }
     ```



### Parte III) Obtención simple de elementos
- 1. ##### Utilizando el ID del paso anterior, obtén los datos de tu registro. Deberías obtener lo mismo que anteriormente escribiste

     Para obtener un documento específico en Elasticsearch, necesitamos conocer el ID del documento: `q50Yk4wB_L614K24Tg2-`

     

     Realizaremos una petición `GET` con la siguiente consulta: 

     ```http
     GET {{elasticsearch-host}}/employees/_doc/q50Yk4wB_L614K24Tg2-
     ```

     

     **<u>Resultado</u>**:

     ```json
     {
         "_index": "employees",
         "_type": "_doc",
         "_id": "q50Yk4wB_L614K24Tg2-",
         "_version": 1,
         "_seq_no": 9999,
         "_primary_term": 1,
         "found": true,
         "_source": {
             "FirstName": "Jose Manuel",
             "LastName": "Pinillos Rubio",
             "Address": "Orense 15, Logroño",
             "Age": 39,
             "DateOfJoining": "1984-03-27",
             "Designation": "Electricista",
             "Gender": "Hombre",
             "Interests": "Música, deporte, naturaleza, animales",
             "Salary": 2000
         }
     }
     ```



### Parte IV) Eliminación de elementos
- 1. ##### Elimina el elemento que has creado anteriormente

     Para eliminar un documento específico en Elasticsearch, necesitamos conocer el ID del documento:  `q50Yk4wB_L614K24Tg2-`

     

     Realizaremos una petición `DELETE` con la siguiente consulta: 

     ```http
     DELETE {{elasticsearch-host}}/employees/_doc/q50Yk4wB_L614K24Tg2-
     ```

     

     **<u>Resultado</u>**:

     ```json
     {
         "_index": "employees",
         "_type": "_doc",
         "_id": "q50Yk4wB_L614K24Tg2-",
         "_version": 6,
         "result": "deleted",
         "_shards": {
             "total": 2,
             "successful": 2,
             "failed": 0
         },
         "_seq_no": 10004,
         "_primary_term": 1
     }
     ```



### Parte V) Consultas
- 1. ##### Obtener empleados cuyo puesto sea `Software Engineer`. [Revisa la documentación sobre term queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-term-query.html)

     Realizar consultas por término en Elasticsearch es una forma eficaz de  buscar documentos que contienen un valor específico en un campo  determinado. Estas consultas son útiles cuando se conoce el término exacto o la cadena de texto que se desea buscar.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
         "query": {
             "term": {
                 "Designation": {
                     "value": "Software Engineer"
                 }
             }
         }
     }
     ```

     

     **<u>Resultado</u>**: (Mostramos solo el primer resultado devuelto)

     ```json
     {
         "took": 1,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 4264,
                 "relation": "eq"
             },
             "max_score": 0.8523601,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "gZyYkowB_L614K24-y2j",
                     "_score": 0.8523601,
                     "_source": {
                         "FirstName": "REYES",
                         "LastName": "STREED",
                         "Designation": "Software Engineer",
                         "Salary": "55000",
                         "DateOfJoining": "2009-01-12",
                         "Address": "9276 Carriage Ave. Thornton, CO 80241",
                         "Gender": "Male",
                         "Age": 30,
                         "MaritalStatus": "Unmarried",
                         "Interests": "Gardening,Parachuting,Dancing,Tombstone Rubbing,Tatting,Beachcombing"
                     }
                 }
             ]
         }
     }
     ```



- 2. ##### Obtener empleados cuyo puesto NO sea ``Software Engineer``. [Revisa la documentación sobre bool queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-bool-query.html)

     Realizar consultas por un término que deseamos excluir, debemos realizar una consulta `bool` combinada con una cláusula `must_not`. Dentro de la cláusula `must_not`, usaremos una consulta `term`  para especificar el valor que deseamos excluir.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
       "query": {
         "bool": {
           "must_not": {
             "term": {
               "Designation": "Software Engineer"
             }
           }
         }
       }
     }
     ```

     

     **<u>Resultado</u>**: (Mostramos solo el primer resultado devuelto)

     ```json
     {
         "took": 4,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 5735,
                 "relation": "eq"
             },
             "max_score": 0.0,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "kKcRmIwB_L614K24A7RL",
                     "_score": 0.0,
                     "_source": {
                         "FirstName": "CIERRA",
                         "LastName": "TOOLS",
                         "Designation": "Senior Software Engineer",
                         "Salary": "61000",
                         "DateOfJoining": "2014-01-13",
                         "Address": "8445 Green Street Morristown, NJ 07960",
                         "Gender": "Female",
                         "Age": 35,
                         "MaritalStatus": "Married",
                         "Interests": "R/C Boats,Dolls,Cloud Watching,Animals/pets/dogs,Crocheting,Casino Gambling"
                     }
                 }
             ]
         }
     }
     ```



- 3. ##### Obtener la primera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos. [Revisa la documentación sobre paginación](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/paginate-search-results.html)

     Por defecto, las búsquedas devuelven los 10 mejores éxitos. Para desplazarnos por un conjunto mas amplio de resultados podemos usar los parámetros `from` y  `size`:

     - El parámetro `from` define el número de páginas a saltar, por defecto a `0`.
     - El parámetro `size` define el número máximo de elementos para devolver.

     Juntos, estos dos parámetros definen una página de resultados.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
       "from": 0, //En esta consulta podríamos eliminar este parámetro.
       "size": 35,
       "query": {
         "bool": {
           "must_not": {
             "term": {
               "Designation": "Software Engineer"
             }
           }
         }
       }
     }
     ```

     

     **<u>Resultado</u>**: (Mostramos solo el primer resultado devuelto)

     ```json
     {
         "took": 3,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 4264,
                 "relation": "eq"
             },
             "max_score": 0.8522601,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "96cRmIwB_L614K24A8pW",
                     "_score": 0.8522601,
                     "_source": {
                         "FirstName": "REYES",
                         "LastName": "STREED",
                         "Designation": "Software Engineer",
                         "Salary": "55000",
                         "DateOfJoining": "2009-01-12",
                         "Address": "9276 Carriage Ave. Thornton, CO 80241",
                         "Gender": "Male",
                         "Age": 30,
                         "MaritalStatus": "Unmarried",
                         "Interests": "Gardening,Parachuting,Dancing,Tombstone Rubbing,Tatting,Beachcombing"
                     }
                 }
             ]
         }
     }
     ```



- 4. ##### Obtener la tercera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos.

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
       "from": 2,
       "size": 35,
       "query": {
         "bool": {
           "must_not": {
             "term": {
               "Designation": "Software Engineer"
             }
           }
         }
       }
     }
     ```

     

     **<u>Resultado</u>**: (Mostramos solo el primer resultado devuelto)

     ```json
     {
         "took": 3,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 5735,
                 "relation": "eq"
             },
             "max_score": 0.0,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "kqcRmIwB_L614K24A7RL",
                     "_score": 0.0,
                     "_source": {
                         "FirstName": "HERSCHEL",
                         "LastName": "BARTOLOME",
                         "Designation": "Senior Software Engineer",
                         "Salary": "70000",
                         "DateOfJoining": "2008-06-02",
                         "Address": "9261 Andover St. De Pere, WI 54115",
                         "Gender": "Male",
                         "Age": 35,
                         "MaritalStatus": "Unmarried",
                         "Interests": "Cheerleading,Snorkeling,Reading To The Elderly,Writing Music,Darts"
                     }
                 }
             ]
         }
     }
     ```



- 5. ##### Obtener los primeros 13 empleados cuyo salario sea mayor a 67.000 dólares. [Revisa la documentación sobre range queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-range-query.html)

     La consulta `range` en Elasticsearch nos permite buscar documentos que contienen campos con valores que caen dentro de un rango  especificado. Este tipo de consulta es especialmente útil para campos numéricos, fechas, y otros tipos de datos ordenados.

     Podemos especificar rangos para valores:

     - `gt`: mayor que.
     - `gte`: mayor o igual que.
     - `lt`: menor que.
     - `lte`: menor o igual que.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
       "size": 13,
       "query": {
         "range": {
           "Salary": {
             "gt": 67000
           }
         }
       }
     }
     ```

     

     **<u>Resultado</u>**: (Mostramos solo el primer resultado devuelto)

     ```json
     {
         "took": 3,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 1591,
                 "relation": "eq"
             },
             "max_score": 1.0,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "kqcRmIwB_L614K24A7RL",
                     "_score": 1.0,
                     "_source": {
                         "FirstName": "HERSCHEL",
                         "LastName": "BARTOLOME",
                         "Designation": "Senior Software Engineer",
                         "Salary": "70000",
                         "DateOfJoining": "2008-06-02",
                         "Address": "9261 Andover St. De Pere, WI 54115",
                         "Gender": "Male",
                         "Age": 35,
                         "MaritalStatus": "Unmarried",
                         "Interests": "Cheerleading,Snorkeling,Reading To The Elderly,Writing Music,Darts"
                     }
                 }
             ]
         }
     }
     ```



- 6. ##### Obtener el número total que hayan entrado en la empresa en el mes de Mayo del año 2003. No se pide una consulta específica, solo saber el número total de hits.

     Al igual que en la consulta anterior, realizaremos una consulta de tipo `range`.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
       "size": 0, // Indicamos que no muestre ningun resultado.
       "query": {
         "range": {
           "DateOfJoining": {
             "gte": "2003-05-01",	// Valores mayores o iguales a la fecha descrita.
             "lte": "2003-05-31"		// Valores menores o iguales a la fecha descrita.
           }
         }
       }
     }
     ```

     

     **<u>Resultado</u>**:

     ```json
     {
         "took": 1,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 8,	// Número total resultados de la consulta.
                 "relation": "eq"
             },
             "max_score": null,
             "hits": []
         }
     }
     ```

     La respuesta de Elasticsearch incluye el campo `hits`, que a su vez incluye el campo `total`. Este `total` nos da el número total de documentos que coinciden con nuestra consulta,  que en este caso representa el número total de empleados que ingresaron en mayo de 2003.



- 7. ##### Obtener empleados cuyo nombre sea ``NATALIE``. [Revisa la documentación sobre match queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-match-query.html)

     La consulta `match` en Elasticsearch es una de las consultas más comunes y versátiles para realizar búsquedas de texto completo. Esta consulta busca documentos que coincidan con un valor dado en un campo específico, pero a diferencia de las consultas `term` o `terms`, la consulta `match` analiza el texto de búsqueda y busca términos analizados en lugar de una coincidencia exacta del texto completo. Esto la hace ideal para trabajar con campos de texto completo, como descripciones o títulos.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
         "query": {
             "match": {
                 "FirstName": "NATALIE"
             }
         }
     }
     ```

     

     **<u>Resultado</u>**:

     ```json
     {
         "took": 1,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 1,
                 "relation": "eq"
             },
             "max_score": 8.804874,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "QKcRmIwB_L614K24A7lN",
                     "_score": 8.804874,
                     "_source": {
                         "FirstName": "NATALIE",
                         "LastName": "SERVIS",
                         "Designation": "Senior Software Engineer",
                         "Salary": "61000",
                         "DateOfJoining": "2003-09-19",
                         "Address": "34 Kingston St. El Dorado, AR 71730",
                         "Gender": "Female",
                         "Age": 35,
                         "MaritalStatus": "Unmarried",
                         "Interests": "Guitar,Learning A Foreign Language,Blacksmithing,Embroidery,Collecting,Becoming A Child Advocate,Taxidermy"
                     }
                 }
             ]
         }
     }
     ```



- 8. ##### Obtener empleados cuya dirección sea o contenga ``Street``. [Revisa la documentación sobre queries sobre campos search-as-you-type](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-as-you-type.html)

     La consulta `multi_match` en Elasticsearch permite buscar un término en múltiples campos de un índice. Esta consulta es especialmente útil cuando tenemos datos similares repartidos en varios campos y queremos que una consulta busque en todos ellos.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
         "query": {
             "match": {
                 "FirstName": "NATALIE"
             }
         }
     }
     ```

     

     **<u>Resultado</u>**:

     ```json
     {
         "took": 1,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 1,
                 "relation": "eq"
             },
             "max_score": 8.804874,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "QKcRmIwB_L614K24A7lN",
                     "_score": 8.804874,
                     "_source": {
                         "FirstName": "NATALIE",
                         "LastName": "SERVIS",
                         "Designation": "Senior Software Engineer",
                         "Salary": "61000",
                         "DateOfJoining": "2003-09-19",
                         "Address": "34 Kingston St. El Dorado, AR 71730",
                         "Gender": "Female",
                         "Age": 35,
                         "MaritalStatus": "Unmarried",
                         "Interests": "Guitar,Learning A Foreign Language,Blacksmithing,Embroidery,Collecting,Becoming A Child Advocate,Taxidermy"
                     }
                 }
             ]
         }
     }
     ```



- 9. ##### Obtener empleados cuya dirección sea o contenga ``wood``.

     

- 10. ##### Obtener empleados interesados en ``Wrestling``.

      

- 11. ##### Obtener el número de hombres y mujeres interesad@s en ``Wrestling``.[Revisa la documentación sobre term aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-terms-aggregation.html)

      

- 12. ##### En base a la consulta anterior, obtener la edad media de cada grupo (grupo hombres y grupo mujeres). [Revisa la documentación sobre sub-agregaciones](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations.html) y [sobre la agregación avg](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-metrics-avg-aggregation.html)

      

- 13. ##### Obtener el número de empleados en función de los siguientes tramos de salario: menor de 60.000 dólares (``tramo 1``), entre 60.000 dólares y 67.000 dólares (``tramo 2``) y superior a 67.000 dólares (``tramo 3``). [Revisa la documentación sobre range aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-range-aggregation.html)

      

- 14. ##### En base a la consulta anterior, para cada tramo, hallar el número de empleados que están casados y no casados.

      

### Parte VI) Crear otro índice y modificar el alias
- 1) Crea un nuevo índice de la misma forma que hiciste al principio, pero ahora llámalo ``employees-v2`` y mete en él todos los datos del fichero de prueba. Modifica el alias ``employees-alias`` que creaste antes para que apunte tanto al índice ``employees`` original como al nuevo ``employees-v2``. Puedes comprobar que lo has hecho correctamente ejecutando la operación "Obtener todos los alias" de la colección de Postman.
- 2) Realiza alguna de las consultas anteriores. ¿Qué observas?
- 3) Elimina ``employees`` del conjunto de índices a los que hace referencia el alias.

## 2. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_10``. Deberás incluir :

- Un archivo ``Entrega_ES.md`` con los comandos cURL obtenidos a través de Postman.