package indexesAndCodes;

public class FEIndexFetcher {

    private final String csvFamName = "FloraEuropaeaFamilyIndex";
    private final String csvGenusName = "FloraEuropaeaGenusIndex";
    private final int searchIndex = 0;

    // returns an array of family/genus indexes under the flora europaea scheme, for an array of genus names
    @Deprecated
    protected String[] fetchCodes(String[] genusNamesArray){
        CSVReader reader = new CSVReader();
        // has to read two values from the CSv, so uses this method instead
        return reader.extractTwoValues(genusNamesArray, searchIndex, csvGenusName);
    }

    // returns a single family/genus index, using the method above
    @Deprecated
    protected String fetchCode(String familyName){
        return fetchCodes(new String[]{familyName})[0];
    }

    // Fetch a specimen for Flora Europaea
    protected String fetchSpecimen(String familyName, String genusName){

        CSVReader reader = new CSVReader();

        return reader.matchSpecimen(familyName, genusName, csvFamName, csvGenusName,
                0, 1, 0);
            // search index for family, seacch index for family code and for genus name

    }

}
