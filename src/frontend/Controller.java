package frontend;

import api.Species;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private JFXTreeTableView<SpeciesItem> treeView;
    @FXML
    private JFXTextField searchBar;
    @FXML
    private Menu recentMenu;
    @FXML
    private JFXTextArea codesLabel;

    private Stage mainStage;
    private LinkedList<String> recentSearches = new LinkedList<>();


    @FXML
    void searchClick() {
        String query = searchBar.getText();

        recentSearches.addFirst(query);

        MenuItem item = new MenuItem(query);
        item.setOnAction(t -> search(query));
        recentMenu.getItems().add(0, item);

        search(searchBar.getText());
    }

    private void search(String queries) {
        codesLabel.setText("");

        searchBar.setText(queries);
        searchBar.positionCaret(searchBar.getText().length());

        JFXSpinner spinner = new JFXSpinner();
        spinner.setRadius(20.0);
        spinner.applyCss();
        treeView.setPlaceholder(spinner);

        ObservableList<SpeciesItem> items = FXCollections.observableArrayList();

        Thread thread = new Thread(() -> runQuery(queries, items));
        thread.start();

        final TreeItem<SpeciesItem> root = new RecursiveTreeItem<>(items, RecursiveTreeObject::getChildren);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    private void runQuery(String queries, ObservableList<SpeciesItem> items) {
        for (String query : queries.split(",")) {
            QueryHandler collection = new QueryHandler();
            QueryHandlerResult result = collection.query(query);

            if(result != null) {
                List<Species> results = result.getSpeciesList();
                String[][] geoCodes = result.getGeoCodes();

                for (Species sp : results) {
                    items.add(new SpeciesItem(sp));
                }

                Platform.runLater(() -> codesLabel.setText(codesLabel.getText() + formatCodes(query.trim(), geoCodes)));
            }
        }
        if (items.size() == 0) {
            Label noResults = new Label("No results have been found");
            noResults.setFont(new Font("Trebuchet MS", 18));
            Platform.runLater(() -> treeView.setPlaceholder(noResults));
        }
    }

    private String formatCodes(String name, String[][] codes) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s\n-----------------------\n", name));
        if(codes[0][0].equals("unknown")){
            result.append("The World Checklist of Selected Plant Families does not contain the distribution for this plant.");
        }
        else {
            for (String[] code : codes) {
                result.append(String.format("%s : %s    ", code[0], code[1]));
            }
        }
        result.append("\n\n");
        return result.toString();
    }

    @FXML
    public void keyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            searchClick();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXTreeTableColumn<SpeciesItem, JFXCheckBox> checkbox = new JFXTreeTableColumn<>("");
        checkbox.setPrefWidth(50);
        checkbox.setCellValueFactory(param -> param.getValue().getValue().getCheckbox());
        
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
        codes.setPrefWidth(200);
        codes.setCellValueFactory(param -> param.getValue().getValue().getCodes());

        JFXTreeTableColumn<SpeciesItem, String> author = new JFXTreeTableColumn<>("Author");
        author.setPrefWidth(200);
        author.setCellValueFactory(param -> param.getValue().getValue().getAuthor());

        JFXTreeTableColumn<SpeciesItem, String> isAccepted = new JFXTreeTableColumn<>("Accepted?");
        isAccepted.setPrefWidth(100);
        isAccepted.setCellValueFactory(param -> param.getValue().getValue().isSynonym() ? new ReadOnlyStringWrapper(" ") : new ReadOnlyStringWrapper("X"));

        JFXTreeTableColumn<SpeciesItem, String> isBasionym = new JFXTreeTableColumn<>("Basionym?");
        isBasionym.setPrefWidth(100);
        isBasionym.setCellValueFactory(param -> param.getValue().getValue().isBasionym() ? new ReadOnlyStringWrapper("X") : new ReadOnlyStringWrapper("     "));

        treeView.getColumns().setAll(checkbox, canonicalName, species, genus, family, codes, isAccepted, isBasionym, author);

        ImageView imageView = new ImageView("file:resources/about_m.png");
        imageView.setOpacity(0.8);
        treeView.setPlaceholder(imageView);
    }

    @FXML
    public void csvClick() {
        File dest = fileChooser();

        if (dest != null) {
            CSVConverter.exportCSV(searchBar.getText(), dest);
        }
    }

    private File fileChooser() {
        mainStage = (Stage) searchBar.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV as...");

        FileChooser.ExtensionFilter csv = new FileChooser.ExtensionFilter("CSV file (*.csv)", "*.csv");
        FileChooser.ExtensionFilter other = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");

        fileChooser.getExtensionFilters().setAll(csv, other);
        return fileChooser.showSaveDialog(mainStage);
    }


    @FXML
    public void aboutClick() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainStage);

        Image image = new Image("file:resources/credits.jpg");
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