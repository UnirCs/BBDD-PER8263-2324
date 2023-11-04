# Ejercicio Tema3 - Alberto de la Serna

## Primera parte - Elegir una tabla y realizar una inserción
- Dentro de la carpeta `model` he creado la clase OracleJob para poder mapear la tabla JOBS de la base de datos de Oracle.
- Dentro de la carpeta `write`, dentro de la clase `OracleApplication` he sobreescrito el método upsert para implementar la inserción de un `job` nuevo.
    - Dentro del método `main` he creado un objeto job y se lo he pasado al método `upsert`

## Segunda parte - Injesta masiva de datos
- he creado una archivo .csv `unirDepartments.csv` donde he creado los 15 departamentos. Los departamentos se los he solicitado a `chatGPT`, estos dos son los prompts son los que he necesitado:
    - Estoy creando un proyecto en Java para leer un archivo csv e insertarlo en una base de datos de MySql. Necesito que crees un archivo csv, que tenga cabecera y que los valores estén separados por comas. El csv tiene que ver con departamentos de una empresa tecnológica, necesito que crees al menos 15 departamentos. La tabla destino tiene esta estructura que debe respetar: create table departments
    (
        dept_no   char(4)     not null
            primary key,
        dept_name varchar(40) not null,
        constraint dept_name
            unique (dept_name)
    );
    - Vale ya existen departamentos que tienen ese ID, el ID debe comenzar en d008 incluido

- he modificado los méotdos ya existentes incluyendo el sufijo Employee, de tal manera que pueda crear los métodos correspondientes para `department`. Todo este código que voy a entregar se podría refactorizar y dejar más cool ;-). De esta manera ya he podido importar los departamentos correspondientes.
- en la actividad se pide que los empleados deben pertenecer a uno de los departamentes que nos ha dado ´chatGPT´ con anterioridad. Claro entonces el código de apoyo que nos ha ofrecido el profesor lo voy a tener que modificar para que también alimente la relación `employee - departament`, que se establece en la tabla `dept_emp`. Este es el prompt que le he solicitado a `chatGPT`:
    - Ahora necesito que crees un csv de empleados que pertenezcan a algún departamento de los que me has dado en el csv anterior. El csv debe tener información inventada, similar a :
    employeeId,firstName,lastName,gender,hireDate,birthDate, deptNo, fromDate, toDate
1000,Carla,Pérez,M,2023-02-15,1990-05-10, d008, 2023-10-01,2023-11-01

- Con ese prompt ya tengo el CSV para importar. Ahora toca modificar el código de apoyo un poco para que meta también la relación entre empleado y departamento:
    - He modificado el modelo `MySQlEmployee` para que contenga también la información del departamento y las fechas de departamento. Lo suyo hubiera sido crear un diagrama de clases con mayor integridad, pero por simplificar el ejercicio y dado que en este momento sólo interesa el alta masiva de empleados, mapeo el CSV con el modelo `MySqlEmployee` y después trabajamos las tablas `employee` y `dept_no`.


## Entrega de los ejercicios
He creado tres carpetas donde he dejado los ejercicios:
- `bbdd-java`: proyecto java con toda la nueva implementación del ejercicio 3. Los ficheros `.csv` se encuentran en el directorio raíz del proyecto. Tal y como se ha comentado en clase.
- `Pantallazos`: pantallazos de MySql y Oracle de los datos introducidos
- `Inserts_Exports`: trees ficheros `.sql` con las inserts correspondientes. En el caso de Mysql, leer csv, si quieres ejecutar las Inserts directamente en base de datos hay que hacerlo en este orden:
    - Primero `InsercionesDepartments.sql`
    - Segundo `InsercionesEmployees.sql`
    - Tercero `InsercionesDept_Emp.sql`