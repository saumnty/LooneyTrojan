package looneytrojans;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class VistaCuarentena extends BorderPane {

    private TableView<File> tabla;
    private ObservableList<File> archivosEnCuarentena;
    private final File carpetaCuarentena = new File("C:/LooneyTrojans/Cuarentena");
    private ResourceBundle bundle;

    public VistaCuarentena() {
        this.setStyle("-fx-background-color: black;");
        this.bundle = ResourceBundle.getBundle("looneytrojans.idiomas.mensajes", Locale.getDefault());
        asegurarCarpeta();
        configurarInterfaz();
        cargarArchivos();
    }

    private void asegurarCarpeta() {
        if (!carpetaCuarentena.exists()) {
            boolean creada = carpetaCuarentena.mkdirs();
            if (creada) {
                System.out.println("✅ Carpeta de cuarentena creada.");
            } else {
                System.out.println("⚠️ No se pudo crear la carpeta de cuarentena.");
            }
        }
    }

    private void configurarInterfaz() {
        Text titulo = new Text(bundle.getString("archivos_cuarentena"));
        titulo.setFill(Color.LIME);
        titulo.setStyle("-fx-font-size: 22px;");

        tabla = new TableView<>();
        tabla.setPrefWidth(700);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<File, String> columnaNombre = new TableColumn<>(bundle.getString("nombre"));
        columnaNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<File, String> columnaRuta = new TableColumn<>(bundle.getString("ruta"));
        columnaRuta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAbsolutePath()));

        tabla.getColumns().addAll(columnaNombre, columnaRuta);

        Button btnEliminar = new Button(bundle.getString("eliminar_archivo"));
        Button btnRestaurar = new Button(bundle.getString("restaurar_archivo"));

        btnEliminar.setOnAction(e -> eliminarArchivo());
        btnRestaurar.setOnAction(e -> restaurarArchivo());

        HBox botones = new HBox(15, btnEliminar, btnRestaurar);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(15));

        VBox contenido = new VBox(15, titulo, tabla, botones);
        contenido.setPadding(new Insets(30));
        contenido.setAlignment(Pos.TOP_CENTER);

        this.setCenter(contenido);
    }

    private void cargarArchivos() {
        archivosEnCuarentena = FXCollections.observableArrayList();

        if (carpetaCuarentena.exists() && carpetaCuarentena.isDirectory()) {
            File[] archivos = carpetaCuarentena.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isFile() && !archivo.getName().equals("origenes.txt")) {
                        archivosEnCuarentena.add(archivo);
                    }
                }
            }
        }

        tabla.setItems(archivosEnCuarentena);
    }

    private void eliminarArchivo() {
        File seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado != null && seleccionado.delete()) {
            archivosEnCuarentena.remove(seleccionado);
            tabla.refresh();
            mostrarMensaje(bundle.getString("archivo_eliminado"));
        } else {
            mostrarMensaje(bundle.getString("no_eliminar"));
        }
    }

    private void restaurarArchivo() {
        File seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            File archivoOrigenes = new File(carpetaCuarentena, "origenes.txt");
            String rutaDestino = null;

            if (archivoOrigenes.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(archivoOrigenes))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        if (linea.startsWith(seleccionado.getName() + "=")) {
                            rutaDestino = linea.split("=", 2)[1];
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File destino = rutaDestino != null
                    ? new File(rutaDestino)
                    : new File(System.getProperty("user.home") + "/Desktop/" + seleccionado.getName());

            boolean movido = seleccionado.renameTo(destino);
            if (movido) {
                archivosEnCuarentena.remove(seleccionado);
                tabla.refresh();
                mostrarMensaje(bundle.getString("archivo_restaurado"));
                eliminarRutaDeOrigenes(seleccionado.getName());
            } else {
                mostrarMensaje(bundle.getString("no_restaurar"));
            }
        }
    }

    private void eliminarRutaDeOrigenes(String nombreArchivo) {
        File archivoOrigenes = new File(carpetaCuarentena, "origenes.txt");
        File temporal = new File(carpetaCuarentena, "origenes_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivoOrigenes));
             FileWriter fw = new FileWriter(temporal)) {

            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.startsWith(nombreArchivo + "=")) {
                    fw.write(linea + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (archivoOrigenes.exists()) {
            archivoOrigenes.delete();
        }
        temporal.renameTo(archivoOrigenes);
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(bundle.getString("cuarentena"));
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}