Instalación de bases de datos relacionales con imágenes Docker
========================================================

En este primer ejercicio vamos a instalar dos bases de datos relacionales que utilizaremos durante la primera parte del curso.

Trabajaremos con MySQL (versión latest) y Oracle Database (versión 19c Enterprise Edition). Sigue las siguientes instrucciones para instalar en tu máquina local estas bases de datos a través de imágenes Docker.

## Requisitos previos

1. Debes [descargar Docker Desktop](https://www.docker.com/products/docker-desktop/) que incluye de por sí la instalación de Docker Engine.
2. Debes disponer de algún programa que permita visualizar bases de datos. Durante el curso utilizaremos [JetBrains DataGrip](https://www.jetbrains.com/es-es/datagrip/), uno de los mejores IDEs profesionales para trabajar con bases de datos.

## Instalación de MySQL

1. Abre una consola en tu equipo (Terminal o Power shell) y ejecuta el siguiente comando para descargar la imagen más reciente de MySQL:

        docker pull mysql

2. Una vez la imagen haya sido descargada, debemos crear un nuevo contenedor a partir de ella. Para ello, ejecuta el siguiente comando donde estaremos creando un contenedor que expondrá el puerto 3306 del propio contenedor en el puerto 3306 de nuestra máquina local. El nombre de la base de datos será ``bbdda-mysql`` y la contraseña del usuario ``root`` será ``mysql``. La versión de MySQL que se instalará será la última disponible en el momento de ejecutar el comando.

        docker run -p 3306:3306 --name bbdda-mysql -e MYSQL_ROOT_PASSWORD=mysql -d mysql:latest

3. Una vez hayamos creado el contenedor lo siguiente será conectarnos a él para crear la base de datos de prueba que utilizaremos para los ejercicios. Utilizaremos la [base de datos de empleados])https://dev.mysql.com/doc/employee/en/employees-installation.html) que ofrece MySQL como ejemplo y seguiremos las instrucciones de instalación.

   - En nuestra máquina local descargaremos todo el contenido de la rama ``master`` de [este repositorio](https://github.com/datacharmer/test_db) que contiene la base de datos de ejemplo. Podemos hacerlo a través del botón verde "Code <>" -> "Download ZIP".
   - En nuestra máquina local, descomprimiremos el archivo (en caso de haberlo descargado) de forma que tengamos un directorio llamado test_db-master.
   - En nuestra máquina local, desde una consola moveremos el contenido de la carpeta test_db-master al contenedor. Para ello, ejecutaremos el siguiente comando (en este caso se está lanzando desde la carpeta local donde se encuentra el directorio test_db-master):

            docker cp Downloads/test_db-master <<containerId>>:/employees

4. Conectaremos con la base de datos MySQL a través de DataGrip y ejecutaremos alguna consulta para comprobar que todo funciona correctamente.

5. Como curiosidad, puedes utilizar DataGrip para ver el modelo E/R de un schema.