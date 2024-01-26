

# Parte I Generar un alias

curl --location '<<HOST>>/_aliases' \
--header 'Content-Type: application/json' \
--data '{
    "actions":[
        {
            "add":{
                "index": "employees",
                "alias": "employees-alias"
            
            }
        }
    ]
}'

# Parte II Inserción de elementos

Inserta un nuevo elemento en el índice utilizando tus datos (puedes inventartelos si lo consideras). Guarda el ID de documento que has obtenido tras la creacion del elemento.

curl --location '<<HOST>>/employees-alias/_doc' \
--header 'Content-Type: application/json' \
--data '{
    "FirstName": "Alberto",
    "LastName": "Acosta",
    "Designation": "Senior Software Engineer",
    "Salary": 61000,
    "DateOfJoining": "2014-01-13",
    "Address": "PUENTE",
    "Gender": "Male",
    "Age": 35,
    "MaritalStatus": "Married",
    "Interests": "c"
}'

{
    "_index": "employees",
    "_type": "_doc",
    "_id": "9P4EiowB-Gty6Sx9BJYD",
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

# Parte III  Obtención simple de elementos

curl --location --request GET '<<HOST>>/employees-alias/_doc/9P4EiowB-Gty6Sx9BJYD' \
--header 'Content-Type: application/json' \
--data '{
    "FirstName":"ALBERTO",
    "LastName":"ACOSTA",
    "Designation":"Senior Software Engineer",
    "Salary":"7000",
    "DateOfJoining":"2014-01-13",
    "Address":"8445 Green Street Morristown, NJ 07960",
    "Gender":"Female",
    "Age":35,
    "MaritalStatus":"Married",
    "Interests":"R/C Boats,Dolls,Cloud Watching,Animals/pets/dogs,Crocheting,Casino Gambling"}'

# Parte IV Eliminación de elementos

curl --location --request DELETE '<<HOST>>/employees-alias/_doc/9P4EiowB-Gty6Sx9BJYD'

# Parte V Consultas

## 1 Obtener empleados cuyo puesto sea Software Engineer
curl --location --request GET '<<HOST>>/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query":{
        "match":{
            "Designation":"Software Engineer"
         }
    }
}'

## 2 Obtener empleados cuyo puesto NO sea Software Engineer
curl --location --request GET '<<HOST>>/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query":{
        "bool": {
            "must_not": {
                 "match":{
                     "Designation": "Software Engineer"
                }
            }
        }
    }
}'
## 3 Obtener la primera página de empleados cuya designation sea Software Engineer
curl --location --request GET '<<HOST>>/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "from": 0,
  "size": 35,
  "query": {
    "match": {
      "Designation": "Software Engineer"
    }
  }
}'
## 4 Obtener la tercera página de empleados cuya designation sea Software Engineer asumiendo un tamaño de página de 35 elementos.
curl --location --request GET '<<HOST>>/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "from": 69,
  "size": 35,
  "query": {
    "match": {
      "Designation": "Software Engineer"
    }
  }
}'
## 5 Obtener los primeros 13 empleados cuyo salario sea mayor a 67.000 dólares.
curl --location --request GET '<<HOST>>/employees-alias/_search' \
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
}
'
## 6 Obtener el número total que hayan entrado en la empresa en el mes de Mayo del año 2003. No se pide una consulta específica, solo saber el número total de hits.

curl --location --request GET '<<HOST>>/employees-alias/_search' \
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
## 7 Obtener empleados cuyo nombre sea NATALIE
curl --location --request GET '<<HOST>>/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query":{
        "match":{
            "FirstName":"NATALIE"
         }
    }
}'
## 8 Obtener empleados cuya dirección sea o contenga Street
curl --location --request GET '<<HOST>>/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query":{
        "multi_match": {
            "query":"Street",
            "type":"bool_prefix",
            "fields":[
                "Address",
                "Address._2gram",
                "Address._3gram"
            ]
        }
    }
}'
## 9 Obtener empleados cuya dirección sea o contenga wood
curl --location --request GET '<<HOST>>/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query":{
        "multi_match": {
            "query":"wood",
            "type":"bool_prefix",
            "fields":[
                "Address",
                "Address._2gram",
                "Address._3gram"
            ]
        }
    }
}'
## 10 Obtener empleados interesados en Wrestling
curl --location --request GET '<<HOST>>/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query":{
        "multi_match": {
            "query":"Wrestling",
            "type":"bool_prefix",
            "fields":[
                "Interests",
                "Interests._2gram",
                "Interests._3gram"
            ]
        }
    }
}'
## 11 Obtener el número de hombres y mujeres interesad@s en Wrestling
curl --location --request GET '<<HOST>>/employees-alias-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size":0,
  "query": {
    "multi_match": {
      "query": "Wrestling",
      "type": "bool_prefix",
      "fields": [
        "Interests",
        "Interests._2gram",
        "Interests._3gram"
      ]
    }
  },
  "aggs": {
    "Generos": {
      "terms": {
        "field": "Gender.keyword"
      }
    }
  }
}
'
## 12 En base a la consulta anterior, obtener la edad media de cada grupo (grupo hombres y grupo mujeres).
curl --location --request GET '<<HOST>>/employees-alias-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size":0,
  "query": {
    "multi_match": {
      "query": "Wrestling",
      "type": "bool_prefix",
      "fields": [
        "Interests",
        "Interests._2gram",
        "Interests._3gram"
      ]
    }
  },
  "aggs": {
    "Generos": {
      "terms": {
        "field": "Gender.keyword"
      },
      "aggs": {
        "avg_age": {
          "avg": {
            "field": "Age"
          }
    }
  }
}
  }}
'
## 13 Obtener el número de empleados en función de los siguientes tramos de salario: menor de 60.000 dólares (tramo 1), entre 60.000 dólares y 67.000 dólares (tramo 2) y superior a 67.000 dólares (tramo 3)
curl --location --request GET '<<HOST>>/employees-alias-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "aggs": {
    "rangodeprecios": {
      "range": {
        "field": "Salary",
        "ranges": [
          {"key": "Tramo 1", "to": 60000 },
          { "key": "Tramo 2" ,"from": 60000, "to": 67000 },
          { "key": "Tramo 3","from": 67000  }
        ]
      }
    }
  }
}

'
## 14 En base a la consulta anterior, para cada tramo, hallar el número de empleados que están casados y no casados.
curl --location --request GET '<<HOST>>/employees-alias-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
  "aggs": {
    "rangodeprecios": {
      "range": {
        "field": "Salary",
        "ranges": [
          {"key": "Tramo 1", "to": 60000 },
          { "key": "Tramo 2" ,"from": 60000, "to": 67000 },
          { "key": "Tramo 3","from": 67000  }
        ]
      },
      "aggs": {
        "marital_status": {
          "terms": {
            "field": "MaritalStatus"
          }
        }
      }
    }
  }
}
'
## 15Respuesta a la generacion de alias correcta
{
    "peliculas": {
        "aliases": {}
    },
    ".kibana_1": {
        "aliases": {
            ".kibana": {}
        }
    },
    "employees": {
        "aliases": {
            "employees-alias": {}
        }
    },
    "employees-v2": {
        "aliases": {
            "employees-alias": {}
        }
    }
}
> Se observa que las consultas actuan sobre dos shards
{
    "took": 1,
    "timed_out": false,
    "_shards": {
        "total": 2,
        "successful": 2,
        "skipped": 0,
        "failed": 0
    },

## 16 Problemas resueltos
- Empezamos  trabajando  con texto  y no con JSON con lo que algunas consultas funcionaban y otras no, repetimos todo el ejercicio.
- La carga de datos desde CMD en Windows requiere que las comillas sean dobles.