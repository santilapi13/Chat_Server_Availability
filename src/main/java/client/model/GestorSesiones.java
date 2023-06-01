package client.model;

public interface GestorSesiones {
    SesionChat getSesionActual();
    void setSesionActual(SesionChat sesionChatActual);
    void addNuevaSesion(SesionChat sesionChat);
}
