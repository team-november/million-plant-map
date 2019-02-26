package indexesAndCodes;

public class GeoCodeFetcher {

    private String[] codes;
    private final String csvName = "geoCodes";
    private final int searchIndex = 0;

    protected String fetchCodesSingleString(String[] geoCodesList){
        // instead returns the converted codes in one long string
        String[] arr = fetchCodesList(geoCodesList);

        StringBuilder builder = new StringBuilder();
        // copy in all the values
        for(int i = 0; i < arr.length-1; i++){
            builder.append(arr[i]);
            builder.append(", ");
        }
        builder.append(arr[arr.length-1]);

        return builder.toString();
    }

    // given an array of codes, it will return an array of locations
    protected String[] fetchCodesList(String[] geoCodesList){
        setCodes(geoCodesList);

        // get a new reader object and extract the relevant values.
        CSVReader reader = new CSVReader();
        return reader.extractValues(geoCodesList, searchIndex, csvName);

    }

    protected void setCodes(String[] codes) {
        this.codes = codes;
    }

}
