package frontend;

import api.APIServiceImpl;
import api.QueryResult;
import api.Species;
import database.DatabaseHandler;
import indexes.IndexFetcher;
import indexes.PlantGeoCodeFetcher;

import java.util.ArrayList;
import java.util.List;

public class QueryHandler {

    public static QueryHandlerResult query(String name) {
        //Code for whe database connection is working:
        //DatabaseHandler dBH = DatabaseHandler.getInstance();
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
            //Code for when the database connection is working:
            /*if(dBH.getFirstSynonymByName(name) == null){
                species.setIsInHerbarium(false);
            }
            else{
                species.setIsInHerbarium(true);
            }*/
            //Instead of this line:
            species.setIsInHerbarium(false);
            toReturn.add(species);
        }
        return new QueryHandlerResult(toReturn, geoCodes);
    }

}
