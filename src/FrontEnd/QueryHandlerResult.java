package FrontEnd;

import api.Species;

import java.util.List;

public class QueryHandlerResult {
    //a list of all the species to be displayed
    private List<Species> speciesList;
    //an array containing the geoCodes (in secondary index 0) and their interpretation (in secondary index 1)
    private String[][] geoCodes;

    public QueryHandlerResult(List<Species> sL, String[][] gC) {
        speciesList = sL;
        geoCodes = gC;
    }

    public List<Species> getSpeciesList() {
        return speciesList;
    }

    public String[][] getGeoCodes() {
        return geoCodes;
    }
}
