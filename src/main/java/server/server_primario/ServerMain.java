package server.server_primario;

import server.ControladorNuevosUsuarios;
import server.Servidor;
import server.server_primario.Heartbeat;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        try {
            String ip;
            String puerto;
            Scanner sc = new Scanner(System.in);

            String modoServidor = null;
            while (modoServidor == null || (modoServidor.equals("1") && modoServidor.equals("2"))) {
                System.out.println("Elija modo del servidor:");
                System.out.println("1. Iniciar servidor como primario.");
                System.out.println("2. Iniciar servidor como secundario.");
                modoServidor = sc.nextLine();
            }
            // SERVIDOR PRIMARIO
            if (modoServidor.equals("1")) {
                String opcion = null;
                while (opcion == null || (opcion.equals("1") && opcion.equals("2"))) {
                    System.out.println("Elija una:");
                    System.out.println("1. Conexiones por defecto.");
                    System.out.println("2. Conexiones personalizadas.");
                    opcion = sc.nextLine();
                }
                // Conexiones por defecto
                if (opcion.equals("1")) {
                    Servidor.getInstance().setPuerto(2345);
                    Heartbeat.getInstance().setIp("localhost");
                    Heartbeat.getInstance().setPuerto(3000);
                    Servidor.getInstance().conectarConSecundario("localhost", 5678);
                // Conexiones personalizadas
                } else {
                    System.out.println("Ingrese puerto para este servidor: ");
                    Servidor.getInstance().setPuerto(Integer.parseInt(sc.nextLine()));
                    // Conexion con secundario
                    System.out.println("Ingrese IP del servidor secundario: ");
                    ip = sc.nextLine();
                    System.out.println("Ingrese puerto del servidor secundario: ");
                    puerto = sc.nextLine();
                    Servidor.getInstance().conectarConSecundario(ip, Integer.parseInt(puerto));
                    // Conexion con monitor
                    System.out.println("Ingrese IP del monitor: ");
                    ip = sc.nextLine();
                    System.out.println("Ingrese puerto del monitor: ");
                    puerto = sc.nextLine();
                    Heartbeat.getInstance().setIp(ip);
                    Heartbeat.getInstance().setPuerto(Integer.parseInt(puerto));
                }
                Heartbeat.getInstance().start();
            // SERVIDOR SECUNDARIO
            } else {
                Servidor.getInstance().setPrimario(false);
                String opcion = null;
                while (opcion == null || (opcion.equals("1") && opcion.equals("2"))) {
                    System.out.println("Elija una:");
                    System.out.println("1. Conexiones por defecto.");
                    System.out.println("2. Conexiones personalizadas.");
                    opcion = sc.nextLine();
                }
                // Conexiones por defecto
                if (opcion.equals("1")) {
                    Servidor.getInstance().setPuerto(5678);
                // Conexiones personalizadas
                } else {
                    System.out.println("Ingrese puerto para este servidor: ");
                    Servidor.getInstance().setPuerto(Integer.parseInt(sc.nextLine()));
                }
                Servidor.getInstance().conectarConPrimario();
                Servidor.getInstance().conectarConMonitor();
            }

            ControladorNuevosUsuarios.getInstance().start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
