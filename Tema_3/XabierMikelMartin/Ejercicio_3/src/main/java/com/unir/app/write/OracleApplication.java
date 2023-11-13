package com.unir.app.write;

import com.unir.config.OracleDatabaseConnector;
import com.unir.model.OracleJobs;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * La version para MySQL seria muy similar a esta, cambiando únicamente el Driver.
 */
@Slf4j
public class OracleApplication {

    private static final String SERIVCE_NAME = "orcl";

    public static void main(String[] args) {

        //Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        //Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try(Connection connection = new OracleDatabaseConnector("localhost", SERIVCE_NAME).getConnection()) {

            log.info("Conexión establecida con la base de datos Oracle");

            OracleJobs job = new OracleJobs("IT_JUN",1100,900, "Junior Programmer");
            jobUpsert(connection, job);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    /**
     * Funcion para hacer una inserción Simple de un Job en la base de datos
     * Si el job ya existe, se actualiza. Si no existe, se inserta.
     * @param connection - Conexion a la base de datos
     * @param job - Job a insertar o actualizar
     * @throws SQLException - Si ocurre algún error al ejecutar la consulta.
     */
    public static void jobUpsert(Connection connection, OracleJobs job) throws SQLException {

        String selectSql = "SELECT COUNT(*) FROM jobs WHERE job_id = ?";
        String updateSql = "UPDATE jobs SET max_salary = ?, min_salary = ? , job_title = ? WHERE job_id = ?";
        String insertSql = "INSERT INTO jobs (job_id, max_salary, min_salary, job_title) VALUES (?, ?, ?, ?)";

        PreparedStatement selectStatement = connection.prepareStatement(selectSql);
        selectStatement.setString(1, job.getJobId()); // Código del job
        ResultSet resultSet = selectStatement.executeQuery();
        resultSet.next(); // Nos movemos a la primera fila
        int rowCount = resultSet.getInt(1);

        if(rowCount > 0) {
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setInt(1, job.getMaxSalary()); // Nuevo maxSalary
            updateStatement.setInt(2, job.getMinSalary()); // Nuevo minSalary
            updateStatement.setString(3, job.getJobTitle()); // Nuevo jobTitle
            updateStatement.setString(4, job.getJobId()); // Código del job
            int filasActualizadas = updateStatement.executeUpdate();
            log.debug("Filas Actualizadas: {}", filasActualizadas);

        } else {
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, job.getJobId()); // Código del job
            insertStatement.setInt(2, job.getMaxSalary()); // Nuevo maxSalary
            insertStatement.setInt(3, job.getMinSalary()); // Nuevo minSalary
            insertStatement.setString(4, job.getJobTitle()); // Nuevo jobTitle
            int filasInsertadas = insertStatement.executeUpdate();
            log.debug("Filas Insertadas: {}", filasInsertadas);
        }
    }
}
