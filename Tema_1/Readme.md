Instalación de bases de datos relacionales con imágenes Docker
========================================================

En este primer ejercicio vamos a instalar dos bases de datos relacionales que utilizaremos durante la primera parte del curso.

Trabajaremos con MySQL (versión latest) y Oracle Database (versión 19c Enterprise Edition). Sigue las siguientes instrucciones para instalar en tu máquina local estas bases de datos a través de imágenes Docker.
https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/1_docker%20modo%20cli.png
<a href="https://www.unir.net/ingenieria/grado-informatica/"></a>

# 1. Requisitos previos

1. Debes [descargar Docker Desktop](https://www.docker.com/products/docker-desktop/) que incluye de por sí la instalación de Docker Engine.
2. Debes disponer de algún programa que permita visualizar bases de datos. Durante el curso utilizaremos [JetBrains DataGrip](https://www.jetbrains.com/es-es/datagrip/), uno de los mejores IDEs profesionales para trabajar con bases de datos.

# 2. Instalación de MySQL

1. Abre una consola en tu equipo (Terminal o Power shell) y ejecuta el siguiente comando para descargar la imagen más reciente de MySQL:

        docker pull mysql

2. Una vez la imagen haya sido descargada, debemos crear un nuevo contenedor a partir de ella. Para ello, ejecuta el siguiente comando donde estaremos creando un contenedor que expondrá el puerto 3306 del propio contenedor en el puerto 3306 de nuestra máquina local. El nombre de la base de datos será ``bbdda-mysql`` y la contraseña del usuario ``root`` será ``mysql``. La versión de MySQL que se instalará será la última disponible en el momento de ejecutar el comando.

        docker run -p 3306:3306 --name bbdda-mysql -e MYSQL_ROOT_PASSWORD=mysql -d mysql:latest

3. Una vez hayamos creado el contenedor lo siguiente será conectarnos a él para crear la base de datos de prueba que utilizaremos para los ejercicios. Utilizaremos la [base de datos de empleados])https://dev.mysql.com/doc/employee/en/employees-installation.html) que ofrece MySQL como ejemplo y seguiremos las instrucciones de instalación.

   - En nuestra máquina local descargaremos todo el contenido de la rama ``master`` de [este repositorio](https://github.com/datacharmer/test_db) que contiene la base de datos de ejemplo. Podemos hacerlo a través del botón verde "Code <>" -> "Download ZIP".
   - En nuestra máquina local, descomprimiremos el archivo (en caso de haberlo descargado) de forma que tengamos un directorio llamado test_db-master.
   - En nuestra máquina local, desde una consola moveremos el contenido de la carpeta test_db-master al contenedor. Para ello, ejecutaremos el siguiente comando (en este caso se está lanzando desde la carpeta local donde se encuentra el directorio test_db-master):

            docker cp Downloads/test_db-master <<containerId>>:/employees
     
   - Entramos a la consola del contenedor, seleccionando nuestro contenedor en ejecución y haciendo click en "CLI":
     
     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/1_docker%20modo%20cli.png">
   - Navegamos a la carpeta ``employees`` que creamos a través del comando anterior:
     
     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/2_docker%20entrando%20a%20carpeta.png">
   - Ejecutamos el archivo ``employees.sql`` tal como se muestra:
     
     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/3_creando%20schema.png">
   - En este punto la base de datos debería tener instalado el Schema de pruebas.
     

5. Conectaremos con la base de datos MySQL a través de DataGrip y ejecutaremos alguna consulta para comprobar que todo funciona correctamente.

   - En la parte superior izquierda de la aplicación, le damos al símbolo ``+`` para crear una nueva conexión. En este caso, MySQL.
     
     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/4_Seleccion%20de%20db.png">
   - Introducimos los datos necesarios teniendo en cuenta los valores que usamos durante la creación del contenedor (usuario ``root`` y contraseña ``mysql``). El host siempre será localhost y el puerto es ``3306`` puesto que ese fue el que expusimos al exterior en nuestro contenedor.
     
     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/5_Setup%20de%20db%20mysql.png">
   - Probamos la conexión. Debe aparecer una mensaje en verde indicando que hay conectividad.
     
     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/6_Setup%20de%20db%20mysql%20ok.png">
   - En la base de datos habrá varios schemas por defecto instalados. Seleccionamos el schema ``employees`` que acabamos de instalar.
     
     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/7_seleccion%20de%20schema.png">
   - Ejecutamos una consulta a modo de prueba para ver que la conexión efectivamente funciona.
  
     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/9_ejecucion%20de%20sentencia%20sql.png">

7. Como curiosidad, puedes utilizar DataGrip para ver el modelo E/R de un schema.

   <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/8_vista%20de%20diagrama.png">

# 3. Instalación de Oracle Database 19c

1. En este caso somos nosotros los que tendremos que generar la imagen docker de la base de datos para poder crear luego un contenedor a partir de ella. Lo primero que haremos será descargar los binarios de la base de datos Oracle Database 19c. Para ello iremos a su [centro de descargas](https://www.oracle.com/es/downloads/):

     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora1_download.png">

2. Para poder descargar software de Oracle es necesario disponer de una cuenta por lo que crearemos una (puedes utilizar la dirección de correo electrónico que quieras).
3. Una vez que estemos dentro del centro de descargas, buscaremos el producto ``Oracle Database 19c`` tal como se muestra en la imagen.

     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora2_download.png">

4. Seleccionaremos la versión ``Enterprise Edition``.

     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora3_download.png">

5. Haremos click en "Continue" arriba a la derecha y pasaremos al chekout. Desmarcaremos todas las opciones a excepción de la base de datos en sí que es lo único que nos interesa descargar. En este punto es importante seleccionar la plataforma que corresponda con nuestro ordenador. Si nuestro procesador es ARM (por ejemplo, los últimos Mac de Apple) debemos elegir la opción ``Linux ARM 64-bit``. Si nuestra máquina tiene una arquitectura X86-64 seleccionamos la opción ``Linux x86-64``. En ningún caso seleccionaremos otra plataforma.

     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora4_download.png">

6. Una vez que haya finalizado la descarga (puede tardar unos minutos) no descomprimiremos el ZIP descargado, si no que lo renombraremos, en funcion de la arquitectura de nuestro procesador.
     
    - ``LINUX.X64_193000_db_home.zip`` si nuestra arquitectura es X86-64.
    - ``LINUX.ARM64_1919000_db_home.zip`` si nuestra arquitectura es ARM. 

7. Descargamos el [repositorio de imágenes de Docker de Oracle](https://github.com/oracle/docker-images) de la misma forma que hicimos en el caso de la base de datos de ejemplo para MySQL.
8. Una vez descargado el repositorio, navegamos hasta la carpeta /OracleDatabase/SingleInstance/dockerfiles/19.3.0/ y copiamos ahí el archivo ZIP de la base de datos de Oracle que hemos descargado anteriormente, junto con el resto de binarios de la instalación.

     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora5_folder.png">

9. Ejecutamos el script ``buildContainerImage.sh`` Esto creará una imagen Docker que podremos usar para crear un contenedor (esto puede tardar varios minutos, dependiendo de tu conexión a internet y la eficiencia de tu máquina).
   
       ./buildContainerImage.sh -v 19.3.0 -e (Para ordenadores basados en UNIX)
       sh buildContainerImage.sh -v 19.3.0 -e (Para ordenadores con Windows y Git Bash, Cygwin o SubSystem instalado)

     <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora6_build.png">

10. Creamos el contenedor para la base de datos. Estamos creándolo con 4 GB de Memoria. Asegúrate de tener recursos suficientes en tu máquina. El nombre del contenedor será ``oracle19cbbdda``. Expondremos en el puerto ``1521`` de nuestra máquina local el tráfico del mismo puerto del contendor. Lo mismo para el puerto ``5500``. El nombre de servicio default es ``orcl`` y la password de administrador es ``oracle``.

        docker run --name oracle19cbbdda -p 1521:1521 -p 5500:5500 -e ORACLE_PDB=orcl -e ORACLE_PWD=oracle -e ORACLE_MEM=4000 -v /opt/oracle/oradata -d oracle/database:19.3.0-ee

11. Si entramos al contenedor haciendo click en su nombre (desde Docker Desktop), veremos los logs de ejecucion del mismo. Una vez veamos el siguiente texto, la base de datos estará lista (esto puede tardar varios minutos, dependiendo de tu conexión a internet y la eficiencia de tu máquina):

       #########################
       DATABASE IS READY TO USE!
       #########################

12. A continuación instalaremos el Schema de pruebas para nuestros ejercicios. Se trata del Schema HR de Oracle. Ejecutaremos, dentro de la consola del contenedor, el comando:
       
        sqlplus sys/oracle@//localhost:1521/orcl as sysdba

13. Si eso nos diese error de acceso, tendremos que dar valor a la variable de entorno ``ORACLE_SID``. Para ello, ejecutaremos el comando:

        export ORACLE_SID=orcl

14. Una vez dentro de SQLPLUS (lo sabremos porque el prompt pasa a ser ``SQL>``) ejecutaremos el siguiente comando, que nos pedirá varios argumentos de forma interactiva.

        @?/demo/schema/human_resources/hr_main.sql
    
    - Valor del primer argumento (contraseña de la db HR): ``oracle``

    - Valor del segundo argumento (tablespace): ``USERS``

    - Valor del tercer argumento (almacenamiento temporal): ``TEMP``

    - Valor del cuarto argumento (destino de logs): ``$ORACLE_HOME/demo/schema/log/``

    <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora7_conexionCreacionHr.png">

15. Una vez finalizada la ejecución del script, la base de datos estará lista para ser utilizada. Podemos comprobarlo conectándonos a ella y ejecutando una consulta a través de DataGrip.

    - Creamos la conexión a la base de datos de Oracle en DataGrip. Para ello, en la parte superior izquierda de la aplicación, le damos al símbolo ``+`` para crear una nueva conexión. En este caso, ``Oracle``.
     
      <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora8_datagrip.png">

    - Introducimos los datos necesarios teniendo en cuenta los valores que usamos durante la creación del contenedor (usuario ``HR`` y contraseña ``oracle``). El servicio es ``orcl`` y el host siempre será localhost. El puerto es ``1521`` puesto que ese fue el que expusimos al exterior en nuestro contenedor. Asegúrate de que el tipo de conexión sea mediante ``Service Name``.
     
      <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora9_datagrip.png">

    - Ejecutamos una consulta a modo de prueba para ver que la conexión efectivamente funciona.
     
      <img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/ora10_datagrip.png">