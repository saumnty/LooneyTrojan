package looneytrojans;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class ManejoBaseDatos {

    // Método para cargar el diccionario de virus desde un archivo
    public static void cargarDiccionarioDesdeArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("❌ El archivo no existe: " + rutaArchivo);
            return;
        }

        try (Connection con = ConectarBaseDatos.getConexion();
             PreparedStatement verificar = con.prepareStatement(
                 "SELECT COUNT(*) FROM diccionario_virus WHERE nombre_archivo = ?");
             PreparedStatement insertar = con.prepareStatement(
                 "INSERT INTO diccionario_virus (nombre_archivo) VALUES (?)");
             java.util.Scanner scanner = new java.util.Scanner(archivo, "UTF-8")) {

            int insertados = 0;
            int duplicados = 0;

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();

                if (!linea.isEmpty()) {
                    // Verifica si ya existe
                    verificar.setString(1, linea);
                    ResultSet rs = verificar.executeQuery();
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        insertar.setString(1, linea);
                        insertar.addBatch();
                        insertados++;
                    } else {
                        duplicados++;
                    }
                }
            }

            insertar.executeBatch();
            System.out.println("Carga completa. Insertados: " + insertados + ", Duplicados: " + duplicados);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al cargar el diccionario.");
        }
    }
}