# Consultas con Elasticsearch

### Parte I) Generar un alias
curl --location 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/_aliases' \
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

### Parte II) Inserción de elementos
curl --location 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_doc' \
--header 'Content-Type: application/json' \
--data '{
    "FirstName":"Volodymyr",
    "LastName":"Bevkh",
    "Address":"Spain",
    "Age":21,
    "DateOfJoining":"2002-02-01",
    "Designation":"Student",
    "Gender":"Hombre",
    "Interests":"Databases",
    "Salary":7000
}'

### Parte III) Obtención simple de elementos
curl --location 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_doc/DBov-owBAiNw_vrynmGS'

### Parte IV) Eliminación de elementos
curl --location --request DELETE 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_doc/DBov-owBAiNw_vrynmGS'

### Parte V) Consultas
- 1) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "term": {
      "Designation": {
        "value": "Software Engineer",
        "boost": 1.0
      }
    }
  }
}'

- 2)
 curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "bool": {
        "must_not" : {
            "term" : {
            "Designation":"Software Engineer"
        }
    }
  }
  }
}'

- 3)
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size" : 35,
  "query": {
    "term": {
      "Designation": {
        "value": "Software Engineer"
      }
    }
  }
}'

- 4) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size" : 35,
  "query": {
    "term": {
      "Designation": {
        "value": "Software Engineer"
      }
    }
  }
}'

- 5) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "from" : 0,
    "size" : 13,
    "query": {
  "range": {
      "Salary": {
        "gt": 67000
      }
    }
  }
}'

- 6) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "from" : 0,
    "size" : 0,
    "query": {
  "range": {
      "DateOfJoining" : {
        "gte": "2003-05-01",    
        "lte": "2003-05-31" 
      }
    }
  }
}'

- 7) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
  "match": {
        "FirstName": "NATALIE"
      }
    }
  }
'

- 8) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "multi_match": {
            "query": "Street",
            "type": "bool_prefix",
            "fields": [
                "Address"
            ]
        }
    }
}'

- 9) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "multi_match": {
            "query": "wood",
            "type": "bool_prefix",
            "fields": [
                "Address"
            ]
        }
    }
}'

- 10) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "multi_match": {
            "query": "Wrestling",
            "type": "bool_prefix",
            "fields": [
                "Interests"
            ]
        }
    }
}'

- 11)
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
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
        "Gender": {
            "terms": {
                "field": "Gender"
            }
        }
    }
}'

- 12) 
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
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
        "Gender": {
            "terms": {
                "field": "Gender"
            },
            "aggs": {
                "Middle Ages": {
                    "avg": {
                        "field": "Age"
                    }
                }
            }
        }
    }
}'

- 13)
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size":0,
    "aggs": {
        "Rangos de salario": {
            "range": {
                "field": "Salary",
                "ranges": [
                    {"key": "menor de 60000", "to": 60000},
                    {"key": "entre 60000 y 67000", "from": 60000, "to": 67000},
                    {"key": "superior a 67000", "from": 67000}
                ]
            }
        }
    }
}'

- 14)
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size":0,
    "aggs": {
        "Rangos de salario": {
            "range": {
                "field": "Salary",
                "ranges": [
                    {"key": "menor de 60000", "to": 60000},
                    {"key": "entre 60000 y 67000", "from": 60000, "to": 67000},
                    {"key": "superior a 67000", "from": 67000}
                ]
            },
            "aggs": {
                "MaritialStatus": {
                    "terms": {
                        "field": "MaritalStatus"
                    }
                }
        }
        }
    }
}
'

### Parte VI) Crear otro índice y modificar el alias
- 1) 1.1 Crear
curl --location --request PUT 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees-v2' \
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

1.2
insertar
curl -XPUT "https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/_bulk" --data-binary @Employees_v2_raw.json -H "Content-Type: application/json"

modificar alias
curl --location 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/_aliases' \
--header 'Content-Type: application/json' \
--data '{
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
}'

comprobar alias
curl --location 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/_aliases' \
--data ''



- 2) Realiza alguna de las consultas anteriores. ¿Qué observas?
curl --location --request GET 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/employees/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "term": {
      "Designation": {
        "value": "Software Engineer",
        "boost": 1.0
      }
    }
  }
}'


Observo que la consulta se realiza sobre los 2 índices, porque el alias apunta a los dos.

- 3) Elimina ``employees`` del conjunto de índices a los que hace referencia el alias.
curl --location 'https://ainzrose4y:s2o7jow6ym@unir-cluster-8225722400.eu-west-1.bonsaisearch.net:443/_aliases' \
--header 'Content-Type: application/json' \
--data '{
  "actions": [
    { "remove": {
        "index": "employees",
        "alias": "employees-alias"
        }
    }
  ]
}'