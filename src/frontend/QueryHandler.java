package frontend;

import api.APIServiceImpl;
import api.QueryResult;
import api.Species;
import indexes.IndexFetcher;
import indexes.PlantGeoCodeFetcher;

public class QueryHandler {

    public static QueryResult query(String name) {
        //Code for whe database connection is working:
        //DatabaseHandler dBH = DatabaseHandler.getInstance();
        APIServiceImpl api = APIServiceImpl.getInstance();
        QueryResult queryResult = api.getAcceptedNameAndSynonyms(name);
        if(!queryResult.iterator().hasNext()){
            return null;
        }

        String[][] geoCodes = PlantGeoCodeFetcher.fetchCodes(queryResult.getAcceptedName().getCanonicalName());
        queryResult.setGeoCodes(geoCodes);

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
        }
        return queryResult;
    }

}
