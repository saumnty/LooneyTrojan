package looneytrojans;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class VistaResultadoEscaneo extends VBox {

    public VistaResultadoEscaneo(int total, int limpios, int sospechosos, int conVirus, Runnable volverAccion) {
        this.setPadding(new Insets(40));
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: black;");

        Text titulo = new Text("\uD83D\uDCC8 Resultado del escaneo");
        titulo.setFill(Color.LIME);
        titulo.setStyle("-fx-font-size: 24px;");

        Text totalTxt = new Text("Archivos analizados: " + total);
        Text limpioTxt = new Text("Archivos limpios: " + limpios);
        Text sospechososTxt = new Text("Archivos sospechosos: " + sospechosos);
        Text virusTxt = new Text("Archivos con virus: " + conVirus);

        for (Text t : new Text[]{totalTxt, limpioTxt, sospechososTxt, virusTxt}) {
            t.setFill(Color.WHITE);
            t.setStyle("-fx-font-size: 16px;");
        }

        Button btnVolver = new Button("Volver a escanear");
        btnVolver.setStyle("-fx-background-color: darkgreen; -fx-text-fill: white;");
        btnVolver.setOnAction(e -> volverAccion.run());

        this.getChildren().addAll(titulo, totalTxt, limpioTxt, sospechososTxt, virusTxt, btnVolver);

        if (conVirus > 0) {
            mostrarAlerta("¡Se han detectado archivos con virus!", "El sistema ha detectado " + conVirus + " archivo(s) malicioso(s). Han sido movidos a la cuarentena.");
        }
    }

    private void mostrarAlerta(String header, String content) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("⚠️ Alerta de Virus");
        alerta.setHeaderText(header);
        alerta.setContentText(content);
        alerta.showAndWait();
    }
}