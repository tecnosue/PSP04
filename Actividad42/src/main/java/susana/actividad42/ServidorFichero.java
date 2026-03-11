package susana.actividad42;

import java.io.*;
import java.net.*;

/**
 *
 * @author USER
 */
public class ServidorFichero extends Thread {

    Socket skCliente;
    static final int Puerto = 1500;

    //constructor
    public ServidorFichero(Socket sCliente) {
        skCliente = sCliente;
    }

    public static void main(String[] arg) {
        try {
            //iniciamos el Serversocket en el puerto 
            ServerSocket skServidor = new ServerSocket(Puerto);
            System.out.println("Servidor de ficheros listo en puerto " + Puerto);

            while (true) {

                Socket skCliente = skServidor.accept();
                new ServidorFichero(skCliente).start();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {

        try {
            DataInputStream flujo_entrada = new DataInputStream(skCliente.getInputStream());
            DataOutputStream flujo_salida = new DataOutputStream(skCliente.getOutputStream());

            // 1. Leer el nombre del fichero solicitado
            String nombreFichero = flujo_entrada.readUTF();
            File archivo = new File(nombreFichero);

            if (archivo.exists() && archivo.isFile()) {
                flujo_salida.writeUTF("OK"); // Indicamos que existe

                // Leemos el fichero y lo enviamos
                BufferedReader lector = new BufferedReader(new FileReader(archivo));
                String linea;

                // Enviamos línea por línea
                while ((linea = lector.readLine()) != null) {
                    flujo_salida.writeUTF(linea);
                }

                // Enviamos una señal de fin de fichero (EOF)
                flujo_salida.writeUTF("EOF");
                lector.close();

            } else {
                flujo_salida.writeUTF("ERROR"); // Indicamos que no existe
                flujo_salida.writeUTF("El fichero no existe o no se puede leer.");
            }

            skCliente.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
