package frontend;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @FXML
    private JFXTextField searchBar;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main_scene.fxml"));
        primaryStage.setTitle("Million Plant Map");
        primaryStage.getIcons().add(new Image("file:resources/icon.png"));
        Scene scene = new Scene(root, 950, 750);
        scene.getStylesheets().add("file:resources/style.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}
