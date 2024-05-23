package zad1.client;

import zad1.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private static final int MAIN_SERVER_PORT = 8080;
    private int port;
    private String wordToTranslate;
    private String languageCode;
    public Client(String wordToTranslate, String languageCode) {
        this.wordToTranslate = wordToTranslate;
        this.languageCode = languageCode;
        setPort();
    }

    public int getPort() {
        return port;
    }

    public void setPort() {
        this.port = Utils.selectPort();
    }

    public String sendRequest() {
        try (
                Socket socket = new Socket("localhost", MAIN_SERVER_PORT);
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            String request = STR."\{this.wordToTranslate} \{this.languageCode} \{getPort()}";
            output.writeUTF(request);
            return initiateServer();
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    private String initiateServer() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            try (Socket socket = serverSocket.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream())) {
                String response = input.readUTF();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Utils.removePort(this.port);
            System.out.println("Connection is closed. Port has been released.");
        }
        return null;
    }
}
