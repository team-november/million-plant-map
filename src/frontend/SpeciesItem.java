package frontend;

import api.Species;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.DatabaseHandler;
import database.IndexScheme;
import database.Synonym;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;

public class SpeciesItem extends RecursiveTreeObject<SpeciesItem> {

    private JFXCheckBox checkBox;
    private JFXTextField textField, notesField;

    private Species species;

    public SpeciesItem(Species species) {
        this.species = species;

        //databaseHandler = DatabaseHandler.getInstance();

        checkBox =  new JFXCheckBox();
        checkBox.selectedProperty().setValue(species.isInHerbarium());
        checkBox.selectedProperty().addListener(this::changed);

        JFXTextField textField = new JFXTextField(species.getCodes());
        textField.setOnKeyPressed(s->{
            if(s.getCode().equals(KeyCode.ENTER)){
                textField.getParent().requestFocus();
                changed(null, null, checkBox.selectedProperty().getValue());
            }
        });

        JFXTextField notesField = new JFXTextField(species.getCodes());
        textField.setOnKeyPressed(s->{
            if(s.getCode().equals(KeyCode.ENTER)){
                notesField.getParent().requestFocus();
                changed(null, null, checkBox.selectedProperty().getValue());
            }
        });
    }


    private void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//        if (newValue) {
//            databaseHandler.insertSynonym(synonym);
//        } else {
//            databaseHandler.deleteSynonym(synonym);
//        }
    }


    public ObjectProperty<JFXCheckBox> getCheckbox(){
        return new ReadOnlyObjectWrapper<>(checkBox);
    }

    public StringProperty getScientificName(){
        return new ReadOnlyStringWrapper(species.getScientificName());
    }

    public StringProperty getCanonicalName(){
        return new ReadOnlyStringWrapper(species.getCanonicalName());
    }

    public StringProperty getSpecies(){
        return new ReadOnlyStringWrapper(species.getSpecies());
    }

    public StringProperty getGenus(){
        return new ReadOnlyStringWrapper(species.getGenus());
    }

    public StringProperty getFamily(){
        return new ReadOnlyStringWrapper(species.getFamily());
    }

    public ObjectPropertyBase<JFXTextField> getCodes() {
        return new ReadOnlyObjectWrapper<>(textField);
    }

    public StringProperty getAuthor(){
        return new ReadOnlyStringWrapper(species.getAuthorship());
    }

    public boolean isSynonym(){
        return species.isSynonym();
    }

    public boolean isBasionym(){
        return species.isBasionym();
    }

    public ObjectPropertyBase<JFXTextField> getNotes() {
        return new ReadOnlyObjectWrapper<>(notesField);
    }


}
