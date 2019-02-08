package api;

import java.util.ArrayList;

/*
 * Implemented as a singleton so that API calls are
 * made from a single source
 */
public class APIServiceImpl implements APIService {

    private static APIServiceImpl instance = new APIServiceImpl();

    private APIServiceImpl() {
    }

    public static APIServiceImpl getInstance() {
        return instance;
    }


    @Override
    public Species getAcceptedName(String name) {
        return null;
    }

    @Override
    public ArrayList<Species> getSynonyms(String acceptedKey) {
        return null;
    }

    @Override
    public APIReturnObject getAcceptedNameAndSynonyms(String name) {
        Species acceptedName = getAcceptedName(name);
        ArrayList<Species> synonyms = getSynonyms(acceptedName.getAcceptedKey());
        APIReturnObject returnObject = new APIReturnObject(acceptedName, synonyms);
        return returnObject;
    }
}
