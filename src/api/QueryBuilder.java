package api;

public class QueryBuilder {
    private final String GBIF_API_URL = "http://api.gbif.org/v1";

    public String searchForSpecies(String name) {
        return String.format("%s/species/search\\?q\\=%s", GBIF_API_URL, name);
    }

    public String searchForSynonyms(String acceptedKey) {
        return String.format("%s/species/%s/synonyms", GBIF_API_URL, acceptedKey);
    }

}
