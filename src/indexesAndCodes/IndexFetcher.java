package indexesAndCodes;

public class IndexFetcher {

    /*
     * For querying species
     *
     */

    // Call this with a family name and a genus name to get any matching index values from the different schemes
    public String fetchIndexes(String familyName, String genusName){

        // Calls each different scheme independently and adds value to result.
        String result = "";

        FEIndexFetcher fe = new FEIndexFetcher();
        BHIndexFetcher bh = new BHIndexFetcher();
        FOGBIIndexFetcher sm = new FOGBIIndexFetcher();

        String sFe = fe.fetchSpecimen(familyName,genusName);
        if(!sFe.equals(""))
            result += "FE:"+sFe;

        String sBh = bh.fetchSpecimen(familyName,genusName);
        if(!sBh.equals(""))
            result += "BH:"+sBh;

        String sSm = sm.fetchSpecimen(familyName,genusName);
        if(!sSm.equals(""))
            result += "FOGBI:"+sSm;

        return result;

    }


    /*
    *
    * FOR QUERYING GEO CODES
     *
     * */
    // Gets interpreted value for a list of codes
    public static String fetchGeoCodeList(String[] codes){

        // instantiate a geo code fetcher and return an string of all the combined codes, interpreted
        GeoCodeFetcher geoCodeFetcher = new GeoCodeFetcher();
        return geoCodeFetcher.fetchCodesSingleString(codes);

    }

    /*
    * OLD METHODS PLEASE USE THE NEW ONES ABOVE
    * */
    // Todo new method added say that it is here
    private static String fetchGeoCode(String code){

        GeoCodeFetcher geoCodeFetcher = new GeoCodeFetcher();
        return geoCodeFetcher.fetchCodesSingleString(new String[]{code});
    }

    // for the two lists of family and genus, it fetches the BH index, and the FE index
    // and the geo codes.
    // will extend this to cover the other indexing schemes that there are
    @Deprecated
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

    @Deprecated
    protected static String[][] fetchAll(String[] families, String[] genuses, String[] geoCodes){
        // Deprecated due to new methods introduced, use those for now on.


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
