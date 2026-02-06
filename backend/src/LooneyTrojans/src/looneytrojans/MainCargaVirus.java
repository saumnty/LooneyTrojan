package looneytrojans;

public class MainCargaVirus {
    public static void main(String[] args) {
        // Cambia esta ruta por la ruta real donde está tu archivo .txt
        String ruta = "E:\\Santiago Saucedo Mendoza - UAM-C\\Quinto Trimestre\\Proyecto de Ingeniería de Software\\LooneyTrojans\\database\\Ransom.Sphinx.txt";
        ManejoBaseDatos.cargarDiccionarioDesdeArchivo(ruta);
    }
}