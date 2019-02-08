package api;

import java.util.ArrayList;

public interface APIService {
     Species getAcceptedName(String name);

     ArrayList<Species> getSynonyms(String acceptedKey);

     APIReturnObject getAcceptedNameAndSynonyms(String name);
}
