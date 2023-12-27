## Operaciones con Elasticsearch - Postman
En este repositorio encontrarás una colección de Postman con varias operaciones predefinidas para ejecutar sobre un clúster de Elasticsearch creado con [Bonsai.io](https://app.bonsai.io/).

Recuerda que deberás crear una variable de colección en Postman llamada ``elasticsearch-host`` y asignarle el host de tu clúster. Puedes obtener este valor en Bonsai.io (Access -> Credentials -> Full Access)

Para crear un índice con datos de prueba, sigue estos pasos:

1) Ejecuta, desde la consola de Bonsai.io la operación ``PUT /employees`` con el siguiente cuerpo:
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
2) Introduce datos de prueba. Puedes usar el fichero [Employees.raw](https://github.com/UnirCs/elasticsearch-operations-postman/blob/master/Employees_raw.json) con datos de empleados de una compañía (datos modificados de [esta fuente](http://ikeptwalking.com/elasticsearch-sample-data/). Siéntente libre de modificar todo lo que consideres. La carga la puedes realizar con el siguiente comando (recuerda ejecutar el comando en el mismo directorio en el que se encuentre el fichero e incluir el host de tu clúster):

    (Para Unix)
    ```
    curl -XPUT '<<host_obtenido_de_bonsai>>/_bulk' --data-binary @Employees_raw.json -H 'Content-Type: application/json'
    ```
    
    (Para Windows)
    ```
    curl -XPUT "<<host_obtenido_de_bonsai>>/_bulk" --data-binary @Employees_raw.json -H "Content-Type: application/json"
    ```
  3) Nos aseguramos de que hemos cargado todos los datos ejecutando desde la consola de Bonsai.io la operación ``/employees/_count?pretty`` que nos debería indicar que hay 9.999 registros almacenados. ¡Estamos listos para comenzar a jugar!
