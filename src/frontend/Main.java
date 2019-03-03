package frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main_scene.fxml"));
        primaryStage.setTitle("Million Plant Map");
        String IconPath = getClass().getResource("/icon.png").toString();
        primaryStage.getIcons().add(new Image(IconPath));
        Scene scene = new Scene(root, 1100, 750);
        String StyleSheetsPath = getClass().getResource("/style.css").toString();
        scene.getStylesheets().add(StyleSheetsPath);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}
