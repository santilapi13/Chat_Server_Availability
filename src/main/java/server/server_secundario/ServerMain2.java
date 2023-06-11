package server.server_secundario;

import server.ControladorNuevosUsuarios;
import server.Servidor;
import server.server_primario.Heartbeat;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain2 {
    public static void main(String[] args) {
        try {
            String ip;
            String puerto;
            Scanner sc = new Scanner(System.in);

            String modoServidor = null;
            while (modoServidor == null || (!modoServidor.equals("1") && !modoServidor.equals("2") && !modoServidor.equals("3"))) {
                System.out.println("Elija modo del servidor:");
                System.out.println("1. Iniciar servidor como primario.");
                System.out.println("2. Iniciar servidor como secundario.");
                System.out.println("3. Reiniciar servidor primario.");
                modoServidor = sc.nextLine();
            }
            // SERVIDOR PRIMARIO
            if (modoServidor.equals("1")) {
                String opcion = null;
                while (opcion == null || (!opcion.equals("1") && !opcion.equals("2"))) {
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
            } else if (modoServidor.equals("2")) {
                Servidor.getInstance().setPrimario(false);
                String opcion = null;
                while (opcion == null || (!opcion.equals("1") && !opcion.equals("2"))) {
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

                // REINICIAR SERVIDOR PRIMARIO
            } else {
                String opcion = null;
                while (opcion == null || (!opcion.equals("1") && !opcion.equals("2"))) {
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
                    ip = "localhost";
                    puerto = "5678";
                    // Conexiones personalizadas
                } else {
                    System.out.println("Ingrese puerto para este servidor: ");
                    Servidor.getInstance().setPuerto(Integer.parseInt(sc.nextLine()));
                    // Conexion con secundario
                    System.out.println("Ingrese IP del servidor secundario: ");
                    ip = sc.nextLine();
                    System.out.println("Ingrese puerto del servidor secundario: ");
                    puerto = sc.nextLine();
                    // Conexion con monitor
                    System.out.println("Ingrese IP del monitor: ");
                    ip = sc.nextLine();
                    System.out.println("Ingrese puerto del monitor: ");
                    puerto = sc.nextLine();
                    Heartbeat.getInstance().setIp(ip);
                    Heartbeat.getInstance().setPuerto(Integer.parseInt(puerto));
                }
                Heartbeat.getInstance().start();
                Servidor.getInstance().conectarConSecundario(ip, Integer.parseInt(puerto));
                Servidor.getInstance().recibirUsuariosAResincronizar();
            }

            ControladorNuevosUsuarios.getInstance().start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
