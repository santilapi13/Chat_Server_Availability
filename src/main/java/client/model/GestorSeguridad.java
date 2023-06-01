package client.model;

import java.io.IOException;

public interface GestorSeguridad {

    String encriptar(String pass, String texto, String algoritmo) throws Exception;

     String desencriptar(String pass, String textoEncriptado, String algoritmo) throws Exception;
}
