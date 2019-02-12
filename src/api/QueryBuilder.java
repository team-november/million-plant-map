package api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QueryBuilder {
    private final String GBIF_API_URL = "http://api.gbif.org/v1";

    public String searchForSpecies(String name) {
        return String.format("%s/species/match?name=%s&strict=TRUE&verbose=TRUE", GBIF_API_URL, encode(name.toLowerCase()));
    }

    public String searchForSynonyms(String acceptedKey) {
        return String.format("%s/species/%s/synonyms", GBIF_API_URL, encode(acceptedKey));
    }

    public String searchForAcceptedName(String acceptedKey) {
        return String.format("%s/species/%s", GBIF_API_URL, encode(acceptedKey));
    }

    public String getAPIUrl() {
        return GBIF_API_URL;
    }

    private String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
