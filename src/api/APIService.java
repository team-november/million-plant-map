package api;

public interface APIService {
     Species getAcceptedSpecies(String name);

     Species[] getSynonyms(String acceptedKey);

     QueryResult getAcceptedNameAndSynonyms(String name);

     String getAcceptedKey(String name);
}
