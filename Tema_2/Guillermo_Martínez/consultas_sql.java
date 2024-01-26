import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class MySqlApplication {

    private static final String DATABASE = "UNIRBDDA-github";

    public static void main(String[] args) {

        try (Connection connection = new MySqlConnector("localhost", DATABASE).getConnection()) {

            log.info("Conexión establecida con la base de datos MySql");

            selectGenderCount(connection);

        } catch (Exception e) {
            log.error("Error al tratar con la base de datos", e);
        }
    }

    private static void selectGenderCount(Connection connection) throws SQLException {
        String sqlGenderCount = "SELECT gender, COUNT(*) as total " +
                "FROM employees " +
                "GROUP BY gender " +
                "ORDER BY total DESC";

        PreparedStatement selectGenderStatement = connection.prepareStatement(sqlGenderCount);
        ResultSet genderResult = selectGenderStatement.executeQuery();
        while (genderResult.next()) {
            log.debug("Género: {}, Total: {}",
                    genderResult.getString("gender"),
                    genderResult.getInt("total"));
        }
    }
}
