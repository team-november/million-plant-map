package frontend;

import api.Species;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;


public class CSVConverter {

    private static String columnNumber(int n) {
        return new String(new char[n-1]).replace("\0", "%s,") + "%s\n";
    }

    private static void writeToCSV(String plantName, File path) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
            QueryHandlerResult result = QueryHandler.query(plantName);
            if(result != null) {

                List<Species> speciesList = result.getSpeciesList();
                String[][] geoCodes = result.getGeoCodes();

                writer.printf(columnNumber(8),
                        "Canonical Name", "Species", "Genus", "Family", "Codes", "Author", "Accepted?", "Basionym?");

                for (Species sp : speciesList) {
                    writer.printf(columnNumber(8),
                            sp.getCanonicalName(), sp.getSpecies(), sp.getGenus(), sp.getFamily(), sp.getCodes(),
                            sp.getAuthorship().replaceAll(",", ""), sp.isSynonym() ? " " : "X", sp.isBasionym() ? "X" : " ");
                }

                writer.print(Controller.formatCodes(plantName, geoCodes) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void exportCSV(String query, File path){
        exportCSVMultiple(Arrays.asList(query.split(",")), path);
    }

    public static void exportCSVMultiple(List<String> queries, File path){
        path.delete();
        for(String query : queries){
            writeToCSV(query, path);
        }
    }

}
