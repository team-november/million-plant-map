package indexes;

public class BHIndexFetcher {

    private final String csvFamName = "BenthamHookerFamilyIndex";
    private final String csvGenusName = "BenthamHookerGenusIndex";
    private final String externFamName = "BenthamHookerFamilyIndex.csv";
    private final String externGenName = "BenthamHookerGenusIndex.csv";
    private final int searchIndex = 1;

    // returns an array of family indexes under the bentham and hooker scheme, for an array of family names
    @Deprecated
    protected String[] fetchCodes(String[] familyNamesArray){

        CSVReader reader = new CSVReader();
        return reader.extractValues(familyNamesArray, searchIndex, externFamName);
    }

    // returns a single family index, using the method above
    public String fetchCode(String familyName){
        return fetchCodes(new String[]{familyName})[0];
    }

    // Fetch a specimen from Bentham and Hooker
    public String fetchSpecimen(String familyName, String genusName){

        CSVReader reader = new CSVReader();

        return reader.matchSpecimen(familyName, genusName, externFamName, externGenName,
                0, 1, 0);
        // search index for family, seacch index for family code and for genus name

    }

}
