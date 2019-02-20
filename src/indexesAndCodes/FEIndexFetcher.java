package indexesAndCodes;

public class FEIndexFetcher {

    private final String csvName = "FloraEuropeaGenusIndex";
    private final int searchIndex = 0;

    // returns an array of family/genus indexes under the flora europaea scheme, for an array of genus names
    protected String[] fetchCodes(String[] genusNamesArray){
        CSVReader reader = new CSVReader();
        // has to read two values from the CSv, so uses this method instead
        return reader.extractTwoValues(genusNamesArray, searchIndex, csvName);
    }

    // returns a single family/genus index, using the method above
    public String fetchCode(String familyName){
        return fetchCodes(new String[]{familyName})[0];
    }

}
