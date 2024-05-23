package zad1.mainServer;

import zad1.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer {
    static Map<String, DictionaryServer> activeServers = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {

        ServerSocket ss = null;
        try {
            int port = 8080;
            ss =  new ServerSocket(port);
        } catch(Exception exc) {
            exc.printStackTrace();
        }
        try {
            while (true) {
                try (
                        Socket socket = ss.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream())
                ) {
                    String request = input.readUTF();
                    String clientPort = request.split(" ")[2];
                    String dictionaryServer = request.split(" ")[1];
                    if (activeServers.containsKey(dictionaryServer)) {
                        DictionaryServer connection1 = activeServers.get(dictionaryServer);
                        int port = connection1.getPort();
                        Socket newSocket = new Socket("localhost", port);
                        Utils.writeToClient(newSocket, request);
                    } else {
                        String wrongLanguageResponse = "Wrong language.";
                        Socket clientSocket = new Socket("localhost", Integer.parseInt(clientPort));
                        Utils.writeToClient(clientSocket, wrongLanguageResponse);
                        DictionaryServer langServer = new DictionaryServer(dictionaryServer);
                        activeServers.put(dictionaryServer, langServer);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
