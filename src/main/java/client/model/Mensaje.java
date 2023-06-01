package client.model;

public class Mensaje {
    private int ID;
    private String texto;
    private String hora;
    private CredencialesUsuario emisor;

    public Mensaje(int ID, String texto, String hora, CredencialesUsuario emisor) {
        this.ID = ID;
        this.texto = texto;
        this.hora = hora;
        this.emisor = emisor;
    }
}
