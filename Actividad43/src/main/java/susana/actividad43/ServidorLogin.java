package susana.actividad43;

import java.io.*;
import java.net.*;

public class ServidorLogin extends Thread {

    Socket skCliente;
    static final int Puerto = 1500;

    // Credenciales válidas
    static final String USUARIO_VALIDO = "javier";
    static final String PASSWORD_VALIDO = "secreta";

    //constructor
    public ServidorLogin(Socket sCliente) {
        skCliente = sCliente;
    }

    public static void main(String[] arg) {
        try {
            //iniciamos el Serversocket en el puerto 
            ServerSocket skServidor = new ServerSocket(Puerto);
            System.out.println("Servidor de ficheros listo en puerto " + Puerto);

            while (true) {

                Socket skCliente = skServidor.accept();
                new ServidorLogin(skCliente).start();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        try {
            DataInputStream flujo_entrada = new DataInputStream(skCliente.getInputStream());
            DataOutputStream flujo_salida = new DataOutputStream(skCliente.getOutputStream());

            int estado = 1;
            String comando;

            do {
                switch (estado) {

                    case 1: // LOGIN
                        flujo_salida.writeUTF("Introduce usuario:");
                        String usuario = flujo_entrada.readUTF();

                        flujo_salida.writeUTF("Introduce contraseña:");
                        String password = flujo_entrada.readUTF();

                        if (usuario.equals(USUARIO_VALIDO) && password.equals(PASSWORD_VALIDO)) {
                            flujo_salida.writeUTF("LOGIN_OK");
                            System.out.println("\tUsuario autenticado");
                            estado = 2;
                        } else {
                            flujo_salida.writeUTF("LOGIN_ERROR");
                            System.out.println("\tLogin fallido");
                        }
                        break;

                    case 2: // MENÚ
                        flujo_salida.writeUTF("Introduce comando (ls/get/exit):");
                        comando = flujo_entrada.readUTF();
                        System.out.println("\tComando: " + comando);

                        if (comando.equalsIgnoreCase("ls")) {
                            estado = 3;
                            break;

                        } else if (comando.equalsIgnoreCase("get")) {
                            estado = 4;
                            break;
                        } else if (comando.equalsIgnoreCase("exit")) {
                            estado = -1;
                            break;
                        }

                    case 3:
                        // Listar directorio
                        File directorio = new File(".");
                        File[] archivos = directorio.listFiles();

                        flujo_salida.writeUTF("LISTADO");
                        for (File archivo : archivos) {
                            if (archivo.isDirectory()) {
                                flujo_salida.writeUTF("[DIR]  " + archivo.getName());
                            } else {
                                flujo_salida.writeUTF("[FILE] " + archivo.getName());
                            }
                        }
                        flujo_salida.writeUTF("FIN_LISTADO");
                        estado = 2;
                        break;

                    case 4:

                        flujo_salida.writeUTF("Introduce el nombre del fichero:");
                        String nombreFichero = flujo_entrada.readUTF();

                        File archivo = new File(nombreFichero);
                        if (archivo.exists() && archivo.isFile()) {
                            flujo_salida.writeUTF("OK");
                            BufferedReader lector = new BufferedReader(new FileReader(archivo));
                            String linea;
                            while ((linea = lector.readLine()) != null) {
                                flujo_salida.writeUTF(linea);
                            }
                            flujo_salida.writeUTF("EOF");
                            lector.close();
                            estado = 2;
                            break;
                        } else {
                            flujo_salida.writeUTF("ERROR");
                            flujo_salida.writeUTF("El fichero no existe.");
                            estado = 2;
                            break;
                        }
                }

            } while (estado != -1);

            skCliente.close();
            System.out.println("Cliente desconectado");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
