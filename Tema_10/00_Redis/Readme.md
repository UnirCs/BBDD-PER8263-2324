# Implementación de una caché con Redis y Spring Boot

Con este ejercicio buscamos hacer uso de una caché en un microservicio que está fuertemente orientado a consultas.

Hablamos del que implementamos en ejercicio opcional del tema 5, que permitía obtener información sobre empleados de una empresa.

Si deseas ir más allá del ejercicio y quieres aprender más sobre Redis, puedes visitar [este artículo de Medium](https://medium.com/simform-engineering/spring-boot-caching-with-redis-1a36f719309f).

## 1. Ejercicio

### Parte I) Instalación de Redis
- 1) Lo primero que harás será descargar la [imagen oficial de Redis](https://hub.docker.com/_/redis). Para ello, ejecuta el siguiente comando:
   ```
   docker pull redis
   ```
  
- 2) Una vez descargada la imagen, crea un contenedor con ella. Para ello, ejecuta el siguiente comando:
    ```
    docker run --name redis-bbdda -p 6379:6379 -d redis
    ```

### Parte II) Incluir la dependencia de Redis en el microservicio
- 1) Lo siguiente que haremos será incluir el starter de Spring Boot para Redis. Para ello, añade la siguiente dependencia en el archivo pom.xml de tu proyecto (nota que no indicamos versión porque se obtiene del Parent POM):
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    ```

### Parte III) Configuración del microservicio para que use Redis
- 1) Debemos modificar el fichero yml de configuración del microservicio. Concretamente, añadiremos la siguiente configuración bajo la clave ``spring``:

    ```yml
      cache:
        type: redis
        host: localhost
        port: 6379
        redis:
          time-to-live: 60000
    ```
    Con esto, indicamos que el microservicio usará Redis como caché, que la caché está en el mismo host que el microservicio y que el puerto en el que escucha es el 6379. Además, indicamos que el tiempo de vida de los elementos de la caché es de 60.000 milisegundos (1 minuto). El código completo del fichero ``application.yml`` debería ser el siguiente:

    ```yml
        ## Configuracion de Spring
        spring:
          cache:
            type: redis
            host: localhost
            port: 6379
            redis:
              time-to-live: 60000
          application:
            name: bbdda-spring-data
          datasource:
            url: ${DB_URL:jdbc:mysql://localhost:3306/employees} #Actualizar con el valor de tu despliegue local de MySQL.
            driver-class-name: ${DB_DRIVER:com.mysql.cj.jdbc.Driver}
            username: ${DB_USER:root} #Actualizar con el valor de tu usuario.
            password: ${DB_PASSWORD:mysql} #Actualizar con el valor de tu contraseña de root.
          jpa:
            show-sql: true
            hibernate:
              ddl-auto: update

        ## Configuracion de Instancia
        server:
          port: 8088
    ```
  Lo siguiente que debemos hacer es incluir la anotación @EnableCaching en la clase principal de la aplicación. En nuestro caso, la clase ``BbddaSpringDataApplication``. Con esto, indicamos a Spring que debe habilitar el uso de caché en la aplicación.
    ```java
        @SpringBootApplication
        @EnableCaching
        public class BBDDASpringDataApplication {

        	/**
        	 * Método principal de la aplicación.
        	 * @param args - argumentos de la aplicación.
        	 */
        	public static void main(String[] args) {
        		SpringApplication.run(BBDDASpringDataApplication.class, args);
        	}
        }
    ```
  Además, haremos que la clase Department implemente la interfaz Serializable. Esto es necesario para que Spring pueda [serializar](https://hazelcast.com/glossary/serialization/) los objetos de tipo Department y almacenarlos en Redis.
    ```java
        @Entity
        @Table(name = "departments")
        public class Department implements Serializable {
        ...
        }
    ```

### Parte IV) Uso de la anotación @Cacheable

- 1) Crea un nuevo controlador llamado ``CachedDepartmentController``. Dentro de él volveremos a implementar el método getDepartmentByName pero modificando ligeramente la firma de la operación:
    ```java
  @GetMapping("/api-cached/departments/{name}")
    @Cacheable(value = "department", key = "#deptName")
    public Department getDepartmentByName(@PathVariable("name") String deptName) {
        return departmentRepository.findByDeptName(deptName).orElse(null);
    }
  ``` 
  Con esto, indicamos que el método ``getDepartmentByName`` debe almacenar en caché el resultado de la operación. Además, indicamos que el nombre de la caché es ``department`` y que la clave que se usará para almacenar el resultado es el nombre del departamento. De esta forma, si se realiza una petición con el nombre de un departamento que ya se ha consultado anteriormente, se devolverá el resultado almacenado en caché en lugar de realizar la consulta a la base de datos.

- 2) Desde Postman, realiza varias peticiones al método ``getDepartmentByName``. Comprueba que la primera vez que se realiza la petición, el tiempo de respuesta es mayor que las siguientes veces. Esto es debido a que la primera vez que se realiza la petición, se realiza la consulta a la base de datos y se almacena el resultado en caché. Las siguientes veces, se devuelve el resultado almacenado en caché, por lo que el tiempo de respuesta es notablemente menor.

- 3) Inspecciona la caché de Redis. Para ello, ejecuta el siguiente comando:
    ```
    docker exec -it redis-bbdda redis-cli
    ```
    Una vez dentro de la consola de Redis, ejecuta el siguiente comando:
    ```
    keys *
    ```
    Con esto, obtendrás el listado de claves almacenadas en Redis. Deberías ver una clave con el nombre ``department::nombreDepartamento``. Para obtener el valor almacenado en la clave, ejecuta el siguiente comando:
    ```
    get "department::nombreDepartamento"
    ```
    Deberías obtener un resultado serializado. Es el resultado de serializar el Department que se consumió en la petición.

## 2. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_10``. Deberás incluir :

- Una carpeta ``redis`` con el código de la implementación del microservicio modificado usando Redis.