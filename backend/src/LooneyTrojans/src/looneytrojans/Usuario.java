package looneytrojans;

public class Usuario {

    private String nombre;
    private String correo;
    private String plan;
    private String fechaRegistro;

    public Usuario(String nombre, String correo, String plan, String fechaRegistro) {
        this.nombre = nombre;
        this.correo = correo;
        this.plan = plan;
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPlan() {
        return plan;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }
}