package frontend;

import api.APIServiceImpl;
import api.QueryResult;
import api.Species;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public StackPane stackPane;
    @FXML
    private JFXTreeTableView<SpeciesItem> treeView;
    @FXML
    private JFXTextField searchBar;
    @FXML
    private Menu recentMenu;
    @FXML
    private JFXTextArea codesLabel;

    private Stage mainStage;

    private List<QueryResult> currentResults;

    private LinkedList<String> recentSearches = new LinkedList<>();

    private String[] autocompleteResults = null;
    private String autocompleteSearch;
    private int autocompleteIndex = 0;

    static String formatCodes(String name, String[][] codes) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s\n-----------------------\n", name));
        if (codes[0][0].equals("unknown")) {
            result.append("The World Checklist of Selected Plant Families does not contain the distribution for this plant.");
        } else {
            for (String[] code : codes) {
                result.append(String.format("%s : %s    ", code[0], code[1]));
            }
        }
        result.append("\n\n");
        return result.toString();
    }

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
        currentResults = new LinkedList<>();

        for (String query : queries.split(",")) {
            QueryResult result = QueryHandler.query(query);
            currentResults.add(result);

            if (result != null) {
                String[][] geoCodes = result.getGeoCodes();

                for (Species sp : result) {
                    items.add(new SpeciesItem(sp));
                }
                Platform.runLater(() -> codesLabel.setText(codesLabel.getText()
                        + formatCodes(result.getAcceptedName().getCanonicalName(), geoCodes)));
            }
        }
        if (items.size() == 0) {
            Label noResults = new Label("No results have been found");
            noResults.setFont(new Font("Trebuchet MS", 18));
            Platform.runLater(() -> treeView.setPlaceholder(noResults));
        }
    }

    @FXML
    public void keyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER) && keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
            searchClick();
        }
    }

    @FXML
    public void arrowPress(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DOWN)) {
            if (autocompleteResults == null) {
                autocompleteSearch = searchBar.getText();
                autocompleteResults = APIServiceImpl.getInstance().autocomplete(autocompleteSearch);
            }
            if (autocompleteResults.length > autocompleteIndex) {
                autocompleteIndex++;

                searchBar.setText(autocompleteResults[autocompleteIndex - 1]);
                searchBar.positionCaret(searchBar.getText().length());
            }
        }else if(event.getCode().equals(KeyCode.UP)) {
            if (autocompleteIndex > 1) {
                autocompleteIndex--;

                searchBar.setText(autocompleteResults[autocompleteIndex - 1]);
                searchBar.positionCaret(searchBar.getText().length());
            }
        }else if(event.getCode().equals(KeyCode.LEFT)) {
            if(autocompleteResults!=null) {
                searchBar.setText(autocompleteSearch);
                searchBar.positionCaret(searchBar.getText().length());

                autocompleteIndex = 0;
            }
        } else if (event.getCode().isLetterKey() || event.getCode().equals(KeyCode.BACK_SPACE)){
            autocompleteResults = null;
            autocompleteIndex = 0;
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

        JFXTreeTableColumn<SpeciesItem, String> author = new JFXTreeTableColumn<>("Author");
        author.setPrefWidth(150);
        author.setCellValueFactory(param -> param.getValue().getValue().getAuthor());

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

        JFXTreeTableColumn<SpeciesItem, String> isAccepted = new JFXTreeTableColumn<>("Accepted?");
        isAccepted.setPrefWidth(100);
        isAccepted.setCellValueFactory(param -> param.getValue().getValue().isSynonym() ? new ReadOnlyStringWrapper(" ") : new ReadOnlyStringWrapper("X"));

        JFXTreeTableColumn<SpeciesItem, String> isBasionym = new JFXTreeTableColumn<>("Basionym?");
        isBasionym.setPrefWidth(100);
        isBasionym.setCellValueFactory(param -> param.getValue().getValue().isBasionym() ? new ReadOnlyStringWrapper("X") : new ReadOnlyStringWrapper("     "));

        treeView.getColumns().setAll(checkbox, canonicalName, author, species, genus, family, codes, isAccepted, isBasionym);

        ImageView imageView = new ImageView("file:resources/about_m.png");
        imageView.setOpacity(0.8);
        treeView.setPlaceholder(imageView);
    }

    @FXML
    public void csvClick() {
        File dest = fileChooser();

        if (dest != null) {
            boolean success = CSVConverter.exportCSV(currentResults, dest);
            if (!success) {
                showErrorDialog();
            }
        }
    }

    private void showErrorDialog() {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Error, could not write to file"));
        content.setBody(new Text("Please ensure that the file is closed and try again"));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction(event -> dialog.close());
        button.setStyle("-jfx-button-type: RAISED;");
        button.setPrefSize(70, 30);
        content.setActions(button);
        dialog.show();
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
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/credits_scene.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            dialog.setScene(new Scene(root1));
            dialog.show();
        }catch (IOException e){
        }
        dialog.setTitle("About Million Plant Map");
        dialog.getIcons().add(new Image("file:resources/icon.png"));
        dialog.setResizable(false);
        dialog.sizeToScene();
        dialog.showAndWait();
    }

}