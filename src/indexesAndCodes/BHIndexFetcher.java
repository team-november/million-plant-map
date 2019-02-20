package indexesAndCodes;

public class BHIndexFetcher {

    private final String csvName = "BenthamHookerFamilyIndex";
    private final int searchIndex = 1;

    // returns an array of family indexes under the bentham and hooker scheme, for an array of family names
    protected String[] fetchCodes(String[] familyNamesArray){
        CSVReader reader = new CSVReader();
        return reader.extractValues(familyNamesArray, searchIndex, csvName);
    }

    // returns a single family index, using the method above
    public String fetchCode(String familyName){
        return fetchCodes(new String[]{familyName})[0];
    }

}
