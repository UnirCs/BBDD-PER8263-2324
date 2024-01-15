# Consultas con Elasticsearch

## Preparaciónde los datos
Para la creación del índice de datos de prueba se ejecuta, desde la consola de Bonsai.io, la operación PUT `/employees` con el cuerpo que se muestra a continuación.
```
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

Se cargan los datos de empleados con el siguiente comando cURL:
```
	curl -XPUT "https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/_bulk" --ssl-no-revoke --data-binary @Employees_raw.json -H "Content-Type: application/json"
```

Y comprueban que, efectivamente, están todos cargados:
```
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

## Parte I) Generar un alias
Comando cURL ejecutado desde Postman:
```
curl --location 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/_aliases' \
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

Resultado obtenido desde Postman:
```{
    "acknowledged": true
}
```


## Parte II) Inserción de elementos
Comando cURL ejecutado desde Postman:
```
curl --location 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees_alias/_doc' \
--header 'Content-Type: application/json' \
--data '{
   "FirstName":"ANTONIO",
   "LastName":"GALLEGO TEJADO",
   "Designation":"Software Architect",
   "Salary":"50000",
   "DateOfJoining":"2024-01-01",
   "Address":"Vitoria-Gasteiz",
   "Gender":"Male",
   "Age":45,
   "MaritalStatus":"Married",
   "Interests":"Software, Games, Sports, Animals, Travels"
}'
```

Resultado obtenido desde Postman:
```
{
    "_index": "employees",
    "_type": "_doc",
    "_id": "sBr9B40BAiNw_vry0ogr",
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

## Parte III) Obtención simple de elementos
Comando cURL ejecutado desde Postman:
```
curl --location 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees_alias/_doc/qkesXogB8M2vRuq9K_bD'
```

Resultado obtenido desde Postman:
```
{
    "_index": "employees",
    "_type": "_doc",
    "_id": "qkesXogB8M2vRuq9K_bD",
    "found": false
}
```

## Parte IV) Eliminación de elementos
Comando cURL ejecutado desde Postman:
```
curl --location --request DELETE 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees_alias/_doc/sBr9B40BAiNw_vry0ogr'
```

Resultado obtenido desde Postman:
```
{
    "_index": "employees",
    "_type": "_doc",
    "_id": "sBr9B40BAiNw_vry0ogr",
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

Si se realiza de nuevo la búsqueda del elemento eliminado:
```
{
  "_index": "employees",
  "_type": "_doc",
  "_id": "sBr9B40BAiNw_vry0ogr",
  "found": false
}
```

## Parte V) Consultas
- 1) Obtener empleados cuyo puesto sea ``Software Engineer``
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
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

- 2) Obtener empleados cuyo puesto NO sea ``Software Engineer``
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
         "bool": {
            "must_not": {
                "term": {
                    "Designation": {
                        "value": "Software Engineer"
                    }
                }
            }
        }
    }
}'
```

- 3) Obtener la primera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "term": {
            "Designation": {
                "value": "Software Engineer"
            }
        }
    },
    "from": 0,
    "size": 35
}'
```

- 4) Obtener la tercera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos.
La primera página iría desde la posición 0, la segunda desde la posición 35 y la tercera desde la posición 70.
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "term": {
            "Designation": {
                "value": "Software Engineer"
            }
        }
    },
    "from": 70,
    "size": 35
}'
```


- 5) Obtener los primeros 13 empleados cuyo salario sea mayor a 67.000 dólares
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "range": {
            "Salary": {
                "gt": 67000
            }
        }
    },
    "from": 0,
    "size": 13
}'
```

- 6) Obtener <b> el número total </b> que hayan entrado en la empresa en el mes de Mayo del año 2003. No se pide una consulta específica, solo saber el número total de hits
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "range": {
            "DateOfJoining": {
                "gte": "2003-05-01",
                "lte": "2023-05-31"
            }
        }
    },
    "size": 0
}'
```

- 7) Obtener empleados cuyo nombre sea ``NATALIE``
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "FirstName": "Natalie"
        }
    }
}'
```

- 8) Obtener empleados cuya dirección sea o contenga ``Street``
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "Address": "Street"
        }
    }
}'
```

- 9) Obtener empleados cuya dirección sea o contenga ``wood``
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "Address": "wood"
        }
    }
}'
```

- 10) Obtener empleados interesados en ``Wrestling``
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "Interests": "Wrestling"
        }
    }
}'
```

- 11) Obtener el número de hombres y mujeres interesad@s en ``Wrestling``
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "Interests": "Wrestling"
        }
    },
    "size": 0,
    "aggs": {
    "genderCounter": {
      "terms": {
        "field": "Gender"
      }
    }
  }
}'
```

- 12) En base a la consulta anterior, obtener la edad media de cada grupo (grupo hombres y grupo mujeres)
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "query": {
        "match": {
            "Interests": "Wrestling"
        }
    },
    "size": 0,
    "aggs": {
    "genderCounter": {
      "terms": {
        "field": "Gender"
      },
      "aggs": {
        "ageAverage": {
          "avg": {
            "field": "Age"
          }
        }
      }
    }
  }
}'
```

- 13) Obtener el número de empleados en función de los siguientes tramos de salario: menor de 60.000 dólares (``tramo 1``), entre 60.000 dólares y 67.000 dólares (``tramo 2``) y superior a 67.000 dólares (``tramo 3``)
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size": 0,
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
  }
}'
```

- 14) En base a la consulta anterior, para cada tramo, hallar el número de empleados que están casados y no casados.
```
curl --location --request GET 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_search' \
--header 'Content-Type: application/json' \
--data '{
    "size": 0,
    "aggs": {
        "salary_ranges": {
        "range": {
            "field": "Salary",
            "ranges": [
                { "to": 60000 },
                { "from": 60000, "to": 67000 }, 
                { "from": 67000 }
            ]
        },
        "aggs": {
            "marital_status_count": {
                "terms": {
                    "field" : "MaritalStatus"
                }
            }
        }
    }
  }
}'
```

## Parte VI) Crear otro índice y modificar el alias
- 1) Crea un nuevo índice de la misma forma que hiciste al principio, pero ahora llámalo ``employees-v2`` y mete en él todos los datos del fichero de prueba. Modifica el alias ``employees-alias`` que creaste antes para que apunte tanto al índice ``employees`` original como al nuevo ``employees-v2``. Puedes comprobar que lo has hecho correctamente ejecutando la operación "Obtener todos los alias" de la colección de Postman.
Comando cURL ejecutado desde Postman:
```
curl --location --request PUT 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees_v2' \
--header 'Content-Type: application/json' \
--data '		{
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
Resultado obtenido desde Postman:
```
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "employees_v2"
}
```
Se hace una copia del archivo JSON original modificando los empleados para sutituir el índice `employees` por `employees_v2` y se lanza el comando cURL de carga con el nuevo archivo.
```
curl -XPUT "https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/_bulk" --ssl-no-revoke --data-binary @Employees_v2_raw.json -H "Content-Type: application/json"
```
Resultado obtenido:
```
{
    "employees": {
        "aliases": {
            "employees-alias": {}
        }
    },
    "employees_v2": {
        "aliases": {
            "employees-alias": {}
        }
    },
    "departments": {
        "aliases": {}
    }
}
```

- 2) Realiza alguna de las consultas anteriores. **¿Qué observas?**

**RESPUESTA:**
Lo primero que he ejecutado es un *count* de los elementos que hay en el alias `employees-alias` y he podido observar que el número de registros se ha multiplicado por dos. En vez de los 9999 que teníamos inicialmente, ahora tenemos **19998**.
```
curl --location 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/employees-alias/_count?pretty'
```
Respuesta obtenida:
```
{
    "count": 19998,
    "_shards": {
        "total": 2,
        "successful": 2,
        "skipped": 0,
        "failed": 0
    }
}
```
Además, podemos observar como ahora hay 2 *shards* en vez de uno, de ahí que tengamos el doble de registros. Es decir, hemos creado creado dos *fragmentos* de la BD que, en este caso, tienen la misma información.

- 3) Elimina ``employees`` del conjunto de índices a los que hace referencia el alias.
```
curl --location 'https://3vm9dzdfa7:yxxncd21ru@unir-cluster-1018628408.eu-west-1.bonsaisearch.net:443/_aliases' \
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
