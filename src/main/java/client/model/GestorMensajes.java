package client.model;

import java.time.LocalDate;
import java.util.ArrayList;

public interface GestorMensajes {
    void addMensaje(String mensaje, boolean enviadoPorLocal);
    ArrayList<Mensaje> getMensajes();
    LocalDate getFecha();
    CredencialesUsuario getRemoto();
    CredencialesUsuario getLocal();
}
