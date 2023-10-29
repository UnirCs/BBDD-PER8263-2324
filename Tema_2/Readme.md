Problemas detectados:
    - No soy capaz de crear las variables de entorno en VisualCode
    - La terminal me indica que no es capaz de obtener los drivers para la BBDD de Oracle
        22:01:46.816 [main] ERROR com.unir.config.OracleDatabaseConnector - Error al conectar con la base de datos
        java.sql.SQLException: No suitable driver found for jdbc:oracle:thin:@//localhost/orcl
        at java.sql.DriverManager.getConnection(DriverManager.java:689)
        at java.sql.DriverManager.getConnection(DriverManager.java:247)
        at com.unir.config.OracleDatabaseConnector.<init>(OracleDatabaseConnector.java:28)
        at com.unir.app.read.OracleApplication.main(OracleApplication.java:17)