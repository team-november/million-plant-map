package frontend;

import api.APIServiceImpl;
import api.QueryResult;
import api.Species;
import indexes.IndexFetcher;
import indexes.PlantGeoCodeFetcher;

import java.util.ArrayList;
import java.util.List;

public class QueryHandler {


    public QueryHandlerResult query(String name) {
        //TODO: Add interaction with database
        APIServiceImpl api = APIServiceImpl.getInstance();
        QueryResult queryResult = api.getAcceptedNameAndSynonyms(name);
        if(!queryResult.iterator().hasNext()){
            return null;
        }
        String[][] geoCodes = PlantGeoCodeFetcher.fetchCodes(queryResult.getAcceptedName().getCanonicalName());
        List<Species> toReturn = new ArrayList<>();
        IndexFetcher indexFetcher = new IndexFetcher();
        for (Species species : queryResult) {
            String codes = indexFetcher.fetchIndexes(species.getFamily(), species.getGenus());
            species.setCodes(codes);
            species.setIsInHerbarium(false);
            toReturn.add(species);
        }
        return new QueryHandlerResult(toReturn, geoCodes);
    }

}
