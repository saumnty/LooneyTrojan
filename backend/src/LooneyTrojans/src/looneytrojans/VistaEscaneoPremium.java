package looneytrojans;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class VistaEscaneoPremium extends BorderPane {

    private ProgressBar barraProgreso;
    private Text textoEstado;
    private Text textoPorcentaje;
    private Circle animacionToxica;
    private ResourceBundle bundle;
    private HBox mensajeInicialBox;
    private AtomicBoolean escaneoCancelado = new AtomicBoolean(false);
    private AtomicBoolean escaneoPausado = new AtomicBoolean(false);
    private Button botonDetener;
    private Button botonIniciar;
    private Button botonPausarReanudar;
    private Button botonSeleccionarArchivo;
    private Button botonSeleccionarCarpeta;
    private Thread escaneoThread;
    private File archivoSeleccionado = null;

    public VistaEscaneoPremium() {
        this.setStyle("-fx-background-color: black;");
        this.bundle = ResourceBundle.getBundle("looneytrojans.idiomas.mensajes", Locale.getDefault());
        configurarElementos();
    }

    private void configurarElementos() {
        VBox centro = new VBox(20);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(40));

        animacionToxica = new Circle(50, Color.LIMEGREEN);
        animacionToxica.setOpacity(0.4);

        ScaleTransition animacion = new ScaleTransition(Duration.seconds(1), animacionToxica);
        animacion.setFromX(1);
        animacion.setToX(1.3);
        animacion.setFromY(1);
        animacion.setToY(1.3);
        animacion.setCycleCount(ScaleTransition.INDEFINITE);
        animacion.setAutoReverse(true);
        animacion.play();

        textoEstado = new Text(bundle.getString("estado_esperando"));
        textoEstado.setFill(Color.LIGHTGREEN);
        textoEstado.setStyle("-fx-font-size: 16px;");

        textoPorcentaje = new Text("0%");
        textoPorcentaje.setFill(Color.LIMEGREEN);
        textoPorcentaje.setStyle("-fx-font-size: 20px;");

        barraProgreso = new ProgressBar(0);
        barraProgreso.setPrefWidth(400);

        ImageView relojIcono = new ImageView(new Image(getClass().getResourceAsStream("/looneytrojans/recursos/iconos/reloj.png")));
        relojIcono.setFitWidth(24);
        relojIcono.setFitHeight(24);
        Text mensajeInicial = new Text(bundle.getString("mensaje_espera"));
        mensajeInicial.setFill(Color.GRAY);
        mensajeInicial.setStyle("-fx-font-size: 14px;");
        mensajeInicialBox = new HBox(10, relojIcono, mensajeInicial);
        mensajeInicialBox.setAlignment(Pos.CENTER);
        mensajeInicialBox.setVisible(false);

        botonSeleccionarArchivo = new Button(bundle.getString("seleccionar_archivo"));
        botonSeleccionarArchivo.setOnAction(e -> seleccionarArchivo());

        botonSeleccionarCarpeta = new Button(bundle.getString("seleccionar_carpeta"));
        botonSeleccionarCarpeta.setOnAction(e -> seleccionarCarpeta());

        botonIniciar = new Button(bundle.getString("iniciar_completo"));
        botonDetener = new Button(bundle.getString("detener_escaneo"));
        botonPausarReanudar = new Button(bundle.getString("pausar_escaneo"));
        botonDetener.setVisible(false);
        botonPausarReanudar.setVisible(false);

        botonIniciar.setOnAction(e -> {
            botonIniciar.setDisable(true);
            mensajeInicialBox.setVisible(true);
            escaneoCancelado.set(false);
            escaneoPausado.set(false);

            textoEstado.setText(bundle.getString("estado_esperando"));
            barraProgreso.setProgress(0);
            textoPorcentaje.setText("0%");

            PauseTransition pausa = new PauseTransition(Duration.seconds(2.5));
            pausa.setOnFinished(event -> {
                mensajeInicialBox.setVisible(false);
                botonDetener.setVisible(true);
                botonPausarReanudar.setVisible(true);
                iniciarEscaneo();
            });
            pausa.play();
        });

        botonDetener.setOnAction(e -> {
            escaneoCancelado.set(true);
            textoEstado.setText(bundle.getString("estado_cancelado"));
            if (escaneoThread != null && escaneoThread.isAlive()) {
                escaneoThread.interrupt();
            }
            Platform.runLater(() -> {
                barraProgreso.setProgress(0);
                textoPorcentaje.setText("0%");
            });
            botonDetener.setVisible(false);
            botonPausarReanudar.setVisible(false);
            botonIniciar.setDisable(false);
        });

        botonPausarReanudar.setOnAction(e -> {
            if (escaneoPausado.get()) {
                escaneoPausado.set(false);
                botonPausarReanudar.setText(bundle.getString("pausar_escaneo"));
                reanudarEscaneo();
            } else {
                escaneoPausado.set(true);
                botonPausarReanudar.setText(bundle.getString("reanudar_escaneo"));
            }
        });

        HBox controles = new HBox(10, botonSeleccionarArchivo, botonSeleccionarCarpeta, botonIniciar);
        controles.setAlignment(Pos.CENTER);

        centro.getChildren().addAll(
                animacionToxica,
                textoPorcentaje,
                barraProgreso,
                textoEstado,
                mensajeInicialBox,
                controles,
                botonDetener,
                botonPausarReanudar
        );

        this.setCenter(centro);
    }

    private void seleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("seleccionar_archivo"));
        archivoSeleccionado = fileChooser.showOpenDialog(getScene().getWindow());
        if (archivoSeleccionado != null) {
            textoEstado.setText(bundle.getString("archivo_seleccionado") + ": " + archivoSeleccionado.getName());
            botonIniciar.setText(bundle.getString("iniciar_archivo"));
        }
    }

    private void seleccionarCarpeta() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(bundle.getString("seleccionar_carpeta"));
        archivoSeleccionado = directoryChooser.showDialog(getScene().getWindow());
        if (archivoSeleccionado != null) {
            textoEstado.setText(bundle.getString("carpeta_seleccionada") + ": " + archivoSeleccionado.getAbsolutePath());
            botonIniciar.setText(bundle.getString("iniciar_carpeta"));
        }
    }

private void iniciarEscaneo() {
    textoEstado.setText(bundle.getString("estado_escaneando"));
    barraProgreso.setProgress(0);
    textoPorcentaje.setText("0%");

    escaneoThread = new Thread(() -> {
        ResultadoEscaneo resultado;

        // Si hay un archivo seleccionado, realiza un escaneo básico
        if (archivoSeleccionado != null) {
            resultado = EscaneoCompleto.escaneoBasico(archivoSeleccionado, progreso -> {
                if (escaneoCancelado.get()) return;

                synchronized (escaneoThread) {
                    // Si está pausado, el hilo espera hasta que se reanude
                    while (escaneoPausado.get()) {
                        try {
                            escaneoThread.wait();  // Espera hasta que sea notificado
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                Platform.runLater(() -> {
                    barraProgreso.setProgress(progreso);
                    textoPorcentaje.setText((int) (progreso * 100) + "%");
                });

                try {
                    Thread.sleep(20); // Lento para mostrar animación
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, escaneoCancelado);
        } else {
            // Si no hay archivo seleccionado, realiza un escaneo completo de todas las unidades
            resultado = new ResultadoEscaneo(0, 0, 0, 0);
            EscaneoCompleto.iniciarEscaneo(progreso -> {
                if (escaneoCancelado.get()) return;
                Platform.runLater(() -> {
                    barraProgreso.setProgress(progreso);
                    textoPorcentaje.setText((int) (progreso * 100) + "%");
                });
            }, () -> escaneoCancelado.get());
        }

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
                            Thread.sleep(20); // Lento para mostrar animación
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

                            // Cambia el texto y habilita los botones
                            textoEstado.setText(bundle.getString("estado_completado"));
                            barraProgreso.setProgress(1);
                            textoPorcentaje.setText("100%");

                            // Muestra la vista de resultados
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

                            // Habilita el botón de iniciar escaneo y cambia su texto
                            botonIniciar.setDisable(false);
                            botonIniciar.setText(bundle.getString("iniciar_completo"));

                            // Oculta los botones de pausar y detener al finalizar
                            botonDetener.setVisible(false);
                            botonPausarReanudar.setVisible(false);
                        });
                    }
                }).start();
            }
        });
    });
    escaneoThread.start();
}

private void reanudarEscaneo() {
    synchronized (escaneoThread) {
        // Cambia el estado de pausa
        escaneoPausado.set(false);
        escaneoThread.notify();  // Notifica al hilo para que continúe
        botonPausarReanudar.setText(bundle.getString("pausar_escaneo"));
    }
}

public void detenerEscaneo() {
    if (escaneoThread != null && escaneoThread.isAlive()) {
        escaneoCancelado.set(true);
        try {
            escaneoThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
}