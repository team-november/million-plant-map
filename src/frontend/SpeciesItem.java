package frontend;

import api.Species;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;

public class SpeciesItem extends RecursiveTreeObject<SpeciesItem> {

    private JFXCheckBox checkBox;
    private JFXTextField textField, noteField;
    //private String newCode = "";
    private String oldCode = "";
    private String oldNote = "";

    private DatabaseAPI databaseAPI;

    private Species species;

    public SpeciesItem(Species species) {
        this.species = species;

        // to access the database
        databaseAPI = new DatabaseAPI();

        checkBox = new JFXCheckBox();
        checkBox.selectedProperty().setValue(species.isInHerbarium());
        checkBox.selectedProperty().addListener(this::changed);

        textField = new JFXTextField(species.getCodes());
        oldCode = species.getCodes();
        textField.setOnKeyPressed(s->{
            if(s.getCode().equals(KeyCode.ENTER)){
                textField.getParent().requestFocus();
                changed(null, null, checkBox.selectedProperty().getValue());
                // Delete the current entry with the old code, and add the new entry with the updated one
                databaseAPI.deleteEntry(this, oldCode, oldNote);
                databaseAPI.updateEntry(this);
                oldCode = textField.getText();

            }
        });

        noteField = new JFXTextField(species.getNote());
        oldNote = species.getNote();
        noteField.getStyleClass().add("row-field");
        noteField.setOnKeyPressed(s->{
            if(s.getCode().equals(KeyCode.ENTER)){
                noteField.getParent().requestFocus();
                changed(null, null, checkBox.selectedProperty().getValue());
                // Delete the current entry with the old note, and add the new entry with the updated one
                databaseAPI.deleteEntry(this, oldCode, oldNote);
                databaseAPI.updateEntry(this);
                oldNote = noteField.getText();

            }
        });

        if(!isSynonym()){
            textField.getStyleClass().add("accepted-row-field");
            noteField.getStyleClass().add("accepted-row-field");
        }
    }


    private void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            databaseAPI.updateEntry(this);
        } else {
            databaseAPI.deleteEntry(this, oldCode, oldNote);
        }
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
        return new ReadOnlyObjectWrapper<>(noteField);
    }

    Species getSpeciesObject(){
        return species;
    }

    String getScientificString(){
        return species.getScientificName();
    }

    String getCode(){
        return textField.getText();
    }

    String getNote(){
        return noteField.getText();
    }
}
