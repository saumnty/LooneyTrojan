import java.sql.*;

public class MigrarDatos {

    public static void main(String[] args) {
        String urlMySQL = "jdbc:mysql://localhost:3306/looney_troyans";
        String user = "root";
        String password = "angri95a";

        String urlSQLite = "jdbc:sqlite:C:/LooneyTrojans/looney_troyans.db";

        try (
            Connection conMySQL = DriverManager.getConnection(urlMySQL, user, password);
            Connection conSQLite = DriverManager.getConnection(urlSQLite)
        ) {
            System.out.println("Conectado a ambas bases de datos.");

            String sqlSelect = "SELECT nombre_archivo, descripcion FROM diccionario_virus";
            PreparedStatement psSelect = conMySQL.prepareStatement(sqlSelect);
            ResultSet rs = psSelect.executeQuery();

            String sqlInsert = "INSERT INTO diccionario_virus (nombre_archivo, descripcion) VALUES (?, ?)";
            PreparedStatement psInsert = conSQLite.prepareStatement(sqlInsert);

            int count = 0;
            while (rs.next()) {
                String nombre = rs.getString("nombre_archivo");
                String descripcion = rs.getString("descripcion");

                psInsert.setString(1, nombre);
                psInsert.setString(2, descripcion);
                psInsert.executeUpdate();
                count++;
            }

            System.out.println("✔ Datos migrados: " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}