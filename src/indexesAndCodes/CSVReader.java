package indexesAndCodes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

    private String csvFile = "";
    private BufferedReader br = null;
    private String line = "";

    // constants for reading and splitting csv files
    private final String csvSplitBy = ",";
    private final String filehandle = "resources/"; // may need to change this to "/resources/" on the final version for it to work
    private final String extn = ".csv";

    private BufferedReader openCSV(String csvName) throws IOException {
        BufferedReader buf = new BufferedReader(new FileReader(filehandle+csvName+extn));

        return buf;
    }

    private String[] getValues(String[] values, int searchIndex) throws IOException{

        String[] results = new String[values.length];
        String noMatch = "unknown";
        int resultIndex = 1-searchIndex;
        String currentStr = "";
        int matches = 0;

        // initialise to no values
        for(int i = 0; i < values.length; i++){
            results[i] = noMatch;
        }

        while((line = br.readLine()) != null){

            // use comma as separator
            String[] cols = line.split(csvSplitBy);
            currentStr = cols[searchIndex];

            // check if current value matches any items in the query list
            for(int i = 0; i < values.length; i++){

                if(values[i].equals(currentStr)){
                    results[i] = cols[resultIndex];
                    matches++;

                    // break if all matches found
                    if(matches == values.length){
                        break;
                    }
                }
            }
        }

        return results;

    }

    private String[] getTwoValues(String[] values, int searchIndex) throws IOException{

        String[] results = new String[values.length];
        String noMatch = "unknown";
        int resultIndex = 1-searchIndex;
        String currentStr = "";
        int matches = 0;

        // initialise to no values
        for(int i = 0; i < values.length; i++){
            results[i] = noMatch;
        }

        while((line = br.readLine()) != null){

            // use comma as separator
            String[] cols = line.split(csvSplitBy);
            currentStr = cols[searchIndex];

            // check if current value matches any items in the query list
            for(int i = 0; i < values.length; i++){

                if(values[i].equals(currentStr)){
                    results[i] = cols[resultIndex] + "/" + cols[resultIndex+1];
                    matches++;

                    // break if all matches found
                    if(matches == values.length){
                        break;
                    }
                }
            }
        }

        return results;

    }

    protected String[] extractValues(String[] values, int searchIndex, String csvName) {

        try {
            // Try to open the csv and extract values from it. If it fails at any point, either opening
            // or reading, throw a csv read exception.
            br = openCSV(csvName);

            // get the values required from the CSV
            return getValues(values, searchIndex);

        } catch (IOException e) {
            // return array of empty values
            String[] empties = new String[values.length];

            for (int i = 0; i < values.length; i++) {
                empties[i] = "";
            }

            System.out.println("ERROR");
            e.printStackTrace();
            return empties;

        }

    }

    // Like the above, but extracts two adjacent values from the csv (because I messed up
    // making the csvs and having one method that extracts two columns is an easier fix
    // than writing another script to copy the values into one column)
    protected String[] extractTwoValues(String[] values, int searchIndex, String csvName) {

        try {
            // Try to open the csv and extract values from it. If it fails at any point, either opening
            // or reading, throw a csv read exception.
            br = openCSV(csvName);

            // get the values required from the CSV
            return getTwoValues(values, searchIndex);

        } catch (IOException e) {
            // return array of empty values
            String[] empties = new String[values.length];

            for (int i = 0; i < values.length; i++) {
                empties[i] = "";
            }

            System.out.println("ERROR");
            e.printStackTrace();
            return empties;

        }

    }

}
