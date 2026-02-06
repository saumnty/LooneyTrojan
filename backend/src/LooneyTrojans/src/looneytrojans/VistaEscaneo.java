package looneytrojans;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class VistaEscaneo extends BorderPane {

    private ProgressBar barraProgreso;
    private Text textoEstado;
    private Text textoPorcentaje;
    private ResourceBundle bundle;
    private AtomicBoolean escaneoCancelado = new AtomicBoolean(false);
    private Thread escaneoThread;
    private File archivoSeleccionado;

    public VistaEscaneo() {
        this.setStyle("-fx-background-color: black;");
        this.bundle = ResourceBundle.getBundle("looneytrojans.idiomas.mensajes", Locale.getDefault());
        configurarElementos();
    }

    private void configurarElementos() {
        VBox centro = new VBox(20);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(40));

        textoEstado = new Text(bundle.getString("estado_esperando"));
        textoEstado.setFill(Color.LIGHTGREEN);
        textoEstado.setStyle("-fx-font-size: 16px;");

        textoPorcentaje = new Text("0%");
        textoPorcentaje.setFill(Color.LIMEGREEN);
        textoPorcentaje.setStyle("-fx-font-size: 20px;");

        barraProgreso = new ProgressBar(0);
        barraProgreso.setPrefWidth(400);

        Button botonSeleccionarArchivo = new Button(bundle.getString("seleccionar_archivo"));
        botonSeleccionarArchivo.setOnAction(e -> {
            seleccionarArchivo();
            barraProgreso.setProgress(0);
            textoPorcentaje.setText("0%");
            textoEstado.setText(bundle.getString("archivo_seleccionado") + ": " + archivoSeleccionado.getName());
        });

        Button botonSeleccionarCarpeta = new Button(bundle.getString("seleccionar_carpeta"));
        botonSeleccionarCarpeta.setOnAction(e -> {
            seleccionarCarpeta();
            barraProgreso.setProgress(0);
            textoPorcentaje.setText("0%");
            textoEstado.setText(bundle.getString("carpeta_seleccionada") + ": " + archivoSeleccionado.getAbsolutePath());
        });

        Button botonIniciar = new Button(bundle.getString("iniciar_escaneo"));
        Button botonDetener = new Button(bundle.getString("detener_escaneo"));

        botonIniciar.setOnAction(e -> {
            if (archivoSeleccionado != null) {
                escaneoCancelado.set(false);
                barraProgreso.setProgress(0);
                textoPorcentaje.setText("0%");
                textoEstado.setText(bundle.getString("estado_escaneando"));
                iniciarEscaneo();
            } else {
                textoEstado.setText(bundle.getString("seleccione_primero"));
            }
        });

        botonDetener.setOnAction(e -> {
            escaneoCancelado.set(true);
            textoEstado.setText(bundle.getString("estado_cancelado"));
            barraProgreso.setProgress(0);
            textoPorcentaje.setText("0%");
        });

        HBox botones = new HBox(10, botonSeleccionarArchivo, botonSeleccionarCarpeta, botonIniciar, botonDetener);
        botones.setAlignment(Pos.CENTER);

        centro.getChildren().addAll(textoPorcentaje, barraProgreso, textoEstado, botones);
        this.setCenter(centro);
    }

    private void seleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("seleccionar_archivo"));
        archivoSeleccionado = fileChooser.showOpenDialog(getScene().getWindow());
        if (archivoSeleccionado != null) {
            textoEstado.setText(bundle.getString("archivo_seleccionado") + ": " + archivoSeleccionado.getName());
        }
    }

    private void seleccionarCarpeta() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(bundle.getString("seleccionar_carpeta"));
        archivoSeleccionado = directoryChooser.showDialog(getScene().getWindow());
        if (archivoSeleccionado != null) {
            textoEstado.setText(bundle.getString("carpeta_seleccionada") + ": " + archivoSeleccionado.getAbsolutePath());
        }
    }

    private void iniciarEscaneo() {
        escaneoThread = new Thread(() -> {
            ResultadoEscaneo resultado = EscaneoCompleto.escaneoBasico(
                archivoSeleccionado,
                progreso -> {
                    if (escaneoCancelado.get()) return;
                    Platform.runLater(() -> {
                        barraProgreso.setProgress(progreso);
                        textoPorcentaje.setText((int) (progreso * 100) + "%");
                    });
                },
                escaneoCancelado
            );

            // Simulación lenta de barra tras escaneo
            Platform.runLater(() -> {
                if (!escaneoCancelado.get()) {
                    new Thread(() -> {
                        for (int i = 0; i <= 100; i++) {
                            final int porcentaje = i;
                            if (escaneoCancelado.get()) break;
                            Platform.runLater(() -> {
                                barraProgreso.setProgress(porcentaje / 100.0);
                                textoPorcentaje.setText(porcentaje + "%");
                            });
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }

                        if (!escaneoCancelado.get()) {
                            Platform.runLater(() -> {
                                if (resultado.conVirus > 0) {
                                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                                    alerta.setTitle("⚠️ Alerta de Virus");
                                    alerta.setHeaderText("¡Se han detectado archivos con virus!");
                                    alerta.setContentText("El sistema ha detectado " + resultado.conVirus +
                                            " archivo(s) malicioso(s).\nHan sido movidos a la cuarentena.");
                                    alerta.showAndWait();
                                }

                                textoEstado.setText(bundle.getString("estado_completado"));
                                VistaResultadoEscaneo vista = new VistaResultadoEscaneo(
                                        resultado.total,
                                        resultado.limpios,
                                        resultado.sospechosos,
                                        resultado.conVirus,
                                        () -> {
                                            VistaPrincipalStatic.getInstance().setCenter(this);
                                            barraProgreso.setProgress(0);
                                            textoPorcentaje.setText("0%");
                                            textoEstado.setText(bundle.getString("estado_esperando"));
                                        }
                                );
                                VistaPrincipalStatic.getInstance().setCenter(vista);
                            });
                        }
                    }).start();
                }
            });
        });
        escaneoThread.start();
    }

    public void detenerEscaneo() {
        if (escaneoThread != null && escaneoThread.isAlive()) {
            escaneoCancelado.set(true);
            escaneoThread.interrupt();
        }
    }
}