package server.monitor;

import server.monitor.Monitor;

import java.util.Scanner;

public class MonitorMain {
    public static void main(String[] args) {

        try {

            int puertoMonitor = 3000;  // Por defecto esta en el puerto 3000, sino, en lo que se pase como parametro
            String ipSecundario = "localhost";
            String puertoSecundario = "5678";
            Scanner sc = new Scanner(System.in);

            String modoMonitor = null;

            while (modoMonitor == null || (!modoMonitor.equals("1") && !modoMonitor.equals("2"))) {
                System.out.println("Elija una:");
                System.out.println("1. Conexiones por defecto.");
                System.out.println("2. Conexiones personalizadas.");
                modoMonitor = sc.nextLine();
            }

            if (modoMonitor.equals("2")) {
                System.out.println("Ingrese puerto para este monitor: ");
                puertoMonitor = Integer.parseInt(sc.nextLine());
                System.out.println("Ingrese IP del servidor secundario: ");
                ipSecundario = sc.nextLine();
                System.out.println("Ingrese puerto del servidor secundario: ");
                puertoSecundario = sc.nextLine();
            }

            Monitor monitor = new Monitor(puertoMonitor);
            monitor.conectarServerSecundario(ipSecundario, Integer.parseInt(puertoSecundario));

            monitor.empezarMonitoreo();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
