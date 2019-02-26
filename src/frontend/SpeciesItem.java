package frontend;

import api.Species;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;

public class SpeciesItem extends RecursiveTreeObject<SpeciesItem> {

    private Species species;

    public SpeciesItem(Species species) {
        this.species = species;
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

    public StringProperty getCodes() {
        return new ReadOnlyStringWrapper(species.getCodes());
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

}
