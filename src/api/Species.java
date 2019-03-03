package api;

import database.Synonym;

public class Species {
    private String key;
    private String canonicalName;
    private String acceptedKey;
    private String family;
    private String genus;
    private String species;
    private boolean synonym;
    private String authorship;
    private String scientificName;
    private boolean isInHerbarium;
    private String codes;
    private boolean basionym;
    private String basionymKey;
    private String note;

    private String rank;

    public boolean isBasionym() {
        return basionym;
    }

    void setBasionym(boolean b) {
        basionym = b;
    }


    public String getKey() {
        return key;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public String getFamily() {
        return family;
    }

    public String getGenus() {
        return genus;
    }

    public String getSpecies() {
        return species;
    }

    public String getAuthorship() {
        return authorship;
    }


    public String getAcceptedKey() {
        return acceptedKey;
    }

    public String getRank() {
        return rank;
    }

    public boolean isSynonym() {
        return synonym;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String newCodes) {
        codes = newCodes;
    }

    public boolean isInHerbarium() {
        return isInHerbarium;
    }
    public void setIsInHerbarium(boolean newIsInHerbarium) {
        isInHerbarium = newIsInHerbarium;
    }

    String getBasionymKey() {
        return basionymKey;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static Species convertSynonymToSpecies(Synonym synonym){
    // Given a Synonym object, create a species object and return it


        // copy over the fields
        Species speciesResult = new Species();
        speciesResult.family = synonym.getFamilyName();
        speciesResult.genus = synonym.getName().split(" ")[0];
        speciesResult.canonicalName = synonym.getName();
        speciesResult.note = synonym.getNote();
        speciesResult.basionym = synonym.isBasionym();
        speciesResult.isInHerbarium = true;
        speciesResult.authorship = ""; // author not stored in the database?
        speciesResult.note = synonym.getNote(); // note inputted by the client

        // create the code using the index information from the synonym class
        String indexCode = synonym.getScheme().toString() +  " " + synonym.getFamilyNumber() +
                    "/" + synonym.getGenusNumber();


        speciesResult.codes = indexCode;


        return speciesResult;
    }
}
