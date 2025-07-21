import java.io.*;
import java.net.*;
import java.util.Set;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static Set<PrintWriter> writers;

    public ClientHandler(Socket socket, Set<PrintWriter> writers) {
        this.socket = socket;
        ClientHandler.writers = writers;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            synchronized (writers) {
                writers.add(out);
            }

            String message;
            while ((message = in.readLine()) != null) {
                synchronized (writers) {
                    for (PrintWriter writer : writers) {
                        writer.println(message);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (writers) {
                writers.remove(out);
            }
        }
    }
}
