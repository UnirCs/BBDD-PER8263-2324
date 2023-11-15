# Spring Data JPA

## 1. Introducción a Spring

Spring es un framework de desarrollo para Java que proporciona una infraestructura integral para desarrollar aplicaciones Java. Es conocido por su inyección de dependencias y su modelo de programación orientado a aspectos. Permite a los desarrolladores crear aplicaciones de alto rendimiento con una configuración simplificada y un soporte robusto para transacciones de base de datos y REST APIs. Para más detalles, consulta la [documentación oficial de Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/).


## 2. Objetivos

El propósito de este ejercicio es fortalecer la comprensión y habilidades en la creación de entidades JPA y el uso de repositorios en Spring Data JPA. Se utilizará un proyecto base que ya tiene algunas entidades y repositorios configurados.


## 3. Ejercicio

Mejorarás el esquema `Employee`, completando las entidades faltantes, ampliarás la funcionalidad de los repositorios existentes y expondrás operaciones a través de endpoints REST.

### Parte 1: Completar el Esquema `Employee`

1. **Creación de Entidades JPA**: Completar el esquema `Employee` añadiendo las entidades JPA faltantes `Salary` y `Title` con todas las anotaciones y relaciones necesarias. Esto es lo mismo que ya hicimos en el ejercicio anterior. No obstante al usar Spring Data JPA habrá cosas que ya no serán necesarias, como por ejemplo los DAOs o las definiciones de algunas columnas.

### Parte 2: Ampliación de Repositorios

2. **Métodos de Repositorio**: Implementar cinco nuevos métodos en los repositorios `EmployeeRepository` y `DepartmentRepository` (entre ambos) usando la derivación de nombres de Spring Data JPA. Tienes el enlace a la documentación oficial de Spring Data JPA en el [código de apoyo](https://github.com/UnirCs/bbdda-spring-data) y en las diapositivas.

### Parte 3: Nuevos Endpoints en Controladores

3. **Endpoints de Controlador**: Crear endpoints en `EmployeeController` y `DepartmentController` para las operaciones definidas en los repositorios. Recuerda hacer uso de Postman para probar que todo funciona correctamente. En el código de apoyo encontrarás una colección lista para importar en Postman con peticiones de ejemplo ya creadas.

## 4. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_5``. Deberás incluir :

- El código del proyecto Java utilizado para realizar el ejercicio.
