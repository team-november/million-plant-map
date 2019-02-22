package indexesAndCodes;

public class IndexFetcher {

    // make sure all input is converted to lower case

    // for the two lists of family and genus, it fetches the BH index, and the FE index
    // and the geo codes.
    // will extend this to cover the other indexing schemes that there are
    public static String[] fetchIndexesCodes(String family, String genus){

        String [] results = new String[2];

        BHIndexFetcher BHfetcher = new BHIndexFetcher();
        String r0 = BHfetcher.fetchCode(family.toLowerCase());
        if(r0 != "") r0 = "BH: " + r0 + ", ";

        FEIndexFetcher FEfetcher = new FEIndexFetcher();
        String r1 = BHfetcher.fetchCode(genus.toLowerCase());
        if(r1 != "") r1 = "FE: " + r0 + ", ";

        results[0] = r0+r1;


        return results;

    }

    private String getOnlineGeoCode(String family, String genus){
        return "";
    }


    private static String[][] fetchAll(String[] families, String[] genuses, String[] geoCodes){

        String [][] results = new String[3][];
        BHIndexFetcher BHfetcher = new BHIndexFetcher();
        results[0] = BHfetcher.fetchCodes(families);

        FEIndexFetcher FEfetcher = new FEIndexFetcher();
        results[1] = FEfetcher.fetchCodes(genuses);

        GeoCodeFetcher geoFetcher = new GeoCodeFetcher();
        results[2] = new String[]{geoFetcher.fetchCodesSingleString(geoCodes)};

        return results;

    }

    // Todo new method need to say its here
    // Gets interpreted value for a list of codes
    public static String fetchGeoCodeList(String[] codes){

        // instantiate a geo code fetcher and return an string of all the combined codes, interpreted
        GeoCodeFetcher geoCodeFetcher = new GeoCodeFetcher();
        return geoCodeFetcher.fetchCodesSingleString(codes);

    }

    // Todo new method added say that it is here
    private static String fetchGeoCode(String code){

        GeoCodeFetcher geoCodeFetcher = new GeoCodeFetcher();
        return geoCodeFetcher.fetchCodesSingleString(new String[]{code});
    }

}
