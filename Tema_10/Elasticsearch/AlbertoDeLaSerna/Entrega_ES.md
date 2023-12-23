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
- 1) Genera un alias para el indice employees, que se llamará ``employees-alias``. A partir de ahora realizaremos las consultas siempre sobre este alias y no sobre el índice original.

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
- 2) Obtener empleados cuyo puesto NO sea ``Software Engineer``. [Revisa la documentación sobre bool queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-bool-query.html)
- 3) Obtener la primera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos. [Revisa la documentación sobre paginación](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/paginate-search-results.html)
- 4) Obtener la tercera página de empleados cuya ``designation`` sea ``Software Engineer`` asumiendo un tamaño de página de 35 elementos.
- 5) Obtener los primeros 13 empleados cuyo salario sea mayor a 67.000 dólares. [Revisa la documentación sobre range queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-range-query.html)
- 6) Obtener <b> el número total </b> que hayan entrado en la empresa en el mes de Mayo del año 2003. No se pide una consulta específica, solo saber el número total de hits.
- 7) Obtener empleados cuyo nombre sea ``NATALIE``. [Revisa la documentación sobre match queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/query-dsl-match-query.html)
- 8) Obtener empleados cuya dirección sea o contenga ``Street``. [Revisa la documentación sobre queries sobre campos search-as-you-type](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-as-you-type.html)
- 9) Obtener empleados cuya dirección sea o contenga ``wood``.
- 10) Obtener empleados interesados en ``Wrestling``.
- 11) Obtener el número de hombres y mujeres interesad@s en ``Wrestling``.[Revisa la documentación sobre term aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-terms-aggregation.html)
- 12) En base a la consulta anterior, obtener la edad media de cada grupo (grupo hombres y grupo mujeres). [Revisa la documentación sobre sub-agregaciones](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations.html) y [sobre la agregación avg](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-metrics-avg-aggregation.html)
- 13) Obtener el número de empleados en función de los siguientes tramos de salario: menor de 60.000 dólares (``tramo 1``), entre 60.000 dólares y 67.000 dólares (``tramo 2``) y superior a 67.000 dólares (``tramo 3``). [Revisa la documentación sobre range aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-range-aggregation.html)
- 14) En base a la consulta anterior, para cada tramo, hallar el número de empleados que están casados y no casados.

### Parte VI) Crear otro índice y modificar el alias
- 1) Crea un nuevo índice de la misma forma que hiciste al principio, pero ahora llámalo ``employees-v2`` y mete en él todos los datos del fichero de prueba. Modifica el alias ``employees-alias`` que creaste antes para que apunte tanto al índice ``employees`` original como al nuevo ``employees-v2``. Puedes comprobar que lo has hecho correctamente ejecutando la operación "Obtener todos los alias" de la colección de Postman.
- 2) Realiza alguna de las consultas anteriores. ¿Qué observas?
- 3) Elimina ``employees`` del conjunto de índices a los que hace referencia el alias.

## 2. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_10``. Deberás incluir :

- Un archivo ``Entrega_ES.md`` con los comandos cURL obtenidos a través de Postman.