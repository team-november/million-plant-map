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

import java.util.*;

public class MainPage{
    public Button searchButton;
    @FXML private TextField plantName;
    @FXML private GridPane gridSynonyms;
    @FXML private ScrollPane scrollPane;

    private void setGridSynonyms(List<Species> synonyms){
        //gridepane settings
        gridSynonyms.setVgap(4);
        gridSynonyms.setGridLinesVisible(true);
        gridSynonyms.setLayoutY(130*7);
        gridSynonyms.setLayoutX(1000);
        gridSynonyms.setMinWidth(980);
        gridSynonyms.setMaxWidth(980);
        scrollPane.setMinWidth(1000);
        scrollPane.setMaxWidth(1000);

        //info to print
        List<Text> texts = new ArrayList<>();
        texts.add(new Text("Scientific Name"));
        texts.add(new Text("Species"));
        texts.add(new Text("Genus"));
        texts.add(new Text("Family"));
        texts.add(new Text("BHcode"));
        texts.add(new Text("FEcode"));
        texts.add(new Text("Author"));
        texts.add(new Text("Is Accepted"));
        //set font, size... for the text boxes
        for(int j=0;j<texts.size();j++){
            texts.get(j).setFont(Font.font("roboto", FontPosture.REGULAR, 16));
            texts.get(j).setTranslateY(20);
            texts.get(j).setTranslateX(200*j);
        }
        //create a pane to insert into the grid
        Pane synonymPane = new Pane();
        //add to it the wanted fields
        for(int j=0;j<texts.size();j++){
            synonymPane.getChildren().add(texts.get(j));
        }
        //add the pane to a grid
        gridSynonyms.addRow(0, synonymPane);




        for (int i = 0; i < synonyms.size(); i++) {
            Species current = synonyms.get(i);

            //info to print
            texts = new ArrayList<>();
            texts.add(new Text(current.getScientificName()));
            texts.add(new Text(current.getSpecies()));
            texts.add(new Text(current.getGenus()));
            texts.add(new Text(current.getFamily()));
            texts.add(new Text(current.getBHcode()));
            texts.add(new Text(current.getFEcode()));
            texts.add(new Text(current.getAuthorship()));
            if(!current.isSynonym()){
                texts.add(new Text("Yes"));
            }else{
                texts.add(new Text("No"));

            }

            //set font, size... for the text boxes
            for(int j=0;j<texts.size();j++){
                texts.get(j).setFont(Font.font("roboto", FontPosture.REGULAR, 16));
                texts.get(j).setTranslateY(20);
                texts.get(j).setTranslateX(200*j);
            }

            //create a pane to insert into the grid
            synonymPane = new Pane();
            //add to it the wanted fields
            for(int j=0;j<texts.size();j++){
                synonymPane.getChildren().add(texts.get(j));
            }
            //add the pane to a grid
            gridSynonyms.addRow(i+1, synonymPane);
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
        // delate previous results of query if any
        deleteRow(gridSynonyms);
        // take text from box
        String plantNameString=plantName.getText();
        DataCollected dc = new DataCollected();
        List<Species> synonyms = dc.DataToPrint(plantNameString);
        // fill the grid with the synonyms data
        setGridSynonyms(synonyms);
    }

    public void pressConvertCSV(ActionEvent actionEvent)
    {
        // take text from box
        String plantNameString=plantName.getText();
        // call function to convert to csv
        CSVConverter cs = new CSVConverter();
        cs.printCSV(plantNameString);
    }


}
