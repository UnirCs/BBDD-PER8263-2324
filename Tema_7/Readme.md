# Transacciones con Spring y Spring Data JPA

## 1. Introducción a Transactional

``@Transactional`` es una anotación que se incluye dentro del framework de Spring. Las operaciones que se realicen dentro de un método con esta anotación se realizarán de forma transaccional, es decir, en bloque. Para el ejercicio que vamos a realizar no es necesario utilizar los parámetros opcionales que esta anotación incluye, pero, si quieres aprender más sobre ellos puedes revisar [esta guía de Baeldung](https://www.baeldung.com/transaction-configuration-with-jpa-and-spring).

Si en algún momento de la ejecución del método anotado con ``@Transactional`` ocurre alguna exepción **no controlada** Spring hará automáticamente un rollback de todo aquello que hasta el momento se haya cambiado en la base de datos.

## 2. Objetivos

El propósito de este ejercicio es trabajar los conceptos básicos de transacciones vistos en clase y aplicarlos de forma práctica con Java y Spring.


## 3. Ejercicio

Partimos del esquema `Employee` que en el ejercicio anterior completamos, añadiendo las entidades que faltaban en el código de apoyo.

El objetivo ahora es crear un nuevo endpoint que será ``POST /api/promotions`` y que recibirá en un body la información necesaria para promocionar a un empleado.

### Parte 1: Crear nuevos repositorios

Si analizamos el esquema de MySQL veremos que para promocionar a un empleado será necesario modificar varias tablas. En el contexto de este ejercicio, una promoción implica un cambio de título, de departamento y de salario. Tendremos que modifcar, por ende, datos en las tablas ``salaries``, ``titles`` y ``dept_emp``. Dado que necesitamos modificar estas tablas es buena idea crear nuevos repositorios de Spring para ellas.

En dichos repositorios será necesario incluir métodos que más adelante utilizaremos:

  - Un método que nos permita obtener, a partir del ID de empleado únicamente, el departamento actual del trabajo del empleado.
  - Un método que nos permita obtener, a partir del ID de empleado únicamente, el salario actual del trabajo del empleado.
  - Un método que nos permita obtener, a partir del ID de empleado únicamente, el título actual del trabajo del empleado.


### Parte 2: Crear un servicio

Crearemos un nuevo paquete dentro del paquete principal ``com.unir.employees`` con el nombre ``service`` y dentro de este paquete crearemos una clase ``PromotionsService``. Esta clase contendrá la lógica de negocio de la promoción.

Dentro de esta clase, anotada con @Service (de Spring) tendremos como dependencias todos los repositorios que serán necesarios en la lógica de la promoción. Habrá un método publico que será ``promote``. Puedes copiar la firma del método si lo deseas:

```java
@Service
@RequiredArgsConstructor
public class PromotionsService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TitleRepository titleRepository;
    private final SalaryRepository salaryRepository;
    private final DeptEmptRepository deptEmpRepository;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    public String promote(PromotionRequest promotionRequest) throws ParseException {
      //Logica de negocio...
      return "Promocionado :D";
    }
}
```

Este método estará anotado con ``@Transactional`` de forma que se tratará como una transacción a nivel de actualizaciones de la base de datos. Cualquier excepción no controlada (decimos cualquiera porque no hacemos uso de ``rollbackFor``) provocará un rollback.

La entrada será un objeto de tipo ``PromotionRequest`` que incluiremos también en nuestro proyecto (en el paquete ``com.unir.employees.model.request``) y deberá ser obligatoriamente así:

```java
import lombok.Data;
import java.util.Date;

@Data
public class PromotionRequest {

    private Integer employeeId;
    private String departmentId; //Nuevo departamento
    private String title; //Nuevo titulo
    private Integer salary; //Nuevo salario
    private Date fromDate;
}
```

A continuación deberemos escribir la lógica de negocio, siguiendo estrictamente esta línea de sucesos:

  1. Obtendremos una referencia al empleado y al nuevo departamento. Si alguno de los dos no existe, el método termina dado que los datos de entrada no son correctos.
  2. Obtendremos una referencia al título, salario y departamento actual del empleado. Si alguno de los tres no existe, el método termina dado que los datos de entrada no son correctos.
  3. Actualizamos el atributo ``toDate`` del título, salario y departamento actuales con el valor recibido en el atributo ``fromDate`` de la entrada del método. Actualizamos dichos registros en la base de datos.
  4. Tras la actualización, incluiremos una validación, y es que **el salario no puede modificarse más de un 15% en una promoción**. En el caso de que esto no se cumpla se lanzará una nueva excepción, usando ``throw new RuntimeException()``.
  5. Creamos nuevos objetos que representen el nuevo título, salario y departamento del empleado. Usaremos para el atributo ``toDate`` el mismo valor que había previamente en los registros que modificamos.
  6. Guardamos los nuevos objetos en la base de datos.

### Parte 3: Exponer un endpoint

Crearemos una nueva clase controladora, ``PromotionsController`` cuyo código será el siguiente:

```java
package com.unir.employees.controller;

import com.unir.employees.model.request.PromotionRequest;
import com.unir.employees.service.PromotionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PromotionsController {

    private final PromotionsService promotionsService;

    @PostMapping("/api/promotions")
    public ResponseEntity<String> promote(@RequestBody PromotionRequest promotionRequest) {
        return ResponseEntity.ok(promotionsService.promote(promotionRequest));
    }
}

```

A partir de este momento ya puedes hacer pruebas. Busca datos en la base de datos para ejecutar de forma correcta la transacción. Luego modifica el nuevo salario introducido por API para que viole la condición impuesta en el código. Observarás que al producirse un rollback no hay cambios en ninguna entidad.

Desde postman, una petición para consumir el endpoint construido sería esta:

<img width="416" alt="Postman" src="https://github.com/UnirCs/BBDD-PER8263-2324/assets/115072043/e3477489-3288-4425-b989-76f423e2160e">

## 4. Entrega

Crea una carpeta con tu nombre y apellidos dentro de ``Tema_7``. Deberás incluir :

- El código del proyecto Java utilizado para realizar el ejercicio.
