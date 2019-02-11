package api;

public class Species {
    //TODO match between API return and Species object
    private String key;
    private String canonicalName;
    private String acceptedKey;
    private String family;
    private String genus;
    private String species;
    private boolean synonym;
    private String authorship;
    private String scientificName;

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

    public boolean isSynonym() {
        return synonym;
    }

    public String getScientificName() {
        return scientificName;
    }

}
