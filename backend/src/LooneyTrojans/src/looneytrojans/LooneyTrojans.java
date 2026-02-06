package looneytrojans;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class LooneyTrojans extends Application {

    private ResourceBundle bundle;

    public static void main(String[] args) {
        System.out.println("➡️ Iniciando LooneyTrojans");
        launch(args);
        System.out.println("✅ Aplicación lanzada");
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("🟢 Dentro de start()");
        // Cargar configuración de usuario e idioma
        ConfiguracionUsuario.cargarConfiguracion();

        // Forzar la carga del archivo "mensajes_es.properties" 
        try {
            // Intentamos cargar "mensajes_es.properties" para español general
            this.bundle = ResourceBundle.getBundle("looneytrojans.idiomas.mensajes", new Locale("es"));
        } catch (Exception e) {
            // Si no se encuentra el archivo, lanzar un error
            e.printStackTrace();
            System.err.println("No se pudo cargar el archivo de recursos en español.");
        }

        // Lanzar la VistaPrincipal
        VistaPrincipal vistaPrincipal = new VistaPrincipal();
        vistaPrincipal.start(primaryStage);
        System.out.println("✅ Interfaz lanzada");
    }
}