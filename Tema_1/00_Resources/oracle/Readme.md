Instalación de Oracle Database 19c Enterprise Edition
========================================================

## Parte I: Creación de la imagen Docker de la base de datos

En este caso somos nosotros los que tendremos que generar la imagen docker de la base de datos para poder crear luego un contenedor a partir de ella. Lo primero que haremos será descargar los binarios de la base de datos Oracle Database 19c. Para ello iremos a su [centro de descargas](https://www.oracle.com/es/downloads/):

<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora1_download.png">

Para poder descargar software de Oracle es necesario disponer de una cuenta por lo que crearemos una (puedes utilizar la dirección de correo electrónico que quieras). Una vez que estemos dentro del centro de descargas, buscaremos el producto ``Oracle Database 19c`` tal como se muestra en la imagen.

<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora2_download.png">

Seleccionaremos la versión ``Enterprise Edition``.

<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora3_download.png">

Haremos click en ``Continue`` arriba a la derecha y pasaremos al chekout.

Desmarcaremos todas las opciones a excepción de la base de datos en sí que es lo único que nos interesa descargar. En este punto es importante seleccionar la plataforma que corresponda con nuestro ordenador. Si nuestro procesador es ARM (por ejemplo, los últimos Mac de Apple) debemos elegir la opción ``Linux ARM 64-bit``. Si nuestra máquina tiene una arquitectura X86-64 seleccionamos la opción ``Linux x86-64``. En ningún caso seleccionaremos otra plataforma.

<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora4_download.png">

Una vez que haya finalizado la descarga (puede tardar unos minutos) no descomprimiremos el ZIP descargado, si no que lo renombraremos, en funcion de la arquitectura de nuestro procesador.
     
 - ``LINUX.X64_193000_db_home.zip`` si nuestra arquitectura es X86-64.

 - ``LINUX.ARM64_1919000_db_home.zip`` si nuestra arquitectura es ARM. 

Descargamos el [repositorio de imágenes de Docker de Oracle](https://github.com/oracle/docker-images). Podemos hacerlo a través del botón verde ``Code <>`` y después ``Download ZIP``. 

Una vez descargado el repositorio, navegamos hasta la carpeta ``/OracleDatabase/SingleInstance/dockerfiles/19.3.0/`` y copiamos ahí el archivo ZIP de la base de datos de Oracle que hemos descargado anteriormente.

<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora5_folder.png">

Ejecutamos el script ``buildContainerImage.sh`` Esto creará una imagen Docker que podremos usar para crear un contenedor (esto puede tardar varios minutos, dependiendo de tu conexión a internet y la eficiencia de tu máquina).
   
 - Para ordenadores basados en UNIX: ``./buildContainerImage.sh -v 19.3.0 -e``
 - Para ordenadores con Windows y Git Bash, Cygwin o SubSystem instalado: ``sh buildContainerImage.sh -v 19.3.0 -e``

Si la imagen se ha creado correctamente, veremos el siguiente mensaje:

<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora6_build.png">

## Parte II: Creación del contenedor de la base de datos Oracle Database

Para crear el contenedor usaremos 4 GB de Memoria. Asegúrate de tener recursos suficientes en tu máquina. El nombre del contenedor será ``oracle19cbbdda``. Expondremos en el puerto ``1521`` de nuestra máquina local el tráfico del mismo puerto del contendor. Lo mismo para el puerto ``5500``. El nombre de servicio default es ``orcl`` y la password de administrador es ``oracle``.

    docker run --name oracle19cbbdda -p 1521:1521 -p 5500:5500 -e ORACLE_PDB=orcl -e ORACLE_PWD=oracle -e ORACLE_MEM=4000 -v /opt/oracle/oradata -d oracle/database:19.3.0-ee

Si entramos al contenedor haciendo click en su nombre (desde Docker Desktop), veremos los logs de ejecucion del mismo. Una vez veamos el siguiente texto, la base de datos estará lista (esto puede tardar varios minutos, dependiendo de tu conexión a internet y la eficiencia de tu máquina):

       #########################
       DATABASE IS READY TO USE!
       #########################

## Parte III: Instalación del Schema ``HR`` de Oracle

A continuación instalaremos el Schema de pruebas para nuestros ejercicios. Se trata del Schema HR de Oracle. Ejecutaremos, dentro de la consola del contenedor, el comando:
       
    sqlplus sys/oracle@//localhost:1521/orcl as sysdba

Si eso nos diese error de acceso, tendremos que dar valor a la variable de entorno ``ORACLE_SID``. Para ello, ejecutaremos el comando:

    export ORACLE_SID=orcl

Una vez dentro de SQLPLUS (lo sabremos porque el prompt pasa a ser ``SQL>``) ejecutaremos el siguiente comando, que nos pedirá varios argumentos de forma interactiva.

    @?/demo/schema/human_resources/hr_main.sql
    
 - Valor del primer argumento (contraseña de la db HR): ``oracle``

 - Valor del segundo argumento (tablespace): ``USERS``

 - Valor del tercer argumento (almacenamiento temporal): ``TEMP``

 - Valor del cuarto argumento (destino de logs): ``$ORACLE_HOME/demo/schema/log/``

La ejecución completa del comando es la siguiente:

<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora7_conexionCreacionHr.png">

## Parte IV: Conexión desde DataGrip

Una vez finalizada la ejecución del script, la base de datos estará lista para ser utilizada. Podemos comprobarlo conectándonos a ella y ejecutando una consulta a través de DataGrip.

Creamos la conexión a la base de datos de Oracle en DataGrip. Para ello, en la parte superior izquierda de la aplicación, le damos al símbolo ``+`` para crear una nueva conexión. En este caso, ``Oracle``.
     
<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora8_datagrip.png">

Introducimos los datos necesarios teniendo en cuenta los valores que usamos durante la creación del contenedor (usuario ``HR`` y contraseña ``oracle``). El servicio es ``orcl`` y el host siempre será localhost. El puerto es ``1521`` puesto que ese fue el que expusimos al exterior en nuestro contenedor. Asegúrate de que el tipo de conexión sea mediante ``Service Name``.
     
<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora9_datagrip.png">

Ejecutamos una consulta a modo de prueba para ver que la conexión efectivamente funciona.
     
<img src="https://raw.githubusercontent.com/UnirCs/BBDD-PER8263-2324/master/Tema_1/00_Resources/imgs/ora10_datagrip.png">