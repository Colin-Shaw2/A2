import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Main extends Application {


    private ListView<String> serverView, clientView;
    private Vector<String> serverFileList = new Vector<>();
    private Vector<String> clientFileList = new Vector<>();
    private VBox vBox;

    private static String computerName;
    private static String sharedFolderPath;
    private SplitPane splitPane1;
    private int width;
    private int height;
    private Stage mainStage;

    @Override
    public void start(Stage stage){
        mainStage = stage;
        width = 400;//1200;
        height = 150;//600;


        vBox = new VBox();


        Button downloadButton = new Button("Download");
        Button uploadButton = new Button("Upload");
        GridPane menuBar = new GridPane();

        menuBar.add(downloadButton, 0, 0);
        menuBar.add(uploadButton, 1, 0);
        vBox.getChildren().add(menuBar);

        splitPane1 = new SplitPane();
        splitPane1.setPrefSize(width, height);

        updateDirectories();


        splitPane1.getItems().addAll(clientView, serverView);

        vBox.getChildren().add(splitPane1);

        Scene scene = new Scene(new Group(vBox), width, height);
        scene.setFill(Color.GHOSTWHITE);
        mainStage.setScene(scene);
        mainStage.setTitle("File Sharer A2");


        mainStage.setScene(scene);
        mainStage.show();


        downloadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                download();
            }
        });

        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                upload();
            }
        });
    }

    private void download() {
        File filedown = new File(serverView.getSelectionModel().getSelectedItem());
        try {
            Socket socket = new Socket(computerName, 4444);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("Download");
            out.println(System.getProperty("user.dir") + "/src/Server/" + filedown);
            System.out.println(System.getProperty("user.dir") + "/src/Server/" + filedown);
            out.flush();
            socket.shutdownOutput();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter outFile = new PrintWriter(System.getProperty("user.dir") + "/src/Client/" + filedown);
            int c;
            char ch;
            while ((c = in.read()) != -1) {
                ch = (char) c;
                outFile.print(ch);
            }
            outFile.flush();
            outFile.close();
            socket.shutdownInput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateDirectories();
    }

    private void upload() {
        File fileUp = new File(clientView.getSelectionModel().getSelectedItem());
        try {
            Socket socket = new Socket(computerName, 4444);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("Upload");
            out.println(System.getProperty("user.dir") + "/src/Client/" + fileUp);
            out.flush();
            System.out.println(System.getProperty("user.dir") + "/src/Client/" + fileUp);
            BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/Client/" + fileUp));
            int c;
            char ch;
            while ((c = in.read()) != -1) {
                ch = (char) c;
                out.print(ch);
            }
            out.flush();
            in.close();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateDirectories();
    }


    private void updateVisuals(){
        updateDirectories();


        splitPane1.getItems().addAll(clientView, serverView);

        vBox.getChildren().add(splitPane1);

        Scene scene = new Scene(new Group(vBox), width, height);
        scene.setFill(Color.GHOSTWHITE);
        mainStage.setScene(scene);
        mainStage.setTitle("File Sharer A2");


        mainStage.setScene(scene);
        mainStage.show();

    }


    private void updateDirectories(){
        clientView = new ListView<>();
        populateClientDirectory(new File(sharedFolderPath + "/src/Client"), clientView, clientFileList);

        serverView = new ListView<>();
        populateServerDirectory(serverView, serverFileList);
    }

    private void populateClientDirectory(File dir, ListView<String> parentItem, Vector<String> obList) {
        File[] files = dir.listFiles();
        for (File file : files) {
            obList.add(file.getName());
        }
        parentItem.setItems(FXCollections.observableList(obList));
    }

    private void populateServerDirectory(ListView<String> parentItem, Vector<String> obList){
        try {
            Socket socket = new Socket(computerName, 4444);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("DIR");
            out.flush();
            socket.shutdownOutput();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            try {
                obList = (Vector<String>) in.readObject();
                parentItem.setItems(FXCollections.observableList(obList));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        computerName = args[0];
        sharedFolderPath = args[1];
        launch(args);
    }
}
