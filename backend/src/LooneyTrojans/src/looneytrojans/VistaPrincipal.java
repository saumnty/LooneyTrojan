package looneytrojans;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

public class VistaPrincipal extends Application {

    private BorderPane root;
    private VBox barraLateral;
    private ResourceBundle bundle;
    private VistaEscaneo vistaEscaneo;
    private VistaEscaneoPremium vistaEscaneoPremium;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        VistaPrincipalStatic.setRoot(root);
        bundle = ResourceBundle.getBundle("looneytrojans.idiomas.mensajes", new Locale("es"));

        barraLateral = crearBarraLateral();
        Pane vistaInicial = crearVistaInicio();

        root.setLeft(barraLateral);
        root.setCenter(vistaInicial);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/looneytrojans/recursos/estilo.css").toExternalForm());

        // Icono de la ventana
        InputStream iconStream = getClass().getResourceAsStream("/looneytrojans/recursos/iconos/logo_troyan.png");
        if (iconStream != null) {
            primaryStage.getIcons().add(new Image(iconStream));
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("LooneyTrojans - Antivirus");

        primaryStage.setOnCloseRequest(event -> {
            terminarEscaneo();
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    private Button crearBotonConIcono(String textoClave, String rutaIcono) {
        String texto = bundle.getString(textoClave);
        InputStream is = getClass().getResourceAsStream(rutaIcono);
        ImageView iconoVista = null;

        if (is != null) {
            Image icono = new Image(is, 32, 32, true, true);
            iconoVista = new ImageView(icono);
        }

        Button boton = (iconoVista != null) ? new Button(texto, iconoVista) : new Button(texto);
        boton.getStyleClass().add("boton-lateral");
        return boton;
    }

    private VBox crearBarraLateral() {
        VBox barra = new VBox(20);
        barra.setPadding(new Insets(30));
        barra.setStyle("-fx-background-color: #101010;");
        barra.setAlignment(Pos.TOP_CENTER);

        // Logo en barra lateral
        try {
            InputStream is = getClass().getResourceAsStream("/looneytrojans/recursos/iconos/logo_troyan.png");
            if (is != null) {
                Image logo = new Image(is, 64, 64, true, true);
                ImageView logoView = new ImageView(logo);
                logoView.getStyleClass().add("logo-troyan");
                barra.getChildren().add(logoView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Text txtUsuario = new Text();
        txtUsuario.setFill(Color.LIGHTGREEN);
        txtUsuario.setStyle("-fx-font-size: 14px;");

        if (SesionUsuario.getUsuarioActual() != null) {
            txtUsuario.setText(SesionUsuario.getUsuarioActual().getNombre());
            barra.getChildren().add(txtUsuario);
        }

        Button btnEscanear = crearBotonConIcono("escanear", "/looneytrojans/recursos/iconos/escudo.png");
        Button btnCuarentena = crearBotonConIcono("cuarentena", "/looneytrojans/recursos/iconos/bacteria.png");
        Button btnConfiguracion = crearBotonConIcono("configuracion", "/looneytrojans/recursos/iconos/engranaje.png");
        Button btnCuenta = crearBotonConIcono("cuenta", "/looneytrojans/recursos/iconos/persona.png");

        btnEscanear.setOnAction(e -> root.setCenter(obtenerVistaEscaneoActual()));
        btnCuarentena.setOnAction(e -> root.setCenter(new VistaCuarentena()));
        btnConfiguracion.setOnAction(e -> root.setCenter(new VistaConfiguracion()));
        btnCuenta.setOnAction(e -> {
            VistaPerfil vistaPerfil = new VistaPerfil(() -> {
                terminarEscaneo();
                actualizarBarraLateral();
                root.setCenter(obtenerVistaEscaneoActual());
            });
            root.setCenter(vistaPerfil);
        });

        barra.getChildren().addAll(btnEscanear, btnCuarentena, btnConfiguracion, btnCuenta);
        return barra;
    }

    private Pane crearVistaInicio() {
        VBox contenido = new VBox(20);
        contenido.setAlignment(Pos.CENTER);
        contenido.setStyle("-fx-background-color: #000000;");
        contenido.setPadding(new Insets(40));

        // Mensaje de bienvenida
        Text bienvenida = new Text();
        if (SesionUsuario.getUsuarioActual() != null) {
            bienvenida.setText(bundle.getString("bienvenida") + ", " + SesionUsuario.getUsuarioActual().getNombre());
        } else {
            bienvenida.setText(bundle.getString("bienvenida"));
        }
        bienvenida.setFill(Color.LIMEGREEN);
        bienvenida.setStyle("-fx-font-size: 24px;");

        // Logo central
        ImageView logo = null;
        try {
            InputStream is = getClass().getResourceAsStream("/looneytrojans/recursos/iconos/logo_troyan.png");
            if (is != null) {
                logo = new ImageView(new Image(is));
                logo.setFitHeight(120);
                logo.setPreserveRatio(true);
                logo.getStyleClass().add("logo-troyan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (logo != null) {
            contenido.getChildren().addAll(logo, bienvenida);
        } else {
            contenido.getChildren().add(bienvenida);
        }

        return contenido;
    }

    private void actualizarBarraLateral() {
        VBox nuevaBarra = crearBarraLateral();
        root.setLeft(nuevaBarra);
    }

    private Pane obtenerVistaEscaneoActual() {
        if (SesionUsuario.getUsuarioActual() != null) {
            String plan = SesionUsuario.getUsuarioActual().getPlan();
            if (plan != null && plan.toLowerCase().contains("premium")) {
                if (vistaEscaneoPremium == null) vistaEscaneoPremium = new VistaEscaneoPremium();
                return vistaEscaneoPremium;
            }
        }
        if (vistaEscaneo == null) vistaEscaneo = new VistaEscaneo();
        return vistaEscaneo;
    }

    public void terminarEscaneo() {
        if (vistaEscaneo != null) {
            vistaEscaneo.detenerEscaneo();
        }
        if (vistaEscaneoPremium != null) {
            vistaEscaneoPremium.detenerEscaneo();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}