package looneytrojans;

import javafx.scene.layout.BorderPane;

public class VistaPrincipalStatic {
    private static BorderPane instancia;

    public static void setRoot(BorderPane root) {
        instancia = root;
    }

    public static BorderPane getInstance() {
        return instancia;
    }
}