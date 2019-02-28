package frontend;

import api.QueryResult;
import api.Species;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class CSVConverter {

    private static String columnNumber(int n) {
        return new String(new char[n-1]).replace("\0", "%s,") + "%s\n";
    }

    private static boolean writeToCSV(QueryResult result, File path) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
            if(result != null) {

                String[][] geoCodes = result.getGeoCodes();

                writer.printf(columnNumber(8),
                        "Canonical Name", "Species", "Genus", "Family", "Codes", "Author", "Accepted?", "Basionym?");

                for (Species sp : result) {
                    writer.printf(columnNumber(8),
                            sp.getCanonicalName(), sp.getSpecies(), sp.getGenus(), sp.getFamily(), sp.getCodes(),
                            sp.getAuthorship().replaceAll(",", ""), sp.isSynonym() ? " " : "X", sp.isBasionym() ? "X" : " ");
                }

                writer.print(Controller.formatCodes("", geoCodes) + "\n");
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean exportCSV(List<QueryResult> results, File path){
        path.delete();
        for(QueryResult query : results){
            boolean success = writeToCSV(query, path);
            if(!success){
                return false;
            }
        }
        return true;
    }

}
