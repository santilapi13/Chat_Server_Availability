package server.monitor;

import server.monitor.Monitor;

import java.util.Scanner;

public class MonitorMain {
    public static void main(String[] args) {

        try {

            int puertoMonitor = 3000;  // Por defecto esta en el puerto 3000, sino, en lo que se pase como parametro
            String ipSecundario = "localhost";
            String puertoSecundario = "5678";

            if (args.length > 0) {
                puertoMonitor = Integer.parseInt(args[0]);
                Scanner sc = new Scanner(System.in);
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
