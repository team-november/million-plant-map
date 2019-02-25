package FrontEnd;

import api.APIServiceImpl;
import api.QueryResult;
import api.Species;
import indexesAndCodes.BHIndexFetcher;
import indexesAndCodes.FEIndexFetcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DataCollected {

    public List<Species> dataToPrint(String plantName) {
        //search on API
        APIServiceImpl api = APIServiceImpl.getInstance();
        QueryResult result = api.getAcceptedNameAndSynonyms(plantName);
        // insert additional informations like BH and FE code
        BHIndexFetcher bHIndexFetcher = new BHIndexFetcher();
        FEIndexFetcher fEIndexFetcher = new FEIndexFetcher();
        Iterator<Species> iterator = result.iterator();
        ArrayList<Species> toPrint = new ArrayList<>();
        while (iterator.hasNext()) {
            Species current = iterator.next();
            //current.setBHcode(bHIndexFetcher.fetchCode(current.getFamily()));
            current.setFEcode(fEIndexFetcher.fetchCode(current.getFamily()));
            current.setIsInHerbarium(false);
            toPrint.add(current);
        }

        return toPrint;
    }
}
