package looneytrojans;

public class ResultadoEscaneo {
    public int total;
    public int limpios;
    public int sospechosos;
    public int conVirus;

    public ResultadoEscaneo(int total, int limpios, int sospechosos, int conVirus) {
        this.total = total;
        this.limpios = limpios;
        this.sospechosos = sospechosos;
        this.conVirus = conVirus;
    }
}