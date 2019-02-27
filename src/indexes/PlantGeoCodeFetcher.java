package indexes;

public class PlantGeoCodeFetcher {
    private static final String csvName = "plantsGeoCodes";
    private static final int searchIndex = 0;

    public static String[][] fetchCodes(String plantName) {
        String pN = plantName.substring(0, 1).toUpperCase() + plantName.substring(1).toLowerCase();
        CSVReader reader = new CSVReader();
        String[] arr = new String[1];
        arr[0] = pN;
        String geoCodes = reader.extractValues(arr, searchIndex, csvName)[0];
        String[] geoCodesSplit = geoCodes.split("\\s+");
        String[] geoCodesInput = geoCodes.replaceAll("[^a-zA-Z0-9 ]", "").toUpperCase().split("\\s+");
        GeoCodeFetcher gCF = new GeoCodeFetcher();
        String[] geoCodesInterpretation = gCF.fetchCodesList(geoCodesInput);
        String[][] toReturn = new String[geoCodesInterpretation.length][2];
        for (int i = 0; i < geoCodesInterpretation.length; i++) {
            toReturn[i][0] = geoCodesSplit[i];
            toReturn[i][1] = geoCodesInterpretation[i];
        }
        return toReturn;
    }
}
