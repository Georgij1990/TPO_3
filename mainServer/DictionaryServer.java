package zad1.mainServer;

import zad1.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class DictionaryServer extends Thread {
    private String serverName;
    private ServerSocket serverSocket;
    private int port;

    private volatile boolean serverRunning = true;

    public DictionaryServer(String serverName) throws IOException {
        setPort();
        this.serverSocket = new ServerSocket(this.port);
        this.serverName = serverName;
        System.out.println(STR."\{serverName} server has been launched");
        start();
    }

    public void run() {
        System.out.println(Thread.currentThread().getName());
        while (serverRunning) {
            try {
                serviceRequests();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        try {
            this.serverSocket.close();
            Utils.removePort(this.port);
        } catch (Exception exc) {

        }
    }

    public int getPort() {
        return port;
    }

    public void setPort() {
        this.port = Utils.selectPort();
    }

    private void serviceRequests() {
        try (Socket socket = serverSocket.accept();
             DataInputStream input = new DataInputStream(socket.getInputStream())) {
            String line = input.readUTF();
            List<String> req = List.of(line.split(" "));
            String wordToTranslate = req.get(0).toLowerCase(Locale.ROOT);
            String languageCode = req.get(1).toUpperCase();
            String port = req.get(2);
            Map<String, String> dictionary;
            String translation = null;
            try {
                dictionary = createDictionary(languageCode);
                translation = dictionary.get(wordToTranslate);
            } catch (RuntimeException e) {
                System.out.println(STR."There is no file for the \{this.serverName} dictionary");
            }
            String response = (translation == null || translation.isEmpty()) ? "Translation not found!" : translation;
            Socket clientSocket = new Socket("localhost", Integer.parseInt(port));
            Utils.writeToClient(clientSocket, response);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private Map<String, String> createDictionary(String languageCode) throws IOException {
        Map<String, String> dictionary = new HashMap<>();
        String file = "C:\\Studying\\PJATK, STUDJA\\Semestr6\\TPO\\Zadanie3\\TPO3_KG_S24171\\src\\zad1\\mainServer\\" + languageCode + ".txt";
        BufferedReader br = null;
        try {
            File fl = new File(file);
            br = new BufferedReader(new FileReader(fl));
            String line;
            while ((line = br.readLine()) != null) {
                String[] keyValuePair = line.split(" ");
                dictionary.put(keyValuePair[0], keyValuePair[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            br.close();
        }
        return dictionary;
    }
}
