import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server dang cho ket noi...");

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("Client da ket noi!");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                String msg;

                while ((msg = reader.readLine()) != null) {
                    System.out.println("Client: " + msg);
                }

                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}