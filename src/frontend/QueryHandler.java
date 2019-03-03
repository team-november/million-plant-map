package frontend;

import api.APIServiceImpl;
import api.QueryResult;
import api.Species;
import indexes.IndexFetcher;
import indexes.PlantGeoCodeFetcher;

public class QueryHandler {

    public static QueryResult query(String name) {
        DatabaseAPI databaseAPI = new DatabaseAPI();

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

            //If something is in the database, get the code from the database and
            // also set the isinHerbarium field.
            Species inDatabase = databaseAPI.getEntry(species.getScientificName());
            if("Vanda cristata Lindl.".equals(species.getScientificName())){
                System.out.println("FOUND MATCH!!!");
            }

            if(inDatabase != null){
                System.out.println("it was not null");
                species.setIsInHerbarium(true);
                species.setCodes(inDatabase.getCodes());
            }
            System.out.println("it was null");

        }
        return queryResult;
    }

}
