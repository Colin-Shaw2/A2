package sample;


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class ClientConnectionHandler implements Runnable{

    private String serverFolderLocation;
    private Socket socket;
    private BufferedReader in;
    private String filePath;

    private String path ="";
    public ClientConnectionHandler(Socket socket, String serverFolderLocation){
        this.socket = socket;
        this.serverFolderLocation = serverFolderLocation;
    }

    @Override
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String usage = in.readLine();


            if ("Upload".equals(usage)) {
                filePath = in.readLine();//path of file in local computer
                upload();

            } else if ("Download".equals(usage)) {
                filePath = in.readLine();//path of file on server
                download();

            } else if ("DIR".equals(usage)) {
                sendNames();
            } else {
                System.err.println("Invalid Command and\n" +
                        "Use: Upload <file>; Download <file>; DIR;");
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                socket.close();//for the good of rome
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }


    private void sendNames() throws IOException{
        socket.shutdownInput();
        Vector<String> fileNames = new Vector<>();
        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
        File serverFolder = new File(serverFolderLocation);
        for (File file : serverFolder.listFiles()) {
            fileNames.add(file.getName());
        }
        objOut.writeObject(fileNames);
        objOut.close();
        socket.shutdownOutput();
    }

    private void upload() throws IOException{
        String fileName = new File(filePath).getName();
        PrintWriter outFile = new PrintWriter(serverFolderLocation + "/"+ fileName);
        int c;
        char ch;
        while ((c = in.read()) != -1) {
            ch = (char) c;
            outFile.print(ch);
        }
        outFile.flush();
        outFile.close();
        socket.shutdownInput();
    }

    private void download() throws IOException{
        socket.shutdownInput();
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader input = new BufferedReader(new FileReader(new File(filePath)));
        int c;
        char ch;
        while ((c = input.read()) != -1) {
            ch = (char) c;
            out.print(ch);
        }
        out.flush();
        socket.shutdownOutput();
        input.close();
    }
}