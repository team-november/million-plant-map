package FrontEnd;

import api.APIServiceImpl;
import api.Species;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MainPage{
    public Button searchButton;
    @FXML private TextField plantName;
    @FXML private GridPane gridSynonyms;
    @FXML private ScrollPane scrollPane;

    private void setGridSynonyms(Species[] synonyms){
        //gridepane settings
        gridSynonyms.setVgap(4);
        gridSynonyms.setGridLinesVisible(true);
        gridSynonyms.setLayoutY(130*7);
        gridSynonyms.setLayoutX(1000);
        gridSynonyms.setMinWidth(980);
        gridSynonyms.setMaxWidth(980);
        scrollPane.setMinWidth(1000);
        scrollPane.setMaxWidth(1000);


        for (int i = 0; i < synonyms.length; i++) {
            Species current = synonyms[i];

            //create text boxs containing the data
            Text scientificName = new Text(current.getScientificName());
            Text family = new Text(current.getFamily());
            Text canonicalName = new Text(current.getCanonicalName());


            //set font, size... for the text boxes
            scientificName.setFont(Font.font("roboto", FontPosture.REGULAR, 16));
            family.setFont(Font.font("roboto", FontPosture.REGULAR, 20));
            canonicalName.setFont(Font.font("roboto", FontPosture.REGULAR, 20));
            //position the scientificName
            scientificName.setTranslateX(10);
            scientificName.setTranslateY(20);

            //position the chance of family
            family.setTranslateX(400);
            family.setTranslateY(20);

            //position the canonicalName
            canonicalName.setTranslateX(600);
            canonicalName.setTranslateY(20);


            //create a pane to insert into the grid
            Pane synonymPane = new Pane();
            //add to it the wanted fields
            synonymPane.getChildren().add(scientificName);
            synonymPane.getChildren().add(family);
            synonymPane.getChildren().add(canonicalName);

            //add the pane to a grid
            gridSynonyms.addRow(i, synonymPane);
        }

    }

    static void deleteRow(GridPane grid) {
        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : grid.getChildren()) {
            // get index from child
            Integer rowIndex = GridPane.getRowIndex(child);

            // handle null values for index=0
            int r = rowIndex == null ? 0 : rowIndex;
            // collect matching rows for deletion
            deleteNodes.add(child);

        }
        // remove nodes from row
        grid.getChildren().removeAll(deleteNodes);
    }

    // When button search is pressed
    @FXML
    public void pressSearch(ActionEvent actionEvent)
    {

        deleteRow(gridSynonyms);
        System.out.println(plantName.getText());
        //take text from box
        String plantNameString=plantName.getText();
        //search on API
        APIServiceImpl api = APIServiceImpl.getInstance();
        Species acceptedSpecies=api.getAcceptedSpecies(plantNameString);
        System.out.println("the accepted name is:"+acceptedSpecies.getCanonicalName());
        Species[] synonyms = api.getSynonyms(api.getAcceptedKey(acceptedSpecies.getCanonicalName()));
        for(int i=0;i<synonyms.length;i++){
            System.out.println("the synonym "+ (i+1)+ " is: "+synonyms[i].getCanonicalName());
        }
        // fill the grid with the synonyms data
        setGridSynonyms(synonyms);
    }
}
