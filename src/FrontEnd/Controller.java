package frontend;

import api.Species;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXTreeTableView<SpeciesItem> treeView;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private Menu recentMenu;

    private Stage mainStage;

    private LinkedList<String> recentSearches = new LinkedList<>();


    void search(String text){
        searchBar.setText(text);

        QueryHandler collection = new QueryHandler();
        ObservableList<SpeciesItem> items = FXCollections.observableArrayList();

        QueryHandlerResult result = collection.query(text);
        List<Species> results = result.getSpeciesList();
        String[][] geoCodes = result.getGeoCodes();

        for(Species sp: results){
            items.add(new SpeciesItem(sp));
        }

        final TreeItem<SpeciesItem> root = new RecursiveTreeItem<>(items, RecursiveTreeObject::getChildren);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    @FXML
    void searchClick() {
        String query = searchBar.getText();
        recentSearches.add(query);

        MenuItem item = new MenuItem(query);
        item.setOnAction(t -> search(query));

        recentMenu.getItems().add(0, item);
        Collections.reverse(recentSearches);

        search(searchBar.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXTreeTableColumn<SpeciesItem, String> canonicalName = new JFXTreeTableColumn<>("Canonical Name");
        canonicalName.setPrefWidth(200);
        canonicalName.setCellValueFactory(param -> param.getValue().getValue().getCanonicalName());
        
        JFXTreeTableColumn<SpeciesItem, String> species = new JFXTreeTableColumn<>("Species");
        species.setPrefWidth(150);
        species.setCellValueFactory(param -> param.getValue().getValue().getSpecies());
        
        JFXTreeTableColumn<SpeciesItem, String> genus = new JFXTreeTableColumn<>("Genus");
        genus.setPrefWidth(100);
        genus.setCellValueFactory(param -> param.getValue().getValue().getGenus());

        JFXTreeTableColumn<SpeciesItem, String> family = new JFXTreeTableColumn<>("Family");
        family.setPrefWidth(100);
        family.setCellValueFactory(param -> param.getValue().getValue().getFamily());

        JFXTreeTableColumn<SpeciesItem, String> codes = new JFXTreeTableColumn<>("Codes");
        codes.setPrefWidth(300);
        codes.setCellValueFactory(param -> param.getValue().getValue().getCodes());

        JFXTreeTableColumn<SpeciesItem, String> author = new JFXTreeTableColumn<>("Author");
        author.setPrefWidth(100);
        author.setCellValueFactory(param -> param.getValue().getValue().getAuthor());

        JFXTreeTableColumn<SpeciesItem, String> isAccepted = new JFXTreeTableColumn<>("Accepted?");
        isAccepted.setPrefWidth(100);
        isAccepted.setCellValueFactory(param -> param.getValue().getValue().isSynonym() ? new ReadOnlyStringWrapper(" ") : new ReadOnlyStringWrapper("X"));

        JFXTreeTableColumn<SpeciesItem, String> isBasionym = new JFXTreeTableColumn<>("Basionym?");
        isBasionym.setPrefWidth(100);
        isBasionym.setCellValueFactory(param -> param.getValue().getValue().isBasionym() ? new ReadOnlyStringWrapper("X") : new ReadOnlyStringWrapper("     "));

        treeView.getColumns().setAll(canonicalName, species, genus, family, codes, author, isAccepted, isBasionym);

        ImageView imageView = new ImageView("file:resources/about_m.jpg");
        imageView.setOpacity(0.8);
        treeView.setPlaceholder(imageView);
    }

    public void keyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            searchClick();
        }
    }

    private File fileChooser(){
        mainStage = (Stage) searchBar.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV as...");

        FileChooser.ExtensionFilter csv = new FileChooser.ExtensionFilter("CSV file (*.csv)", "*.csv");
        FileChooser.ExtensionFilter other = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");

        fileChooser.getExtensionFilters().setAll(csv, other);

        return fileChooser.showSaveDialog(mainStage);
    }

    public void csvClick(ActionEvent actionEvent) {
        File dest = fileChooser();

        if(dest != null) {
            CSVConverter.exportCSV(searchBar.getText(), dest);
        }
    }


    public void aboutClick(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainStage);

        Image image = new Image("file:resources/about.jpg");
        ImageView imageView = new ImageView(image);

        GridPane gridPane = new GridPane();
        gridPane.getChildren().add(imageView);

        Scene scene = new Scene(gridPane);

        dialog.setTitle("About Million Plant Map");
        dialog.getIcons().add(new Image("file:resources/icon.png"));
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.sizeToScene();
        dialog.showAndWait();
    }
}
