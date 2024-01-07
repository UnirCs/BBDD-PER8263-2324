# EJERCICIO ELASTICSEARCH

<b> Para cada operación solicitada, incluye el comando cURL que se obtiene de Postman </b> en un archivo Entrega_ES.md

## Parte I) Generar un alias
- 1) Genera un alias para el indice employees, que se llamará `employees-alias`. A partir de ahora realizaremos las consultas siempre sobre este alias y no sobre el índice original.

cURL:

``` SSH
curl --location 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/_aliases' \
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



## Parte II) Inserción de elementos
- 1) Inserta un nuevo elemento en el índice utilizando tus datos (puedes inventartelos si lo consideras). Guarda el ID de documento que has obtenido tras la creacion del elemento.

cURL:

``` SSH
curl --location 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_doc' \
--header 'Content-Type: application/json' \
--data '{
   "FirstName":"XABIER MIKEL",
   "LastName":"MARTIN",
   "Designation":"Student",
   "Salary":"10000",
   "DateOfJoining":"2023-12-26",
   "Address":"8445 Green Street Morristown, NJ 07960",
   "Gender":"Male",
   "Age":41,
   "MaritalStatus":"Married",
   "Interests":"R/C Boats,Dolls,Cloud Watching,Animals/pets/dogs,Crocheting,Casino Gambling"
}'
```


## Parte III) Obtención simple de elementos
- 1) Utilizando el ID del paso anterior, obtén los datos de tu registro. Deberías obtener lo mismo que anteriormente escribiste.


cURL:

``` SSH
curl --location 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_doc/88HypowB_L614K24pQG6'
```

Resultado desde Postman:


## Parte IV) Eliminación de elementos
- 1) Elimina el elemento que has creado anteriormente.

cURL:

``` SSH
curl --location --request DELETE 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_doc/88HypowB_L614K24pQG6'
```


## Parte V) Consultas
- 1) Obtener empleados cuyo puesto sea ``Software Engineer``. [Revisa la documentación sobre term queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-term-query.html)

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Designation": "Software Engineer"
    }
  }
}'
```

- 2) Obtener empleados cuyo puesto NO sea ``Software Engineer``. [Revisa la documentación sobre bool queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-bool-query.html)

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "bool": {
      "must_not": {
        "match": {
          "Designation": "Software Engineer"
        }
      }
    }
  }
}'
```

- 3) Obtener la primera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos. [Revisa la documentación sobre paginación](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/paginate-search-results.html)

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Designation": "Software Engineer"
    }
  },
  "size": 35,        
  "from": 0          
}'
```

- 4) Obtener la tercera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos.

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Designation": "Software Engineer"
    }
  },
  "size": 35,        
  "from": 70          
}'
```

- 5) Obtener los primeros 13 empleados cuyo salario sea mayor a 67.000 dólares. [Revisa la documentación sobre range queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-range-query.html)

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "bool": {
      "filter": {
        "range": {
          "Salary": {
            "gt": 67000
          }
        }
      }
    }
  },
  "size": 13  
}'
```

- 6) Obtener <b> el número total </b> que hayan entrado en la empresa en el mes de Mayo del año 2003. No se pide una consulta específica, solo saber el número total de hits.

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
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
}'
```

- 7) Obtener empleados cuyo nombre sea ``NATALIE``. [Revisa la documentación sobre match queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-match-query.html)

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match_phrase": {
      "FirstName":"NATALIE"
    }
  }
}'
```

- 8) Obtener empleados cuya dirección sea o contenga ``Street``. [Revisa la documentación sobre queries sobre campos search-as-you-type](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-as-you-type.html)

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Address": "Street"
    }
  }
}'
```

- 9) Obtener empleados cuya dirección sea o contenga ``wood``.

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "Address": "wood"
    }
  }
}'
```

- 10) Obtener empleados interesados en ``Wrestling``.

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "multi_match": {
      "query": "Wrestling",
      "fields": ["Interests"]
    }
  }
}'
```

- 11) Obtener el número de hombres y mujeres interesad@s en ``Wrestling``.[Revisa la documentación sobre term aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-terms-aggregation.html)

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "size":0,
  "query": {
    "multi_match": {
      "query": "Wrestling",
      "fields": ["Interests"]
    }
  },
  "aggs": {
    "Genero": {
      "terms": {
        "field": "Gender"
      }
    }
  }
}'
```

- 12) En base a la consulta anterior, obtener la edad media de cada grupo (grupo hombres y grupo mujeres). [Revisa la documentación sobre sub-agregaciones](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations.html) y [sobre la agregación avg](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-metrics-avg-aggregation.html)

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "size":0,
  "query": {
    "multi_match": {
      "query": "Wrestling",
      "fields": ["Interests"]
    }
  },
  "aggs": {
    "Genero": {
      "terms": {
        "field": "Gender"
      },
        "aggs": {
             "edad_media": {
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

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "size":0,  
  "aggs": {
    "tramos_salario": {
      "range": {
        "field": "Salary",
        "ranges": [
          { "key":"Tramo 1" ,"to": 60000 },                  
          { "key":"Tramo 2" ,"from": 60000, "to": 67000 },   
          { "key":"Tramo 3" ,"from": 67001 }                 
        ]
      }
    }
  }
}'
```

- 14) En base a la consulta anterior, para cada tramo, hallar el número de empleados que están casados y no casados.
cURL:

cURL:

```SSH
curl --location --request GET 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "size":0,  
  "aggs": {
    "tramos_salario": {
      "range": {
        "field": "Salary",
        "ranges": [
           { "key":"Tramo 1" ,"to": 60000 },                  
          { "key":"Tramo 2" ,"from": 60000, "to": 67000 },   
          { "key":"Tramo 3" ,"from": 67001 }                 
        ]
      },
      "aggs": {
        "estado_civil": {
          "terms": {
            "field": "MaritalStatus"
          }
        }
      }
    }
  }
}'
```

## Parte VI) Crear otro índice y modificar el alias
- 1) Crea un nuevo índice de la misma forma que hiciste al principio, pero ahora llámalo ``employees-v2`` y mete en él todos los datos del fichero de prueba. Modifica el alias ``employees-alias`` que creaste antes para que apunte tanto al índice ``employees`` original como al nuevo ``employees-v2``. Puedes comprobar que lo has hecho correctamente ejecutando la operación "Obtener todos los alias" de la colección de Postman.

**Creo el índice**

cURL:

```SSH
curl --location --request PUT 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/employees-v2' \
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


**Cargo los datos de prueba**

Para cargar los datos de prueba en este nuevo índice, he modificado el fichero `Employees_raw.json` reemplazando `{"index":{"_index":"employees"}}` por `{"index":{"_index":"employees-v2"}}`

Ejecuto de nuevo el comando de carga:

```SSH
curl -XPUT 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/_bulk' --data-binary @Employees_raw.json -H 'Content-Type: application/json'
```


**Modifico el alias**

cURL:

```SSH
curl --location 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/_aliases' \
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

**Compruebo que están los dos alias**
cURL:

```SSH
curl --location 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/_alias'
```


- 2) Realiza alguna de las consultas anteriores. ¿Qué observas?

Observo que el campo `_shards` ha cambiado de 1 a 2. Esto quiere decir que la consulta se ha ejecutado en los 2 indices a los que apunta el alias.


- 3) Elimina ``employees`` del conjunto de índices a los que hace referencia el alias.

cURL:

```SSH
curl --location 'https://p85v0lxtou%3Ag2sjx1562w@unir-cluster-9881734872.eu-west-1.bonsaisearch.net:443/_aliases' \
--header 'Content-Type: application/json' \
--data '{
  "actions": [
    {
      "remove": {
        "index": "employees",
        "alias": "employees-alias"
      }
    }
  ]
}'
```

