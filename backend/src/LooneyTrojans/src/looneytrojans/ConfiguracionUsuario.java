package looneytrojans;

import java.io.*;
import java.util.Locale;
import java.util.Properties;

public class ConfiguracionUsuario {

    private static final File archivoConfig = new File("C:/LooneyTrojans/config/usuario_config.txt");
    private static String tema = "Verde viscoso";
    private static String idioma = "Español";

    public static void cargarConfiguracion() {
        if (archivoConfig.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfig))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("tema=")) {
                        tema = linea.replace("tema=", "").trim();
                    } else if (linea.startsWith("idioma=")) {
                        idioma = linea.replace("idioma=", "").trim();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        aplicarIdioma();
    }

    private static void aplicarIdioma() {
        if (idioma.equalsIgnoreCase("English")) {
            Locale.setDefault(new Locale("en"));
        } else {
            Locale.setDefault(new Locale("es"));
        }
    }

    public static String getTema() {
        return tema;
    }

    public static String getIdioma() {
        return idioma;
    }
}