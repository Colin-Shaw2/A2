package sample;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server implements Runnable{
    protected ServerSocket serverSocket;
    protected String ServerPath;

    public static int SERVER_PORT = 16789;
    public static int MAX_CLIENTS = 4;

    public Server() {
        try {
            serverSocket = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}