package looneytrojans;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.sql.*;

public class VistaPerfil extends VBox {

    private boolean modoLogin = true;
    private static Connection conexion;
    private Runnable onSessionUpdate;

    public VistaPerfil() {
        this.setPadding(new Insets(40));
        this.setSpacing(20);
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: black;");

        if (conexion == null) {
            conexion = ConectarBaseDatos.getConexion();
            if (conexion != null) {
                System.out.println("✅ Conectado a SQLite correctamente");
            }
        }

        construirVista();

        if (SesionUsuario.getUsuarioActual() != null) {
            mostrarPerfilUsuario(SesionUsuario.getUsuarioActual());
        }
    }

    public VistaPerfil(Runnable onSessionUpdate) {
        this();
        this.onSessionUpdate = onSessionUpdate;
    }

    private void construirVista() {
        Label titulo = new Label();
        titulo.setStyle("-fx-font-size: 22px; -fx-text-fill: lime;");

        Label lblNombre = new Label("Nombre:");
        lblNombre.setTextFill(Color.WHITE);
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Tu nombre completo");

        Label lblEmail = new Label("Email:");
        lblEmail.setTextFill(Color.WHITE);
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Ingresa tu correo electrónico");

        Label lblContraseña = new Label("Contraseña:");
        lblContraseña.setTextFill(Color.WHITE);
        PasswordField txtContraseña = new PasswordField();
        txtContraseña.setPromptText("Ingresa tu contraseña");

        Label lblPlan = new Label("Plan:");
        lblPlan.setTextFill(Color.WHITE);
        ComboBox<String> comboPlan = new ComboBox<>();
        comboPlan.getItems().addAll("Básico", "Premium");
        comboPlan.setValue("Básico");

        Label mensaje = new Label();
        mensaje.setTextFill(Color.RED);

        Button btnAccion = new Button();
        Button btnSwitchModo = new Button();

        btnAccion.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String contraseña = txtContraseña.getText().trim();
            String nombre = txtNombre.getText().trim();
            int idPlan = comboPlan.getSelectionModel().getSelectedIndex() + 1;

            if (modoLogin) {
                Usuario usuario = validarInicioSesion(email, contraseña);
                if (usuario != null) {
                    SesionUsuario.setUsuarioActual(usuario);
                    mostrarPerfilUsuario(usuario);
                    if (onSessionUpdate != null) onSessionUpdate.run();
                } else {
                    mensaje.setTextFill(Color.RED);
                    mensaje.setText("Email o contraseña incorrectos.");
                }
            } else {
                if (nombre.isEmpty()) {
                    mensaje.setText("El nombre no puede estar vacío.");
                    return;
                }
                if (registrarUsuario(nombre, email, contraseña, idPlan)) {
                    mensaje.setTextFill(Color.LIME);
                    mensaje.setText("Registro exitoso. Inicia Sesión");
                } else {
                    mensaje.setTextFill(Color.RED);
                    mensaje.setText("Error al registrar usuario.");
                }
            }
        });

        btnSwitchModo.setOnAction(e -> {
            modoLogin = !modoLogin;
            mensaje.setText("");
            this.getChildren().clear();
            construirVista();
        });

        actualizarVista(titulo, btnAccion, btnSwitchModo);

        if (modoLogin) {
            this.getChildren().addAll(
                    titulo,
                    lblEmail, txtEmail,
                    lblContraseña, txtContraseña,
                    btnAccion,
                    btnSwitchModo,
                    mensaje
            );
        } else {
            this.getChildren().addAll(
                    titulo,
                    lblNombre, txtNombre,
                    lblEmail, txtEmail,
                    lblContraseña, txtContraseña,
                    lblPlan, comboPlan,
                    btnAccion,
                    btnSwitchModo,
                    mensaje
            );
        }
    }

    private void actualizarVista(Label titulo, Button btnAccion, Button btnSwitch) {
        if (modoLogin) {
            titulo.setText("Iniciar sesión");
            btnAccion.setText("Iniciar sesión");
            btnSwitch.setText("¿Aún no tienes cuenta? Regístrate");
        } else {
            titulo.setText("Registro de nuevo usuario");
            btnAccion.setText("Registrarse");
            btnSwitch.setText("¿Ya tienes cuenta? Inicia sesión");
        }
    }

    private Usuario validarInicioSesion(String email, String contraseña) {
        try {
            String sql = "SELECT u.nombre, u.email, u.fecha_registro, p.nombre AS nombre_plan " +
                         "FROM usuarios u JOIN planes p ON u.id_plan = p.id " +
                         "WHERE u.email = ? AND u.contraseña = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String correo = rs.getString("email");
                String plan = rs.getString("nombre_plan");
                Date fecha = rs.getDate("fecha_registro");
                return new Usuario(nombre, correo, plan, fecha.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean registrarUsuario(String nombre, String email, String contraseña, int idPlan) {
        try {
            String sql = "INSERT INTO usuarios (nombre, email, contraseña, id_plan) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, contraseña);
            stmt.setInt(4, idPlan);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarPerfilUsuario(Usuario usuario) {
        this.getChildren().clear();

        Label titulo = new Label("👤 Perfil del usuario");
        titulo.setStyle("-fx-font-size: 24px; -fx-text-fill: lime;");

        Label lblNombre = new Label("Nombre: " + usuario.getNombre());
        Label lblCorreo = new Label("Correo: " + usuario.getCorreo());
        Label lblPlan = new Label("Plan activo: " + usuario.getPlan());
        Label lblFecha = new Label("Fecha de registro: " + usuario.getFechaRegistro());

        for (Label lbl : new Label[]{lblNombre, lblCorreo, lblPlan, lblFecha}) {
            lbl.setTextFill(Color.WHITE);
            lbl.setStyle("-fx-font-size: 16px;");
        }

        Button btnCerrarSesion = new Button("Cerrar sesión");
        btnCerrarSesion.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
        btnCerrarSesion.setOnAction(e -> {
            SesionUsuario.setUsuarioActual(null);
            modoLogin = true;
            this.getChildren().clear();
            construirVista();
            if (onSessionUpdate != null) onSessionUpdate.run();
        });

        this.getChildren().addAll(titulo, lblNombre, lblCorreo, lblPlan, lblFecha, btnCerrarSesion);
    }

    public void setOnSessionUpdate(Runnable r) {
        this.onSessionUpdate = r;
    }
}