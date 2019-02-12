package indexesAndCodes;

public class IndexFetcher {

    // make sure all input is converted to lower case

    // for the two lists of family and genus, it fetches the BH index, and the FE index
    // and the geo codes.
    // will extend this to cover the other indexing schemes that there are
    public static String[][] fetchAll(String[] families, String[] genuses, String[] geoCodes){

        String [][] results = new String[3][];
        BHIndexFetcher BHfetcher = new BHIndexFetcher();
        results[0] = BHfetcher.fetchCodes(families);

        FEIndexFetcher FEfetcher = new FEIndexFetcher();
        results[1] = FEfetcher.fetchCodes(genuses);

        GeoCodeFetcher geoFetcher = new GeoCodeFetcher();
        results[2] = new String[]{geoFetcher.fetchCodesSingleString(geoCodes)};

        return results;

    }

}
