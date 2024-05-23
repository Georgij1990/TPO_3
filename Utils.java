package zad1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    private static List<Integer> ports = new ArrayList<>();

    public static synchronized void addPort(Integer port) {
        ports.add(port);
    }

    public static synchronized void removePort(Integer port) {
        ports.remove(port);
    }

    public static synchronized boolean containsPort(Integer port) {
        return ports.contains(port);
    }

    public static synchronized List<Integer> getPorts() {
        return new ArrayList<>(ports);
    }

    public static int selectPort() {
        Random random = new Random();
        int port;
        synchronized (Utils.class) {
            int randomNumber = random.nextInt(65536 - 1024) + 1024;
            while (ports.contains(randomNumber) && randomNumber != 8080) {
                randomNumber = random.nextInt(65536 - 1024) + 1024;
            }
            port = randomNumber;
            addPort(port);
        }
        return port;
    }

    public static synchronized void writeToClient(Socket socket, String response) {
        try (DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            output.writeUTF(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
