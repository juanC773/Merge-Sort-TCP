import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

    public static void main(String[] args) {

        ServerSocket servidor = null;
        int puertoServidor = 5000;

        try {
            servidor = new ServerSocket(puertoServidor);
            System.out.println("Servidor iniciado");

            // Creamos una piscina de hilos con un máximo de 10 hilos
            ExecutorService pool = Executors.newFixedThreadPool(10);

            while (true) {
                Socket socketClient = servidor.accept();
                System.out.println("Cliente Conectado");

                // Al recibir una solicitud de cliente, ejecutamos la tarea en un hilo del pool
                pool.execute(new ClienteHandler(socketClient));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClienteHandler implements Runnable {
        private Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                // Lee la lista de números enviada por el cliente
                int cantidadNumeros = in.readInt();
                List<Integer> numeros = new ArrayList<>();
                for (int i = 0; i < cantidadNumeros; i++) {
                    numeros.add(in.readInt());
                }

                // Ordena la lista de números recibida del cliente usando Merge Sort
                mergeSort(numeros);

                // Construye la lista ordenada como una cadena de texto
                StringBuilder listaOrdenada = new StringBuilder();
                for (int numero : numeros) {
                    listaOrdenada.append("[").append(numero).append("] ");
                }

                // Envía la lista ordenada de vuelta al cliente
                out.writeUTF(listaOrdenada.toString());



                //Cierra el puerto
                socket.close();
                System.out.println("Cliente desconectado");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

      
    }


    // Ordenamiento

    private static void mergeSort(List<Integer> numeros) {

        if (numeros.size() > 1) {
            int medio = numeros.size() / 2;
            List<Integer> izquierda = new ArrayList<>(numeros.subList(0, medio));
            List<Integer> derecha = new ArrayList<>(numeros.subList(medio, numeros.size()));

            mergeSort(izquierda);
            mergeSort(derecha);

            int i = 0, j = 0, k = 0;
            while (i < izquierda.size() && j < derecha.size()) {
                if (izquierda.get(i) < derecha.get(j)) {
                    numeros.set(k++, izquierda.get(i++));
                } else {
                    numeros.set(k++, derecha.get(j++));
                }
            }

            while (i < izquierda.size()) {
                numeros.set(k++, izquierda.get(i++));
            }

            while (j < derecha.size()) {
                numeros.set(k++, derecha.get(j++));
            }
        }
    }
}
