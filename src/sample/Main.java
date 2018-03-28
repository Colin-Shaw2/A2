package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {


    private Canvas canvas;

    @Override
    public void start(Stage stage) throws Exception {
        int width = 1200;
        int height = 600;


        VBox vBox = new VBox();


        Button downloadButton = new Button("Download");
        Button uploadButton = new Button("Upload");
        GridPane menuBar = new GridPane();

        menuBar.add(downloadButton, 0, 0);
        menuBar.add(uploadButton, 1, 0);
        vBox.getChildren().add(menuBar);

        SplitPane splitPane1 = new SplitPane();
        splitPane1.setPrefSize(width, height);

        ListView<String> clientDirectory = new ListView<>();
        populateDirectory(new File("src/Server"), clientDirectory);

        ListView<String> serverDirectory = new ListView<>();
        populateDirectory(new File("src/Client"), serverDirectory);


        splitPane1.getItems().addAll(serverDirectory, clientDirectory);

        vBox.getChildren().add(splitPane1);

        Scene scene = new Scene(new Group(vBox), width, height);
        scene.setFill(Color.GHOSTWHITE);
        stage.setScene(scene);
        stage.setTitle("File Sharer A2");


        stage.setScene(scene);
        stage.show();


        downloadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO download(fileToDownload)
                //                download();
            }
        });

        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO upload(fileToUpload)
                //                upload();
            }
        });
    }


    private void populateDirectory(File dir, ListView<String> parentItem) {
        File[] files = dir.listFiles();
        for (File file : files) {
            parentItem.getItems().add(file.getName());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
