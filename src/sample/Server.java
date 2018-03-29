package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private ServerSocket serverSocket;
    private String folderPath;



    private Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            folderPath = System.getProperty("user.dir") + "/src/server";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Server(int port, String folderPath) {
        try {
            serverSocket = new ServerSocket(port);
            this.folderPath = folderPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Listening");
        System.out.println("Port: " + this.serverSocket.getLocalPort() +
        "\nAddress: " + this.serverSocket.getInetAddress());
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(clientSocket, folderPath);
                    Thread thread = new Thread(clientConnectionHandler);
                    thread.start();
                } catch (Exception e){
                    clientSocket.close();
                }
            }
        } catch (Exception e){
            try {
                serverSocket.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        Server s = new Server(4444);
        s.run();
    }



}