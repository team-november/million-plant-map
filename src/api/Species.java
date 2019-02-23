package api;

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
    private String BHcode;
    private String FEcode;
    private boolean basionym;
    private String basionymKey;

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

    public boolean isSynonym() {
        return synonym;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getBHcode() { return BHcode; }
    public void setBHcode(String newBHcode) { BHcode=newBHcode; }

    public String getFEcode() { return FEcode; }
    public void setFEcode(String newFEcode) { FEcode=newFEcode; }

    public boolean isInHerbarium() {
        return isInHerbarium;
    }
    public void setIsInHerbarium(boolean newIsInHerbarium) {
        isInHerbarium = newIsInHerbarium;
    }

    String getBasionymKey() {
        return basionymKey;
    }
}
