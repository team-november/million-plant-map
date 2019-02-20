package FrontEnd;

import api.APIServiceImpl;
import api.Species;
import indexesAndCodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DataCollected {
    //First will always be the accepted one
    private List<Species> toPrint;

    public List<Species> DataToPrint(String plantName){
        //search on API
        APIServiceImpl api = APIServiceImpl.getInstance();
        Species acceptedSpecie = api.getAcceptedSpecies(plantName);
        Species[] synonyms = api.getSynonyms(acceptedSpecie.getKey());
        toPrint= new ArrayList<>(Arrays.asList(synonyms));
        toPrint.add(0,acceptedSpecie);
        // insert additional informations like BH and FE code
        BHIndexFetcher bHIndexFetcher = new BHIndexFetcher();
        FEIndexFetcher fEIndexFetcher = new FEIndexFetcher();
        for(int i=0;i<toPrint.size();i++){
            Species current = toPrint.get(0);

            current.setBHcode(bHIndexFetcher.fetchCode(current.getFamily()));
            current.setFEcode(fEIndexFetcher.fetchCode(current.getFamily()));
            current.setIsInHerbarium(false);
        }
        return toPrint;
    }
}
