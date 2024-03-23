import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        final String host = "localhost";
        int puertoCliente = 5000;

        // Cliente al servidor
        DataInputStream in;

        // Servidor al cliente
        DataOutputStream out;

        try {
            Socket sc = new Socket(host, puertoCliente);

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            // Solicitar al usuario que ingrese la lista de números
            List<Integer> numeros = leerListaNumerosDesdeConsola();

            // Envía la lista desordenada al servidor
            enviarLista(out, numeros);

            String mensaje = in.readUTF();

            System.out.println("\nLista ordenada:");
            System.out.println(mensaje);

            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Lee la lista de números ingresada por el usuario desde la consola
    private static List<Integer> leerListaNumerosDesdeConsola() {
        List<Integer> numeros = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los números separados por espacios:");

        String input = scanner.nextLine();
        String[] numerosString = input.split(" ");

        for (String numeroString : numerosString) {
            try {
                int numero = Integer.parseInt(numeroString);
                numeros.add(numero);
            } catch (NumberFormatException e) {
                System.out.println("Error: '" + numeroString + "' no es un número válido. Se omitirá.");
            }
        }

        return numeros;
    }

    // Envía una lista de números al servidor
    private static void enviarLista(DataOutputStream out, List<Integer> lista) throws IOException {
        out.writeInt(lista.size());
        for (int numero : lista) {
            out.writeInt(numero);
        }
    }
}
