package indexes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

    /* This takes one single term and matches it with the first word in each entry in the given csv
    * Returns a list of potential indexes to check
    * and returns empty list if there are no matches
    * This is for matching family csvs, assumes exactly two columns are present */
    private String[] matchTerm(String searchTerm, int searchIndex, String csvName){

        // Only match on lower case terms
        searchTerm = getFirstWord(searchTerm.toLowerCase());

        // Results to return
        ArrayList<String> results = new ArrayList<>();

        // Note assumes there are exactly two columns and this is for matching families
        int resultIndex = 1-searchIndex;

        // Open the CSV
        try{
            br = openCSV(csvName);
            String currentStr;

        // Read through the CSV to find the match:
        while((line = br.readLine()) != null){

            // use comma as separator
            String[] cols = line.split(csvSplitBy);

            if(cols.length == 2) {
                // Only try and extract if there are values there to take
                currentStr = cols[searchIndex];

                // If the current col is a match, add it to the result lists
                // Use get first word to just match first section of name
                if (getFirstWord(currentStr).equals(searchTerm))
                    results.add(cols[resultIndex]);

            }

        }

        } catch (IOException e){
            return new String[0];
        }

        // Return all results found, should be numerical indexes for families
        // Note its a list because some families have multiple numbers in bentham and hooker
        String[] finalResult = new String[results.size()];
        for(int i = 0; i < results.size(); i++){
            finalResult[i] = (String) results.get(i);
        }
        return finalResult;
    }

    // Will take two terms, say a family index and a genus name, and return the corresponding genus number
    private String matchTwoTerms(String searchTerm1, String searchTerm2, int searchIndex1,
                                   int searchIndex2, String csvName){
        /* Make searchindex1 family index column
            And searchindex2 genus name column
            And searchTerms 1 and 2 family index and genus name respectively
         */

        // Only match on lower case terms, and match first word of string
        searchTerm1 = getFirstWord(searchTerm1.toLowerCase());
        searchTerm2 = getFirstWord(searchTerm2.toLowerCase());

        // Will find first match then return it
        String result = "";

        // Note assumes there are exactly three columns and this is for matching genuses
        int resultIndex = getResultIndex(searchIndex1, searchIndex2);

        try{
            // Open the csv
            br = openCSV(csvName);

            // Read columns name line
            br.readLine();

            // Read through the CSV to find the match:
            while((line = br.readLine()) != null){

                // use comma as separator
                String[] cols = line.split(csvSplitBy);

                // Checks for searchIndex1 first, and if correct matches searchIndex2

                if(cols.length == 3) {
                    // Only try and extract if there are values there to take
                    if (Float.valueOf(getFirstWord(cols[searchIndex1])).equals(Float.valueOf(searchTerm1))) {

                        if (getFirstWord(cols[searchIndex2]).equals(searchTerm2)) {
                            // If find match, done, put it in result.
                            result = cols[resultIndex];
                            break;
                        }

                    }
                }


            }

        } catch (IOException e){
            return result;
        }

        return result;
    }

    protected String matchSpecimen(String familyName, String genusName, String csvNameFam, String csvNamegenus,
                                   int indexFam, int indexGenus1, int indexGenus2){
        /* Family name is the string name of the family being searched for
            Genus name is the string name of the genus being searched for
            csvNameFam is the name of the family csv in the resources folder
            csvNamegenus is the name if the genus csv in the resources folder
            indexFam is the column number for the indexes for the families in the family csv
            indexGenus1 is the column number for the indexes for the families in the genus csv
            indexGenus2is the column number for the genus names in the genus csv

         */

        String[] familyIndexes = this.matchTerm(familyName, indexFam, csvNameFam);
        String[] genusIndexes = new String[familyIndexes.length];

        String result = "";

        // For each potential family index returned, find any genus matches
        for(int i = 0; i < familyIndexes.length; i++){
            genusIndexes[i] = this.matchTwoTerms(familyIndexes[i], genusName, indexGenus1, indexGenus2, csvNamegenus);
        }

        // Now to combine the results together nicely, and produce a string of matched output.
        for(int i = 0; i < familyIndexes.length; i++){
            if(!genusIndexes[i].equals("")){
                result += familyIndexes[i] + "/" + genusIndexes[i] + "  ";
            }
            else{
                result += familyIndexes[i] + "/  ";
            }
        }

        return result;
    }

    private static String getFirstWord(String word){
        // Returns the first word from the string, so we don't try and match on author names etc
        return word.split(" ")[0];
    }

    private static int getResultIndex(int index1, int index2) {
        // there are indexes 0,1,2 and each index can only be one of them, find the missing one
        int sum = index1 + index2;
        if(sum == 3)
            return 0;
        if (sum == 2)
            return 1;
        else
            return 2;

    }

}
