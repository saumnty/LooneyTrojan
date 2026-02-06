package looneytrojans;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EscaneoBasico {

    private static Connection con;
    private static final String CARPETA_CUARENTENA = "C:/LooneyTrojans/Cuarentena";

    public static void obtenerConexion() {
        if (con == null) {
            con = ConectarBaseDatos.getConexion();
        }
    }

    public static void cerrarConexion() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void escanearArchivoOCarpeta(File objetivo, Consumer<Double> actualizarProgreso, Supplier<Boolean> shouldStop) {
        obtenerConexion();
        asegurarCarpetaCuarentena();

        List<File> archivosTotales = new ArrayList<>();

        if (objetivo != null && objetivo.exists()) {
            if (objetivo.isDirectory()) {
                listarArchivosRecursivos(objetivo, archivosTotales);
            } else {
                archivosTotales.add(objetivo);
            }
        }

        int total = archivosTotales.size();
        int procesados = 0;

        for (File archivo : archivosTotales) {
            if (shouldStop.get()) {
                System.out.println("🛑 Escaneo cancelado por el usuario.");
                break;
            }

            if (archivo.isFile()) {
                verificarArchivo(archivo);
                procesados++;
                double progreso = (double) procesados / total;
                actualizarProgreso.accept(progreso);
            }
        }

        cerrarConexion();
    }

    private static void asegurarCarpetaCuarentena() {
        File carpeta = new File(CARPETA_CUARENTENA);
        if (!carpeta.exists()) {
            boolean creada = carpeta.mkdirs();
            if (creada) {
                System.out.println("✅ Carpeta de cuarentena creada.");
            } else {
                System.out.println("⚠️ No se pudo crear la carpeta de cuarentena.");
            }
        }
    }

    private static void listarArchivosRecursivos(File directorio, List<File> lista) {
        if (directorio != null && directorio.exists() && directorio.isDirectory() && directorio.canRead()) {
            File[] archivos = directorio.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    lista.add(archivo);
                    if (archivo.isDirectory()) {
                        listarArchivosRecursivos(archivo, lista);
                    }
                }
            }
        }
    }

    public static void verificarArchivo(File archivo) {
        String nombreArchivo = archivo.getName();
        String sql = "SELECT * FROM diccionario_virus WHERE nombre_archivo = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nombreArchivo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("🛑 ¡El archivo " + nombreArchivo + " es un virus!");
                moverACuarentena(archivo);
            } else {
                System.out.println("✅ El archivo " + nombreArchivo + " está limpio.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void moverACuarentena(File archivoOriginal) {
        try {
            Path origen = archivoOriginal.toPath();
            Path destino = Paths.get(CARPETA_CUARENTENA, archivoOriginal.getName());

            int contador = 1;
            while (Files.exists(destino)) {
                String nuevoNombre = agregarSufijo(archivoOriginal.getName(), contador++);
                destino = Paths.get(CARPETA_CUARENTENA, nuevoNombre);
            }

            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("☣️ Archivo movido a cuarentena: " + destino);

        } catch (IOException e) {
            System.out.println("❌ No se pudo mover " + archivoOriginal.getAbsolutePath());
            e.printStackTrace();
        }
    }

    private static String agregarSufijo(String nombreOriginal, int numero) {
        int punto = nombreOriginal.lastIndexOf(".");
        if (punto > 0) {
            return nombreOriginal.substring(0, punto) + "_" + numero + nombreOriginal.substring(punto);
        } else {
            return nombreOriginal + "_" + numero;
        }
    }
}