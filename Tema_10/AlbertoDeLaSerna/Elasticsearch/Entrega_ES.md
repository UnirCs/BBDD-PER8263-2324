# Actividad Elasticsearch

## Carga de índices y datos de `employees`

**Carga de índices de employees**

Desde la consola de `app.bonsai.io` he ejecutado la siguiente operación para la creación de los índices.

La operación es de tipo: `PUT /employees`

``` JSON
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
Una vez finalizada la operación retorna el siguiente resultado:

``` JSON
{
  "acknowledged": true,
  "shards_acknowledged": true,
  "index": "employees"
}
```

**Carga de datos de prueba `employees`**

Primero tengo que descargar el fichero `Employees.raw` y en el terminal lanzar el comando: `curl -XPUT '<<host_obtenido_de_bonsai>>/_bulk' --data-binary @Employees_raw.json -H 'Content-Type: application/json'`

Reemplazo `<<host_obtenido_de_bonsai>>` por el que tengo descrito en el apartado de `ACCESS/Credentials`.

Accedo a la terminal de mi MAC y lanzo el comando.

Ya puedo observar que en el apartado `data` tengo ocupado `416/125MB`.

Lanzo en la consola la operación `/employees/_count?pretty` y veo que me devuelve el resultado indicado:

``` JSON
{
  "count": 9999,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  }
}
```

También he cargado en Postman el proyecto con las operaciones básicas. Me he creado un `Environment` para tener en una variable de entorno el host de elasticsearch de bonsai. La variable se llama: `elasticsearch-host`.


## 1. Ejercicio

<b> Para cada operación solicitada, incluye el comando cURL que se obtiene de Postman </b> en un archivo Entrega_ES.md

### Parte I) Generar un alias
- 1) Genera un alias para el indice employees, que se llamará `employees-alias`. A partir de ahora realizaremos las consultas siempre sobre este alias y no sobre el índice original.

Información del CURL:

``` SSH
curl --location 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/_aliases' \
--header 'Content-Type: application/json' \
--data '{
  "actions" : [
    { "add" : { "index" : "employees", "alias" : "employees-alias" } }
  ]
}'
```

Resultado desde Postman:

``` JSON
{
    "acknowledged": true
}
```


### Parte II) Inserción de elementos
- 1) Inserta un nuevo elemento en el índice utilizando tus datos (puedes inventartelos si lo consideras). Guarda el ID de documento que has obtenido tras la creacion del elemento.

Información del CURL:

``` SSH
curl --location 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_doc' \
--header 'Content-Type: application/json' \
--data '{
   "FirstName":"ALBERTO",
   "LastName":"DE LA SERNA PARADA",
   "Designation":"Software Architect",
   "Salary":"1000000",
   "DateOfJoining":"2014-01-13",
   "Address":"Vitoria-Gasteiz",
   "Gender":"Male",
   "Age":41,
   "MaritalStatus":"Married",
   "Interests":"R/C Boats,Dolls,Cloud Watching,Software-Architecture,Animals/pets/dogs,Crocheting"
}'
```

Resultado desde Postman:

``` JSON
{
    "_index": "employees",
    "_type": "_doc",
    "_id": "bX3_l4wBf0EPUv8hJIx8",
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
- 1) Utilizando el ID del paso anterior, obtén los datos de tu registro. Deberías obtener lo mismo que anteriormente escribiste.


Información del CURL:

``` SSH
curl --location 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_doc/bX3_l4wBf0EPUv8hJIx8'
```

Resultado desde Postman:

```JSON
{
    "_index": "employees",
    "_type": "_doc",
    "_id": "bX3_l4wBf0EPUv8hJIx8",
    "_version": 1,
    "_seq_no": 9999,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "FirstName": "ALBERTO",
        "LastName": "DE LA SERNA PARADA",
        "Designation": "Software Architect",
        "Salary": "1000000",
        "DateOfJoining": "2014-01-13",
        "Address": "Vitoria-Gasteiz",
        "Gender": "Male",
        "Age": 41,
        "MaritalStatus": "Married",
        "Interests": "R/C Boats,Dolls,Cloud Watching,Software-Architecture,Animals/pets/dogs,Crocheting"
    }
}
```


### Parte IV) Eliminación de elementos
- 1) Elimina el elemento que has creado anteriormente.

Información del CURL:

``` SSH
curl --location --request DELETE 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_doc/bX3_l4wBf0EPUv8hJIx8'
```

Resultado desde Postman:

```JSON
{
    "_index": "employees",
    "_type": "_doc",
    "_id": "bX3_l4wBf0EPUv8hJIx8",
    "_version": 2,
    "result": "deleted",
    "_shards": {
        "total": 2,
        "successful": 2,
        "failed": 0
    },
    "_seq_no": 10000,
    "_primary_term": 1
}
```

### Parte V) Consultas
- 1) Obtener empleados cuyo puesto sea ``Software Engineer``. [Revisa la documentación sobre term queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-term-query.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "term": {
            "Designation": {
                "value": "Software Engineer"
            }
        }
    }
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            ...
        ]
    }
}
```

- 2) Obtener empleados cuyo puesto NO sea ``Software Engineer``. [Revisa la documentación sobre bool queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-bool-query.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "bool": {
      "must_not": {
        "term": {
          "Designation": "Software Engineer"
        }
      }
    }
  }
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
{
    "took": 10,
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
            ...
        ]
    }
}
```

- 3) Obtener la primera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos. [Revisa la documentación sobre paginación](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/paginate-search-results.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "term": {
      "Designation": "Software Engineer"
    }
  },
  "size": 35,
  "from": 0
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
{
    "took": 34,
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
            ...
        ]
    }
}
```

- 4) Obtener la tercera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos.


Como la paginación es de 35, las dos primeras páginas irían de la [0-70], por lo tanto la tercera irá a partir de la 35.

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "term": {
      "Designation": "Software Engineer"
    }
  },
  "size": 35,
  "from": 70
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            ...
        ]
    }
}
```


- 5) Obtener los primeros 13 empleados cuyo salario sea mayor a 67.000 dólares. [Revisa la documentación sobre range queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-range-query.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "range": {
      "Salary": {
        "gt": 67000
      }
    }
  },
  "size": 13
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            "value": 1591,
            "relation": "eq"
        },
        "max_score": 1.0,
        "hits": [
            ...
        ]
    }
}
```

- 6) Obtener <b> el número total </b> que hayan entrado en la empresa en el mes de Mayo del año 2003. No se pide una consulta específica, solo saber el número total de hits.

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "range": {
      "DateOfJoining": {
        "gte": "2003-05-01",
        "lte": "2003-05-31"
      }
    }
  },
  "size": 0
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            "value": 8,
            "relation": "eq"
        },
        "max_score": null,
        "hits": []
    }
}
```

- 7) Obtener empleados cuyo nombre sea ``NATALIE``. [Revisa la documentación sobre match queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-match-query.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "FirstName": "NATALIE"
    }
  }
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
                "_id": "Vznil4wBOdImnDQlY69t",
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

- 8) Obtener empleados cuya dirección sea o contenga ``Street``. [Revisa la documentación sobre queries sobre campos search-as-you-type](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-as-you-type.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Address": "Street"
    }
  }
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            "value": 1580,
            "relation": "eq"
        },
        "max_score": 1.9266167,
        "hits": [
            ...
        ]
    }
}
```

- 9) Obtener empleados cuya dirección sea o contenga ``wood``.

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Address": "wood"
    }
  }
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            "value": 19,
            "relation": "eq"
        },
        "max_score": 6.5165105,
        "hits": [
            ...
        ]
    }
}
```

- 10) Obtener empleados interesados en ``Wrestling``.

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Interests": "Wrestling"
    }
  }
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            "value": 154,
            "relation": "eq"
        },
        "max_score": 6.399149,
        "hits": [
            ...
        ]
    }
}
```

- 11) Obtener el número de hombres y mujeres interesad@s en ``Wrestling``.[Revisa la documentación sobre term aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-terms-aggregation.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Interests": "Wrestling"
    }
  },
  "aggs": {
    "gender_count": {
      "terms": {
        "field": "Gender"
      }
    }
  },
  "size": 0
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            "value": 154,
            "relation": "eq"
        },
        "max_score": null,
        "hits": []
    },
    "aggregations": {
        "gender_count": {
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

- 12) En base a la consulta anterior, obtener la edad media de cada grupo (grupo hombres y grupo mujeres). [Revisa la documentación sobre sub-agregaciones](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations.html) y [sobre la agregación avg](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-metrics-avg-aggregation.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Interests": "Wrestling"
    }
  },
  "aggs": {
    "gender": {
      "terms": {
        "field": "Gender"
      },
      "aggs": {
        "average_age": {
          "avg": {
            "field": "Age"
          }
        }
      }
    }
  },
  "size": 0
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
            "value": 154,
            "relation": "eq"
        },
        "max_score": null,
        "hits": []
    },
    "aggregations": {
        "gender": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 0,
            "buckets": [
                {
                    "key": "Female",
                    "doc_count": 80,
                    "average_age": {
                        "value": 30.65
                    }
                },
                {
                    "key": "Male",
                    "doc_count": 74,
                    "average_age": {
                        "value": 30.33783783783784
                    }
                }
            ]
        }
    }
}
```

- 13) Obtener el número de empleados en función de los siguientes tramos de salario: menor de 60.000 dólares (``tramo 1``), entre 60.000 dólares y 67.000 dólares (``tramo 2``) y superior a 67.000 dólares (``tramo 3``). [Revisa la documentación sobre range aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-range-aggregation.html)

Información del CURL:

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "aggs": {
    "salary_ranges": {
      "range": {
        "field": "Salary",
        "ranges": [
          { "to": 60000 },             
          { "from": 60000, "to": 67000 }, 
          { "from": 67000 }             
        ]
      }
    }
  },
  "size": 0
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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
        "salary_ranges": {
            "buckets": [
                {
                    "key": "*-60000.0",
                    "to": 60000.0,
                    "doc_count": 3872
                },
                {
                    "key": "60000.0-67000.0",
                    "from": 60000.0,
                    "to": 67000.0,
                    "doc_count": 4020
                },
                {
                    "key": "67000.0-*",
                    "from": 67000.0,
                    "doc_count": 2107
                }
            ]
        }
    }
}
```

- 14) En base a la consulta anterior, para cada tramo, hallar el número de empleados que están casados y no casados.
Información del CURL:

```JSON
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
        "salary_ranges": {
            "buckets": [
                {
                    "key": "*-60000.0",
                    "to": 60000.0,
                    "doc_count": 3872,
                    "marital_status": {
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
                    "key": "60000.0-67000.0",
                    "from": 60000.0,
                    "to": 67000.0,
                    "doc_count": 4020,
                    "marital_status": {
                        "doc_count_error_upper_bound": 0,
                        "sum_other_doc_count": 0,
                        "buckets": [
                            {
                                "key": "Married",
                                "doc_count": 2024
                            },
                            {
                                "key": "Unmarried",
                                "doc_count": 1996
                            }
                        ]
                    }
                },
                {
                    "key": "67000.0-*",
                    "from": 67000.0,
                    "doc_count": 2107,
                    "marital_status": {
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
- 1) Crea un nuevo índice de la misma forma que hiciste al principio, pero ahora llámalo ``employees-v2`` y mete en él todos los datos del fichero de prueba. Modifica el alias ``employees-alias`` que creaste antes para que apunte tanto al índice ``employees`` original como al nuevo ``employees-v2``. Puedes comprobar que lo has hecho correctamente ejecutando la operación "Obtener todos los alias" de la colección de Postman.

**Creo el índice**

Información del CURL:

```SSH
curl --location --request PUT 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-v2' \
--header 'Content-Type: application/json' \
--data '{
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
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "employees_v2"
}
```

**Cargo los datos de prueba**

Para cargar los datos de prueba en ese índice, tengo que modificar en el fichero `Employees_raw.json`:
- `{"index":{"_index":"employees"}}` por `{"index":{"_index":"employees-v2"}}`

Ejecuto de nuevo el comando de carga:

```SSH
curl -XPUT 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/_bulk' --data-binary @Employees_raw.json -H 'Content-Type: application/json'
```


**Modifico el alias**

Información del CURL:

```SSH
curl --location 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/_aliases' \
--header 'Content-Type: application/json' \
--data '{
  "actions" : [
    { "add" : { "index" : "employees-v2", "alias" : "employees-alias" } }
  ]
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
{
    "acknowledged": true
}
```

**Compruebo que están los dos alias**
Información del CURL:

```SSH
curl --location 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/_alias'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
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


- 2) Realiza alguna de las consultas anteriores. ¿Qué observas?

```SSH
curl --location --request GET 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "term": {
            "Designation": {
                "value": "Software Engineer"
            }
        }
    }
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):


Nuevo resultado:
```JSON
{
    "took": 3,
    "timed_out": false,
    "_shards": {
        "total": 2,
        "successful": 2,
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
            ...
        ]
    }
}
```

Resultado anterior:
```JSON
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
            ...
        ]
    }
}
```

**¿Qué observo?**

Observo que `_shards` ha cambiado, ahora me informa de que tengo dos:

```JSON
    {
        "total": 2,
        "successful": 2,
        "skipped": 0,
        "failed": 0
    }
```


Cuando se realiza una consulta en Elasticsearch y el alias apunta a más de un índice, Elasticsearch ejecutará la consulta en todos los índices a los que apunta el alias. Por lo tanto la consulta se ha ejecutdo en dos índices diferentes, por lo tanto tiene sentido que se haya ejecutado en dos `shards` diferentes.


Entonces, el hecho de que vea "2" en los shards totales y exitosos indica que el alias está funcionando correctamente y que apunta a dos índices distintos. Cada índice puede tener uno o más shards, y la consulta se ejecuta en todos los shards de todos los índices a los que apunta el alias.


- 3) Elimina ``employees`` del conjunto de índices a los que hace referencia el alias.

Información del CURL:

```SSH
curl --location 'https://fpsmns7zkz:y6nynpica2@unir-cluster-8917584216.eu-west-1.bonsaisearch.net:443/_aliases' \
--header 'Content-Type: application/json' \
--data '{
  "actions" : [
    { "remove" : { "index": "employees", "alias": "employees-alias" } }
  ]
}'
```

Resultado desde Postman (en los hits pongo tres puntos suspensivos para que no ocupe):

```JSON
{
    "acknowledged": true
}
```

He vuelto a realizar la consulta anterior y otra vez vuelve a salir como resultado un único `shard`:


```JSON
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
            "value": 0,
            "relation": "eq"
        },
        "max_score": null,
        "hits": []
    }
}
```
