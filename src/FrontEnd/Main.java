package frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    //TODO: Feedback
    //      About scene
    //      move author to the end and make it bigger
    //      maybe make columns resize according to window size
    //      export multiple
    //      remove initial text in tableview
    //      improve dataCollection class
    //      system doesn't actually work when inputting synonym
    //      Look at query result class and maybe I should be using them
    //      cursor moving to start
    //      autocomplete
    //      row text justified differently or padding on the left
    //      maybe CSV maintains the order from the table
    //      editable column for actual code, stored in the database and a note field
    //      add jfx spinner while results are loading
    //      hamburger menu instead of javafx
    //      maybe try dark
    //      make the package names match the conventions
    //      remove useless files from the repository

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main_scene.fxml"));
        primaryStage.setTitle("Million Plant Map");
        primaryStage.getIcons().add(new Image("file:resources/icon.png"));

        Scene scene = new Scene(root,1050, 850);
        scene.getStylesheets().add("file:resources/style.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}
