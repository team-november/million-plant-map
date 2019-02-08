package api;

import java.util.ArrayList;

public class APIReturnObject {
    private Species acceptedName;
    private ArrayList<Species> synonyms;

    public APIReturnObject(Species acceptedName, ArrayList<Species> synonyms) {
        this.acceptedName = acceptedName;
        this.synonyms = synonyms;
    }

    public Species getAcceptedName() {
        return acceptedName;
    }

    public void setAcceptedName(Species acceptedName) {
        this.acceptedName = acceptedName;
    }

    public ArrayList<Species> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<Species> synonyms) {
        this.synonyms = synonyms;
    }
}
