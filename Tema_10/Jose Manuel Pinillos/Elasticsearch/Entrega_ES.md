# Consultas con Elasticsearch

## 1. Ejercicio

<b> Para cada operación solicitada, incluye el comando URL que se obtiene de Postman </b> en un archivo Entrega_ES.md

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



- 8. ##### Obtener empleados cuya dirección sea o contenga `Street`. [Revisa la documentación sobre queries sobre campos search-as-you-type](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-as-you-type.html)

     La consulta `multi_match` en Elasticsearch permite buscar un término en múltiples campos de un índice. Esta consulta es especialmente útil cuando tenemos datos similares repartidos en varios campos y queremos que una consulta busque en todos ellos.

     Estas consultas pueden realizarse en una variedad de tipos de campos. Sin embargo, su uso es más común y efectivo en campos de `text` o `search_as_you_type`.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
         "query": {
             "multi_match": {
                 "query": "Street",
                 "type": "bool_prefix",
                 "fields": [
                     "Address"
                 ]
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
                 "value": 1580,
                 "relation": "eq"
             },
             "max_score": 1.0,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "kKcRmIwB_L614K24A7RL",
                     "_score": 1.0,
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



- 9. ##### Obtener empleados cuya dirección sea o contenga `wood`.

     Al igual que en la consulta anterior, realizaremos una consulta de tipo `multi_match`.

     

     A través del comando `GET` con las instrucciones en JSON:

     ```http
     GET {{elasticsearch-host}}/employees/_search
     ```

     ```json
     {
         "query": {
             "multi_match": {
                 "query": "wood",
                 "type": "bool_prefix",
                 "fields": [
                     "Address"
                 ]
             }
         }
     }
     ```

     

     **<u>Resultado</u>**: (Mostramos solo el primer resultado devuelto)

     ```json
     {
         "took": 2,
         "timed_out": false,
         "_shards": {
             "total": 1,
             "successful": 1,
             "skipped": 0,
             "failed": 0
         },
         "hits": {
             "total": {
                 "value": 102,
                 "relation": "eq"
             },
             "max_score": 1.0,
             "hits": [
                 {
                     "_index": "employees",
                     "_type": "_doc",
                     "_id": "SacRmIwB_L614K24A7VL",
                     "_score": 1.0,
                     "_source": {
                         "FirstName": "BETTINA",
                         "LastName": "SIVIE",
                         "Designation": "Senior Software Engineer",
                         "Salary": "60000",
                         "DateOfJoining": "2006-07-24",
                         "Address": "127 Woodland Drive Stockbridge, GA 30281",
                         "Gender": "Female",
                         "Age": 32,
                         "MaritalStatus": "Married",
                         "Interests": "Beach/Sun tanning,Collecting Artwork,Playing music,Illusion,Train Spotting"
                     }
                 }
             ]
         }
     }
     ```



- 10. ##### Obtener empleados interesados en `Wrestling`.

      Al igual que en la consulta anterior, realizaremos una consulta de tipo `multi_match`.

      

      A través del comando `GET` con las instrucciones en JSON:

      ```http
      GET {{elasticsearch-host}}/employees/_search
      ```

      ```json
      {
          "query": {
              "multi_match": {
                  "query": "Wrestling",
                  "type": "bool_prefix",
                  "fields": [
                      "Interests"
                  ]
              }
          }
      }
      ```

      

      **<u>Resultado</u>**: (Mostramos solo el primer resultado devuelto)

      ```json
      {
          "took": 2,
          "timed_out": false,
          "_shards": {
              "total": 1,
              "successful": 1,
              "skipped": 0,
              "failed": 0
          },
          "hits": {
              "total": {
                  "value": 154,
                  "relation": "eq"
              },
              "max_score": 1.0,
              "hits": [
                  {
                      "_index": "employees",
                      "_type": "_doc",
                      "_id": "macRmIwB_L614K24A7RL",
                      "_score": 1.0,
                      "_source": {
                          "FirstName": "MILAGRO",
                          "LastName": "CUTCHINS",
                          "Designation": "Senior Software Engineer",
                          "Salary": "67000",
                          "DateOfJoining": "2007-01-25",
                          "Address": "8772 Wentworth Dr. Mcallen, TX 78501",
                          "Gender": "Female",
                          "Age": 33,
                          "MaritalStatus": "Unmarried",
                          "Interests": "Socializing with friends/neighbors,Wrestling,Pyrotechnics,Collecting Swords,Soap Making,Piano,Marksmanship"
                      }
                  }
              ]
          }
      }
      ```



- 11. ##### Obtener el número de hombres y mujeres interesad@s en `Wrestling`.[Revisa la documentación sobre term aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-terms-aggregation.html)

      La agregación `terms` en Elasticsearch es una de las más comunes y útiles para crear *buckets* de documentos basados en los términos únicos de un campo específico. Esencialmente, esta agregación permite agrupar datos según los valores distintos de un campo y contar cuántos documentos hay en cada grupo.

      **Funcionamiento Básico**:

      - **Agrupa Documentos:** La agregación `terms` agrupa los documentos en buckets basados en los términos únicos encontrados en un campo especificado.
      - **Conteo de Documentos:** Cada bucket proporciona el conteo de cuántos documentos contienen ese término específico.
      - **Aplicaciones Comunes:** Es útil para contar la ocurrencia de términos, como categorías de productos, etiquetas de blogs, localizaciones geográficas, etc.

      

      A través del comando `GET` con las instrucciones en JSON:

      ```http
      GET {{elasticsearch-host}}/employees/_search
      ```

      ```json
      {
          "size":0, // Hacemos que el número de hits sea 0 para que solo nos muestre los valores encontrados.
          "query": {
              "multi_match": {
                  "query": "Wrestling",
                  "type": "bool_prefix",
                  "fields": [
                      "Interests"
                  ]
              }
          },
          "aggs": {
              "Generos": {
                  "terms": {
                      "field": "Gender"
                  }
              }
          }
      }
      ```

      

      **<u>Resultado</u>**:

      ```json
      {
          "took": 2,
          "timed_out": false,
          "_shards": {
              "total": 1,
              "successful": 1,
              "skipped": 0,
              "failed": 0
          },
          "hits": {
              "total": {
                  "value": 154,
                  "relation": "eq"
              },
              "max_score": null,
              "hits": []
          },
          "aggregations": {
              "Generos": {
                  "doc_count_error_upper_bound": 0,
                  "sum_other_doc_count": 0,
                  "buckets": [
                      {
                          "key": "Female",
                          "doc_count": 80
                      },
                      {
                          "key": "Male",
                          "doc_count": 74
                      }
                  ]
              }
          }
      }
      ```



- 12. ##### En base a la consulta anterior, obtener la edad media de cada grupo (grupo hombres y grupo mujeres). [Revisa la documentación sobre sub-agregaciones](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations.html) y [sobre la agregación avg](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-metrics-avg-aggregation.html)

      Las **agregaciones métricas** en Elasticsearch son operaciones que calculan una métrica única, usualmente numérica, sobre los valores de un campo determinado. Estas agregaciones son usadas para obtener información resumida, como promedios, sumas, mínimos y máximos, sobre un conjunto de datos. Las agregaciones métricas son ideales para analizar y entender mejor las características de tus datos.

      **Tipos Principales**:

      - **`avg`**: Calcula el promedio de los valores numéricos de un campo.

        Solo se aplica a campos que tienen valores numéricos (como `integer`, `float`, `long`, etc.).

      - **`sum`**: Suma todos los valores numéricos de un campo.
      
      - **`min`**: Encuentran el valor mínimo en un conjunto de valores numéricos.
      
      - **`max`**: Encuentran el valor máximo en un conjunto de valores numéricos.
      
      
      
      Las **sub-agregaciones** en Elasticsearch permiten realizar agregaciones anidadas, lo que significa que podemos aplicar una agregación a los *buckets* creados por otra agregación. Esta capacidad es extremadamente útil para realizar análisis de datos complejos, ya que te permite desglosar los datos en categorías más específicas y realizar cálculos adicionales dentro de cada categoría.
      
      **Funcionamiento**:
      
      1. **Crear *Buckets* con una Agregación Primaria**: Empezamos con una agregación (como `terms`, `date_histogram`, `range`, etc.) que divide los datos en *buckets*.
      2. **Aplicar Agregaciones Adicionales a Cada *Bucket***: Dentro de cada *bucket*, podemos aplicar una o más sub-agregaciones para calcular métricas como promedios (`avg`), sumas (`sum`), mínimos (`min`), máximos (`max`), etc., o incluso crear *sub-buckets* adicionales.
      
      
      
      A través del comando `GET` con las instrucciones en JSON:
      
      ```http
      GET {{elasticsearch-host}}/employees/_search
      ```
      
      ```json
      {
          "size":0,
          "query": {
              "multi_match": {
                  "query": "Wrestling",
                  "type": "bool_prefix",
                  "fields": [
                      "Interests"
                  ]
              }
          },
          "aggs": {
              "Generos": {
                  "terms": {
                      "field": "Gender"
                  },
                  "aggs": {
                      "Edad Media": {
                          "avg": {
                              "field": "Age"
                          }
                      }
                  }
              }
          }
      }
      ```
      
      
      
      **<u>Resultado</u>**:
      
      ```json
      {
          "took": 2,
          "timed_out": false,
          "_shards": {
              "total": 1,
              "successful": 1,
              "skipped": 0,
              "failed": 0
          },
          "hits": {
              "total": {
                  "value": 154,
                  "relation": "eq"
              },
              "max_score": null,
              "hits": []
          },
          "aggregations": {
              "Generos": {
                  "doc_count_error_upper_bound": 0,
                  "sum_other_doc_count": 0,
                  "buckets": [
                      {
                          "key": "Female",
                          "doc_count": 80,
                          "Edad Media": {
                              "value": 30.65
                          }
                      },
                      {
                          "key": "Male",
                          "doc_count": 74,
                          "Edad Media": {
                              "value": 30.33783783783784
                          }
                      }
                  ]
              }
          }
      }
      ```



- 13. ##### Obtener el número de empleados en función de los siguientes tramos de salario: menor de 60.000 dólares (``tramo 1``), entre 60.000 dólares y 67.000 dólares (``tramo 2``) y superior a 67.000 dólares (``tramo 3``). [Revisa la documentación sobre range aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-range-aggregation.html)

      Las **agregaciones de rango (`range aggregations`)** en Elasticsearch permiten agrupar documentos en diferentes *buckets* basándose en rangos de valores numéricos, fechas u otros tipos de datos que admitan un orden. Son muy útiles para entender cómo se distribuyen los datos a través de diferentes intervalos.
      
      **Funcionamiento de las Agregaciones de Rango**:
      
      1. **Definir Rangos**: Especificar los rangos para los cuales se quiere agrupar los documentos. Cada rango define un *bucket*.
      2. **Aplicar a Campos Adecuados**: Pueden ser aplicadas a campos numéricos, de fecha, o cualquier otro tipo de campo que tenga un sentido de orden.
      3. **Resultados en *Buckets***: Cada *bucket* contiene todos los documentos cuyos valores en el campo especificado caen dentro del rango definido.
      
      
      
      A través del comando `GET` con las instrucciones en JSON:
      
      ```http
      GET {{elasticsearch-host}}/employees/_search
      ```
      
      ```json
      {
          "size":0,
          "aggs": {
              "Rangos de salario": {
                  "range": {
                      "field": "Salary",
                      "ranges": [
                          {"key": "Menor a 60000", "to": 60000},
                          {"key": "Entre 60000 y 67000", "from": 60000, "to": 67000},
                          {"key": "Superior a 67000", "from": 67000}
                      ]
                  }
              }
          }
      }
      ```
      
      
      
      **<u>Resultado</u>**:
      
      ```json
      {
          "took": 2,
          "timed_out": false,
          "_shards": {
              "total": 1,
              "successful": 1,
              "skipped": 0,
              "failed": 0
          },
          "hits": {
              "total": {
                  "value": 9999,
                  "relation": "eq"
              },
              "max_score": null,
              "hits": []
          },
          "aggregations": {
              "Rangos de salario": {
                  "buckets": [
                      {
                          "key": "Menor a 60000",
                          "to": 60000.0,
                          "doc_count": 3872
                      },
                      {
                          "key": "Entre 60000 y 67000",
                          "from": 60000.0,
                          "to": 67000.0,
                          "doc_count": 4020
                      },
                      {
                          "key": "Superior a 67000",
                          "from": 67000.0,
                          "doc_count": 2107
                      }
                  ]
              }
          }
      }
      ```



- 14. ##### En base a la consulta anterior, para cada tramo, hallar el número de empleados que están casados y no casados.

      Para realizar esta consulta realizaremos una **sub-agregaciónes** a la consulta anterior.
      
      
      
      A través del comando `GET` con las instrucciones en JSON:
      
      ```http
      GET {{elasticsearch-host}}/employees/_search
      ```
      
      ```json
      {
          "size":0,
          "aggs": {
              "Rangos de salario": {
                  "range": {
                      "field": "Salary",
                      "ranges": [
                          {"key": "Menor a 60000", "to": 60000},
                          {"key": "Entre 60000 y 67000", "from": 60000, "to": 670000},
                          {"key": "Superior a 67000", "from": 67000}
                      ]
                  },
                  "aggs": {
                      "Estado civil": {
                          "terms": {
                              "field": "MaritalStatus"
                          }
                      }
                  }
              }
          }
      }
      ```
      
      
      
      **<u>Resultado</u>**:
      
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
                  "value": 9999,
                  "relation": "eq"
              },
              "max_score": null,
              "hits": []
          },
          "aggregations": {
              "Rangos de salario": {
                  "buckets": [
                      {
                          "key": "Menor a 60000",
                          "to": 60000.0,
                          "doc_count": 3872,
                          "Estado civil": {
                              "doc_count_error_upper_bound": 0,
                              "sum_other_doc_count": 0,
                              "buckets": [
                                  {
                                      "key": "Unmarried",
                                      "doc_count": 1945
                                  },
                                  {
                                      "key": "Married",
                                      "doc_count": 1927
                                  }
                              ]
                          }
                      },
                      {
                          "key": "Entre 60000 y 67000",
                          "from": 60000.0,
                          "to": 670000.0,
                          "doc_count": 6127,
                          "Estado civil": {
                              "doc_count_error_upper_bound": 0,
                              "sum_other_doc_count": 0,
                              "buckets": [
                                  {
                                      "key": "Married",
                                      "doc_count": 3095
                                  },
                                  {
                                      "key": "Unmarried",
                                      "doc_count": 3032
                                  }
                              ]
                          }
                      },
                      {
                          "key": "Superior a 67000",
                          "from": 67000.0,
                          "doc_count": 2107,
                          "Estado civil": {
                              "doc_count_error_upper_bound": 0,
                              "sum_other_doc_count": 0,
                              "buckets": [
                                  {
                                      "key": "Married",
                                      "doc_count": 1071
                                  },
                                  {
                                      "key": "Unmarried",
                                      "doc_count": 1036
                                  }
                              ]
                          }
                      }
                  ]
              }
          }
      }
      ```



### Parte VI) Crear otro índice y modificar el alias
- 1. Crea un nuevo índice de la misma forma que hiciste al principio, pero ahora llámalo `employees-v2` mete en él todos los datos del fichero de prueba. Modifica el alias ``employees-alias`` que creaste antes para que apunte tanto al índice ``employees`` original como al nuevo ``employees-v2``. Puedes comprobar que lo has hecho correctamente ejecutando la operación "Obtener todos los alias" de la colección de Postman.

     

     - Creamos el nuevo índice `employees-v2`:

       ```http
       PUT {{elasticsearch-host}}/employees-v2
       ```

       ```json
       {
          "mappings":{
             "properties":{
                "Address":{
                   "type":"search_as_you_type"
                },
                "Age":{
                   "type":"integer"
                },
                "DateOfJoining":{
                   "type":"date",
                   "format":"yyyy-MM-dd"
                },
                "Designation":{
                   "type":"keyword"
                },
                "FirstName":{
                   "type":"text"
                },
                "Gender":{
                   "type":"keyword"
                },
                "Interests":{
                   "type":"text"
                },
                "LastName":{
                   "type":"text"
                },
                "MaritalStatus":{
                   "type":"keyword"
                },
                "Salary":{
                   "type":"double"
                }
             }
          }
       }
       ```

       

     - Inserción de datos en el nuevo índice `employees-v2`:

       Para realizar la inserción de datos en el nuevo índice, lo primero que debemos realizar es la modificación del fichero [EmployeesV2_raw](Elasticsearch\elasticsearch-operations-postman\EmployeesV2_raw.json) para indicarle en que índice debe insertar los datos:
       
       ```json
       {"index":{"_index":"employees-v2"}}
       ```
       
       
       
       Seguidamente ejecutaremos el código desde el terminal:
       
       ```c
       curl -XPUT 'https://3j4sixnnv0:plpjsn00jd@unir-cluster-4875094071.eu-west-1.bonsaisearch.net:443/_bulk' --data-binary @EmployeesV2_raw.json -H 'Content-Type: application/json'
       ```
       
       
       
     - Modificamos el alias `employees-alias` para que apunte a los índices `employees` y `employees-v2`.
     
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
           },
           {
               "add": {
               "index": "employees-v2",
               "alias": "employees-alias"
               }
           }
         ]
       }
       ```
       
       
       
     - Comprobamos que el alias se ha modificado correctamente:
     
       ```http
       GET {{elasticsearch-host}}/_alias
       ```
     
       
       
       **<u>Resultado</u>**:
       
       ```json
       {
           "employees-v2": {
               "aliases": {
                   "employees-alias": {}
               }
           },
           "employees": {
               "aliases": {
                   "employees-alias": {}
               }
           }
       }
       ```
       

  

- 2. Realiza alguna de las consultas anteriores. ¿Qué observas?

     - **Empleados cuyo puesto es "Software Engineer"**:
     
       ```http
       GET {{elasticsearch-host}}/employees-alias/_search
       ```
     
       ```json
       {
           "size": 0,
           "query": {
               "term": {
                   "Designation": {
                       "value": "Software Engineer"
                   }
               }
           }
       }
       ```
     
       
     
       **<u>Resultado actual</u>**:
     
       ```json
       {
           "took": 1,
           "timed_out": false,
           "_shards": {
               "total": 2,
               "successful": 2,
               "skipped": 0,
               "failed": 0
           },
           "hits": {
               "total": {
                   "value": 8528,
                   "relation": "eq"
               },
               "max_score": null,
               "hits": []
           }
       }
       ```
     
       
     
       **<u>Resultado anterior</u>**:
     
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
               "max_score": null,
               "hits": []
           }
       }
       ```
     
       
       
       Observamos como cuando realizamos una consulta en Elasticsearch que apunta a más de un índice, se ejecuta la consulta en todos los índices a los que hace referencia el alias.
       
       En nuestro caso, como el alias apuntaba a dos índices `employees` y `employees-v2`, la consulta se realiza sobre estos dos índices, lo que conlleva que la consulta se realice en los *shards* de los índices, en nuestro caso 2.
       
       Esto se puede observar linea 5 del código del resultado.
     
     

- 3. Elimina ``employees`` del conjunto de índices a los que hace referencia el alias.

     ```http
     POST {{elasticsearch-host}}/_aliases
     ```
     
     
     
     **<u>Resultado</u>**:
     
     ```json
     {
       "actions": [
         { "remove": {
             "index": "employees",
             "alias": "employees-alias"
             }
         }
       ]
     }
     ```
     
     
     
     Comprobamos que el alias se ha eliminado correctamente:
     
     ```http
     GET {{elasticsearch-host}}/_alias
     ```
     
     
     
     **<u>Resultado</u>**:
     
     ```json
     {
         "employees-v2": {
             "aliases": {
                 "employees-alias": {}
             }
         },
         "employees": {
             "aliases": {}
         }
     }
     ```
