package frontend;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;

public class PersistentList {

    /*
     * For accessing a list of recent searches from persistent storage
     * needs to check if a file is there or not, and if not not construct it
     *
     * Then it needs to turn that file into a list of recent search strings
     * */

    private static String filepath = "queries.txt";
    private static String capSize;

    private static RandomAccessFile getFile() throws IOException{
        // gets a handle to the resources file:
        URL storepath = PersistentList.class.getProtectionDomain().getCodeSource().getLocation();
        String urlString = storepath.toString();
        int firstSlash =  urlString.indexOf("/");
        int targetSlash = urlString.lastIndexOf("/", urlString.length() - 2) + 1;
        RandomAccessFile file = new RandomAccessFile(urlString.substring(firstSlash, targetSlash)+"queries.txt", "rw");
        return file;
    }


    public static void updateFile(LinkedList<String> queries) {
        //update file with new list
        try {
            // Open the file
            RandomAccessFile file = getFile();
            // Clean the file
            file.setLength(0);
            // Seek the start
            file.seek(0);
            int size = queries.size();
            // Add all the recent results to the start of the query
            for (String query : queries) {
                // Access the results in reverse order, so they appear in correct order to the user
                file.write((query+"\n").getBytes());
            }
            // Close the file
            file.close();
        } catch (Exception e){
            // In this case do nothing
            e.printStackTrace();
        }
    }

    public static LinkedList<String> retrieveFile() {
        // return list of queries from file or emptyList
        LinkedList<String> result = new LinkedList<>();
        // Read each line and add the result to the list
        try {
            // Open the file
            RandomAccessFile file = getFile();
            String line;
            // Add each line of the file to the list
            while ((line = file.readLine()) != null) {
                result.add(line);
            }
            // Close the file after access
            file.close();

        } catch (Exception e) {
            // In this case, will just return an empty linked list
            e.printStackTrace();
            return result;
        }
        return result;
    }
}
