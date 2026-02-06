package looneytrojans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.File;

public class ConectarBaseDatos {

    public static Connection getConexion() {
        try {
            // Ruta relativa del archivo .db
            File dbFile = new File("app/database/looney_troyans.db");

            // Asegurarse de que las carpetas existen
            if (!dbFile.getParentFile().exists()) {
                dbFile.getParentFile().mkdirs();
            }

            // Registrar el driver manualmente si es necesario
            Class.forName("org.sqlite.JDBC");

            // Crear la conexión con la ruta absoluta
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            Connection con = DriverManager.getConnection(url);

            System.out.println("✅ Conectado a SQLite correctamente");

            // Crear tablas necesarias
            crearTablasSiNoExisten(con);

            // Cargar diccionario desde archivo si existe
            String rutaDiccionario = new File("app/database/Ransom.Sphinx.txt").getAbsolutePath();
            File archivoDiccionario = new File(rutaDiccionario);
            if (archivoDiccionario.exists()) {
                // Solo cargar el diccionario si no se ha cargado previamente
                if (!diccionarioCargado(con)) {
                    ManejoBaseDatos.cargarDiccionarioDesdeArchivo(rutaDiccionario);
                }
            } else {
                System.out.println("⚠️ Diccionario no encontrado: " + rutaDiccionario);
            }

            return con;

        } catch (ClassNotFoundException e) {
            System.err.println("❌ No se encontró el driver JDBC de SQLite.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con SQLite:");
            e.printStackTrace();
            return null;
        }
    }

    private static void crearTablasSiNoExisten(Connection con) {
        try (Statement stmt = con.createStatement()) {
            // Tabla de planes
            stmt.execute("CREATE TABLE IF NOT EXISTS planes (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nombre TEXT UNIQUE)");

            // Tabla de usuarios
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nombre TEXT," +
                         "email TEXT UNIQUE," +
                         "contraseña TEXT," +
                         "id_plan INTEGER," +
                         "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                         "estado TEXT DEFAULT 'activo'," +
                         "FOREIGN KEY (id_plan) REFERENCES planes(id))");

            // Tabla de diccionario de virus
            stmt.execute("CREATE TABLE IF NOT EXISTS diccionario_virus (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nombre_archivo TEXT UNIQUE)");

            // Insertar planes si no existen
            stmt.execute("INSERT OR IGNORE INTO planes (id, nombre) VALUES " +
                         "(1, 'Básico'), (2, 'Premium')");

            System.out.println("✅ Tablas creadas o verificadas correctamente.");

        } catch (SQLException e) {
            System.err.println("⚠️ Error al crear tablas:");
            e.printStackTrace();
        }
    }

    private static boolean diccionarioCargado(Connection con) {
        // Verificar si la tabla 'diccionario_virus' tiene entradas
        String sql = "SELECT COUNT(*) FROM diccionario_virus";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1) > 0;  // Si hay más de 0 registros, significa que ya está cargado
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}