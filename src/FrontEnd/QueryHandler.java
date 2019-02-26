package FrontEnd;

import api.APIService;
import api.APIServiceImpl;
import api.QueryResult;
import api.Species;
import indexesAndCodes.IndexFetcher;
import indexesAndCodes.PlantGeoCodeFetcher;

import java.util.ArrayList;
import java.util.List;

public class QueryHandler {


    public QueryHandlerResult query(String name) {
        //TODO: Add interaction with database
        APIService api = APIServiceImpl.getInstance();
        QueryResult qr = api.getAcceptedNameAndSynonyms(name);
        String[][] geoCodes = PlantGeoCodeFetcher.fetchCodes(qr.getAcceptedName().getCanonicalName());
        List<Species> toReturn = new ArrayList<>();
        IndexFetcher iF = new IndexFetcher();
        for (Species s : qr) {
            String codes = iF.fetchIndexes(s.getFamily(), s.getGenus());
            s.setCodes(codes);
            s.setIsInHerbarium(false);
            toReturn.add(s);
        }
        QueryHandlerResult qHR = new QueryHandlerResult(toReturn, geoCodes);
        return qHR;
    }
}
