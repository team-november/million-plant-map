package FrontEnd;

import api.Species;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

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

    private LinkedList<String> recentSearches = new LinkedList<>();


    void search(String text){
        searchBar.setText(text);

        DataCollected collection = new DataCollected();
        ObservableList<SpeciesItem> items = FXCollections.observableArrayList();

        List<Species> results = collection.DataToPrint(text);

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

        JFXTreeTableColumn<SpeciesItem, String> BHCode = new JFXTreeTableColumn<>("BH Code");
        BHCode.setPrefWidth(100);
        BHCode.setCellValueFactory(param -> param.getValue().getValue().getBHCode());

        JFXTreeTableColumn<SpeciesItem, String> FECode = new JFXTreeTableColumn<>("FE Code");
        FECode.setPrefWidth(100);
        FECode.setCellValueFactory(param -> param.getValue().getValue().getFECode());
        
        JFXTreeTableColumn<SpeciesItem, String> isAccepted = new JFXTreeTableColumn<>("Accepted?");
        isAccepted.setPrefWidth(100);
        isAccepted.setCellValueFactory(param -> param.getValue().getValue().isSynonym() ? new ReadOnlyStringWrapper(" ") : new ReadOnlyStringWrapper("X"));

        JFXTreeTableColumn<SpeciesItem, String> isBasionym = new JFXTreeTableColumn<>("Basionym?");
        isBasionym.setPrefWidth(100);
        isBasionym.setCellValueFactory(param -> param.getValue().getValue().isBasionym() ? new ReadOnlyStringWrapper("X") : new ReadOnlyStringWrapper("     "));

        treeView.getColumns().setAll(canonicalName, species, genus, family, BHCode, FECode, isAccepted, isBasionym);
    }

    public void keyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            searchClick();
        }
    }
}
