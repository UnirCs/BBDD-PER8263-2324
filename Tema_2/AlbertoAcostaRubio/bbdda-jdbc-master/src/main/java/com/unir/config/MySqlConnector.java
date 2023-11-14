package com.unir.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Slf4j
@Getter
public class MySqlConnector {
    private final Connection connection;
    private final String mysqlUser = "root";
    private final String mysqlPassword = "mysql";

    /**
     * Constructor de la clase. Se conecta a la base de datos.
     * @param host
     * @param database
     */
    public MySqlConnector(String host, String database) {
            
        try {
            //Creamos la conexión a la base de datos
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    mysqlUser,
                mysqlPassword);

        } catch (SQLException e) {
            log.error("Error al conectar con la base de datos", e);
            throw new RuntimeException(e);
        }
    }
}
