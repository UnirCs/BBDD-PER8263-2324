# Consultas con Elasticsearch _ Sharon Ursula De Lille
Unir - BBDDA

## 1. Ejercicio

### Parte I) Generar un alias
- 1) Genera un alias para el indice employees, que se llamará ``employees-alias``. A partir de ahora realizaremos las consultas siempre sobre este alias y no sobre el índice original.
```SSH
curl --location 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/_aliases' \
--header 'Content-Type: application/json' \
--data '{
    "actions": [
        {
            "add": {
                "index": "employees",
                "alias": "employees-alias"
            }
        }
    ]
}'
```

### Parte II) Inserción de elementos
- 1) Inserta un nuevo elemento en el índice utilizando tus datos (puedes inventartelos si lo consideras). Guarda el ID de documento que has obtenido tras la creacion del elemento.
```SSH
curl --location 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_doc' \
--header 'Content-Type: application/json' \
--data '{
   "FirstName":"SHARON URSULA",
   "LastName":"DE LILLE",
   "Designation":"Student",
   "Salary":"50000",
   "DateOfJoining":"2023-12-29",
   "Address":"8445 Green Street Morristown, NJ 07960",
   "Gender":"FEMALE",
   "Age":33,
   "MaritalStatus":"Married",
   "Interests":"R/C Boats,Dolls,Cloud Watching,Animals/pets/dogs,Crocheting,Casino Gambling"
}'
```
Id de documento:     ``j6DxtIwBqsuFDNbW8x4L``

### Parte III) Obtención simple de elementos
- 1) Utilizando el ID del paso anterior, obtén los datos de tu registro. Deberías obtener lo mismo que anteriormente escribiste.
```SSH
curl --location 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_doc/j6DxtIwBqsuFDNbW8x4L'
```
Datos de Postman: 	FirstName: ``SHARON URSULA``
					LastName: ``DE LILLE``

### Parte IV) Eliminación de elementos
- 1) Elimina el elemento que has creado anteriormente.
```SSH
curl --location --request DELETE 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_doc/j6DxtIwBqsuFDNbW8x4L'
```
Postman repetir parte III: ``"found": false``

### Parte V) Consultas
- 1) Obtener empleados cuyo puesto sea ``Software Engineer``. [Revisa la documentación sobre term queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-term-query.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
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

- 2) Obtener empleados cuyo puesto NO sea ``Software Engineer``. [Revisa la documentación sobre bool queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-bool-query.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "bool": {
            "must_not": {
                "term": {"Designation" : "Software Engineer"}
            }
        }
    }
}'
```

- 3) Obtener la primera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos. [Revisa la documentación sobre paginación](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/paginate-search-results.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "from": 0,
    "size": 35,
    "query": {
        "term": {
            "Designation": {
                "value": "Software Engineer"
            }
        }
    }
}'
```

- 4) Obtener la tercera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos.
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "from": 70,
    "size": 35,
    "query": {
        "term": {
            "Designation": {
                "value": "Software Engineer"
            }
        }
    }
}'
```

- 5) Obtener los primeros 13 empleados cuyo salario sea mayor a 67.000 dólares. [Revisa la documentación sobre range queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-range-query.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size": 13,
    "query": {
        "range": {
            "Salary": {
                "gt": 67000
            }
        }
    }
}'
```

- 6) Obtener <b> el número total </b> que hayan entrado en la empresa en el mes de Mayo del año 2003. No se pide una consulta específica, solo saber el número total de hits.
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size": 0,
    "query": {
        "range": {
            "DateOfJoining": {
                "gte": "2003-05-01",
                "lt": "2003-06-01"
            }
        }
    }
}

'
```
<b>Respuesta postman: </b> 
``"hits": {"total": {"value": 8,``
			
- 7) Obtener empleados cuyo nombre sea ``NATALIE``. [Revisa la documentación sobre match queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-match-query.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "FirstName":  "NATALIE"
        }
    }
}

'
```

- 8) Obtener empleados cuya dirección sea o contenga ``Street``. [Revisa la documentación sobre queries sobre campos search-as-you-type](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-as-you-type.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "Address": "Street"
        }
    }
}
'
```

- 9) Obtener empleados cuya dirección sea o contenga ``wood``.
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "Address": "wood"
        }
    }
}
'
```

- 10) Obtener empleados interesados en ``Wrestling``.
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "Interests": "Wrestling"
        }
    }
}'
```

- 11) Obtener el número de hombres y mujeres interesad@s en ``Wrestling``.[Revisa la documentación sobre term aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-terms-aggregation.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size": 0,
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
    }
}'
```

- 12) En base a la consulta anterior, obtener la edad media de cada grupo (grupo hombres y grupo mujeres). [Revisa la documentación sobre sub-agregaciones](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations.html) y [sobre la agregación avg](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-metrics-avg-aggregation.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size": 0,
    "query": {
        "match": {
            "Interests": "Wrestling"
        }
    },
    "aggs": {
        "Género": {
            "terms": {
                "field": "Gender"
            },
            "aggs": {
                "edadMedia": {
                    "avg": {
                        "field": "Age"
                    }
                }
            }
        }
    }
}'
```

- 13) Obtener el número de empleados en función de los siguientes tramos de salario: menor de 60.000 dólares (``tramo 1``), entre 60.000 dólares y 67.000 dólares (``tramo 2``) y superior a 67.000 dólares (``tramo 3``). [Revisa la documentación sobre range aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-range-aggregation.html)
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size":0,
    "aggs": {
        "salary_ranges": {
            "range": {
                "field": "Salary",
                "ranges": [
                    { "to": 60000, "key": "tramo 1" },
                    { "from": 60000, "to": 67000, "key": "tramo 2" },
                    { "from": 67000, "key": "tramo 3" }
                ]
            }
        }
    }
}
'
```

- 14) En base a la consulta anterior, para cada tramo, hallar el número de empleados que están casados y no casados.
```SSH
curl --location --request GET 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size": 0,
    "aggs": {
        "salary_ranges": {
            "range": {
                "field": "Salary",
                "ranges": [
                    { "to": 60000, "key": "tramo 1" },
                    { "from": 60000, "to": 67000, "key": "tramo 2" },
                    { "from": 67000, "key": "tramo 3" }
                ]
            },
            "aggs": {
                "Casado_o_no": {
                    "terms": {
                        "field": "MaritalStatus"
                    }
                }
            }
        }
    }
}'
```

### Parte VI) Crear otro índice y modificar el alias
- 1) Crea un nuevo índice de la misma forma que hiciste al principio, pero ahora llámalo ``employees-v2`` y mete en él todos los datos del fichero de prueba. 
<b>Creación de nuevo índice</b>
```SSH
curl --location --request PUT 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees-v2' \
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
<b>Meter datos del fichero</b>
Reemplazo en el fichero lo siguiente: ``{"index":{"_index":"employees-v2"}}``
```SSH
curl -XPUT "https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/_bulk" --data-binary @Employees_raw.json -H "Content-Type: application/json"
```
<b>Modificar alias</b>
Modifica el alias ``employees-alias`` que creaste antes para que apunte tanto al índice ``employees`` original como al nuevo ``employees-v2``. 
```SSH
curl --location 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/_aliases' \
--header 'Content-Type: application/json' \
--data '{
    "actions": [
        {
            "add": {
                "index": "employees-v2",
                "alias": "employees-alias"
            }
        }
    ]
}'
```
Puedes comprobar que lo has hecho correctamente ejecutando la operación "Obtener todos los alias" de la colección de Postman.
```SSH
curl --location 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/_alias' \
--data ''
```

- 2) Realiza alguna de las consultas anteriores. ¿Qué observas?
Oberservo que se busca en ambos índices. Postman lo muestra con: ``"_shards": {"total": 2,``

- 3) Elimina ``employees`` del conjunto de índices a los que hace referencia el alias.
```SSH
curl --location --request DELETE 'https://6dqxxoxvxa:ubgi4jpba0@unir-cluster-9429558659.us-east-1.bonsaisearch.net:443/employees'
```