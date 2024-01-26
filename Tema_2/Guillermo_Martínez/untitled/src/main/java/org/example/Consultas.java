package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Consultas {

    public static void main(String[] args) {
        // Configuración de la conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/employees";
        String usuario = "root";
        String contraseña = "mysql";

        // Consulta SQL
        String consulta = "SELECT gender, COUNT(*) AS total FROM UNIRBDDA-github GROUP BY gender ORDER BY total DESC";

        try (
                // Establecer conexión a la base de datos
                Connection conexion = DriverManager.getConnection(url, usuario, contraseña);

                // Crear una declaración preparada para ejecutar la consulta
                PreparedStatement statement = conexion.prepareStatement(consulta);

                // Ejecutar la consulta y obtener el resultado
                ResultSet resultado = statement.executeQuery()
        ) {
            // Imprimir los resultados
            System.out.println("Gender\tTotal");
            while (resultado.next()) {
                String gender = resultado.getString("gender");
                int total = resultado.getInt("total");
                System.out.println(gender + "\t" + total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
