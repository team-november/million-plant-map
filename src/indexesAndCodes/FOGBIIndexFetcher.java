package indexesAndCodes;

public class FOGBIIndexFetcher {

    private final String csvFamName = "FOGBIFamilyIndex";
    private final String csvGenusName = "FOGBIGenusIndex";

    protected String fetchSpecimen(String familyName, String genusName){

        CSVReader reader = new CSVReader();

        return reader.matchSpecimen(familyName, genusName, csvFamName, csvGenusName,
                1, 0, 2);
        // search index for family name, search index for family code and for genus name

    }

}
