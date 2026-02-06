package looneytrojans;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class VistaConfiguracion extends VBox {

    private ComboBox<String> selectorIdioma;
    private static final File archivoConfig = new File("C:/LooneyTrojans/config/usuario_config.txt");
    private ResourceBundle bundle;
    private Runnable onIdiomaCambiado; // 🔁 Callback para VistaPrincipal

    public VistaConfiguracion() {
        this.setSpacing(20);
        this.setPadding(new Insets(40));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: black;");

        this.bundle = ResourceBundle.getBundle("looneytrojans.idiomas.mensajes", Locale.getDefault());

        Text titulo = new Text(bundle.getString("configuracion"));
        titulo.setFill(Color.LIME);
        titulo.setStyle("-fx-font-size: 22px;");

        selectorIdioma = new ComboBox<>();
        selectorIdioma.getItems().addAll("Español", "English");
        selectorIdioma.setValue("Español");

        Button guardar = new Button(bundle.getString("guardar_cambios"));
        guardar.setOnAction(e -> guardarConfiguracion());

        VBox contenedor = new VBox(15);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().addAll(
                titulo,
                new Label(bundle.getString("idioma")), selectorIdioma,
                guardar
        );

        Label version = new Label(bundle.getString("version"));
        version.setTextFill(Color.GRAY);
        version.setStyle("-fx-font-size: 10px;");
        VBox.setMargin(version, new Insets(50, 0, 0, 0));

        this.getChildren().addAll(contenedor, version);

        cargarConfiguracion();
    }

    public void setOnIdiomaCambiado(Runnable callback) {
        this.onIdiomaCambiado = callback;
    }

    private void guardarConfiguracion() {
        try {
            archivoConfig.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(archivoConfig);
            writer.write("idioma=" + selectorIdioma.getValue() + "\n");
            writer.close();
            mostrarAlerta(bundle.getString("config_guardada"));
            aplicarIdioma();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(bundle.getString("config_error"));
        }
    }

    private void cargarConfiguracion() {
        if (archivoConfig.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(archivoConfig));
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("idioma=")) {
                        selectorIdioma.setValue(linea.replace("idioma=", ""));
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(bundle.getString("configuracion"));
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void aplicarIdioma() {
        String idioma = selectorIdioma.getValue();
        if (idioma.equals("English")) {
            Locale.setDefault(new Locale("en"));
            System.out.println("🌐 " + bundle.getString("idioma_cambiado_ingles"));
        } else {
            Locale.setDefault(new Locale("es"));
            System.out.println("🌐 " + bundle.getString("idioma_cambiado_espanol"));
        }

        if (onIdiomaCambiado != null) {
            onIdiomaCambiado.run(); // ✅ Actualiza botones laterales
        }
    }
}