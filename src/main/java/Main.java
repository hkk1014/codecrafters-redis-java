import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

//      Uncomment this block to pass the first stage
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.

            clientSocket = serverSocket.accept();

            while (clientSocket != null) {
                clientSocket.getOutputStream().write("+PONG\r\n".getBytes());

            }


//            handle(clientSocket);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    private static void handle(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            char[] buffer = new char[100];
            final int readBytes = in.read(buffer);
            final String[] request = parseRequest(buffer, readBytes);
            if ("ping".equals(request[1])) {
                if (request.length == 2) {
                    out.print("+PONG\r\n");
                } else if (request.length == 4) {
                    out.print("+" + request[3] + "\r\n");
                } else {
                    out.print("-Error!\r\n");
                }
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String[] parseRequest(char[] buffer, int readBytes) {
        return String.valueOf(buffer, 4, readBytes - 4).split("\r\n");
    }
}
