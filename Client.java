
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER = "localhost";
    private static final int PORT = 12345;

    private JFrame frame = new JFrame("Chat Client");
    private JTextArea chatArea = new JTextArea(20, 50);
    private JTextField inputField = new JTextField(40);
    private PrintWriter out;

    public Client() {
        chatArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(chatArea), BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(inputField);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            out.println(inputField.getText());
            inputField.setText("");
        });

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try {
            Socket socket = new Socket(SERVER, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        chatArea.append(line + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            chatArea.append("Unable to connect to server.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}
