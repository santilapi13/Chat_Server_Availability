package client.model;

import java.io.IOException;

public interface GestorConexion {
    void desactivarModoEscucha() throws IOException;
    void solicitarChat(CredencialesUsuario credencialesUsuarioReceptor) throws IOException;
    void desconectar() throws IOException;
}
