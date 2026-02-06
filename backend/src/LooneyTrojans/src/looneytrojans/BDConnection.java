package looneytrojans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class BDConnection {
    // Atributos para la conexión
    static String url = "jdbc:mysql://localhost:3306/looney_troyans";
    static String user = "root";
    static String pass = "angri95a";
    
    // Método para realizar la conexión
    public static Connection conectar () {
        // Inicializamos la conexión
        Connection con = null;
        
        try {
            // Establecemos la conexión
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión exitosa");
        } catch (SQLException e) {
            e.printStackTrace(); // Mostramos el error si falla la conexión
        }
        return con; // Devolvemos la conexión
    }
    
    // Método para realizar una consulta
    public static void consultarDatos() {
        // Consultamos la tabla "usuarios"
        String query = "SELECT * FROM usuarios"; // Cambia a la tabla que necesites

        try (Connection con = conectar(); // Conectamos a la base de datos
             Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery(query); // Ejecutamos la consulta

            while (rs.next()) { // Recorremos los resultados
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Contraseña: " + rs.getString("contraseña"));
                System.out.println("Estado: " + rs.getString("estado"));
                System.out.println("ID Plan: " + rs.getInt("id_plan"));
                System.out.println("---");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para insertar datos en la tabla "usuarios"
    public static void insertarDatos(String nombre, String email, String contraseña, int idPlan) {
        // La consulta para insertar un nuevo usuario
        String query = "INSERT INTO usuarios (nombre, email, contraseña, id_plan) VALUES ('" 
                        + nombre + "', '" + email + "', '" + contraseña + "', " + idPlan + ")";

        try (Connection con = conectar(); // Conectamos a la base de datos
             Statement stmt = con.createStatement()) {

            int rowsAffected = stmt.executeUpdate(query); // Ejecutamos la inserción
            if (rowsAffected > 0) {
                System.out.println("¡Usuario insertado exitosamente!");
            } else {
                System.out.println("Error al insertar el usuario.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}