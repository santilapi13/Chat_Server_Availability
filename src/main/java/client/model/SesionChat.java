package client.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class SesionChat implements GestorMensajes {
    private LocalDate fecha;
    private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
    private CredencialesUsuario local;
    private CredencialesUsuario remoto;

    public CredencialesUsuario getRemoto() {
        return remoto;
    }

    @Override
    public CredencialesUsuario getLocal() {
        return local;
    }

    public SesionChat(CredencialesUsuario local, CredencialesUsuario remoto) {
        this.fecha = LocalDate.now();
        this.local = local;
        this.remoto = remoto;
    }

    public void addMensaje(String mensaje, boolean enviadoPorLocal) {
        if (enviadoPorLocal)
            this.mensajes.add(new Mensaje(mensajes.size(), mensaje, LocalTime.now().toString(), local));
        else
            this.mensajes.add(new Mensaje(mensajes.size(), mensaje, LocalTime.now().toString(), remoto));
    }

    @Override
    public ArrayList<Mensaje> getMensajes() {
        return this.mensajes;
    }

    @Override
    public LocalDate getFecha() {
        return this.fecha;
    }

}
