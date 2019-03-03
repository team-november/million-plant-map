package frontend;

import indexes.CSVReader;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
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
        // This is the problem, its just accessing a txt in the resources folder
        RandomAccessFile file = new RandomAccessFile("resources/"+filepath, "rw");

        return file;
    }


    public static void updateFile(LinkedList<String> queries) {
        //update file with new list

        try {


            // Open the file
            RandomAccessFile file = getFile();
            System.out.println("Length: " + file.length());

            // Seek the start
            file.seek(0);

            int size = queries.size();
            // Add all the recent results to the start of the query
            for (int i = 1; i < size; i++) {
                // Access the results in reverse order, so they appear in correct order to the user
                String query = queries.get(size-i);

                file.write((query+"\n").getBytes());
            }

            // Close the file
            file.close();

        } catch (IOException e){
            // In this case do nothing
            e.printStackTrace();
        }

    }

    public static LinkedList<String> retrieveFile() {
        // return list of queries from file or emptyList
        LinkedList<String> result = new LinkedList<>();

        // Read each line and add the result to the list
        try {

            //
            int counter = 0;

            // Open the file
            String line;
            RandomAccessFile file = getFile();

            // Add each line of the file to the list
            while ((line = file.readLine()) != null) {
                counter += 1;
                if (counter > 15){
                    file.write("".getBytes());
                }

                result.add(line);

            }

            // Close the file after access
            file.close();

        } catch (IOException e) {
            // In this case, will just return an empty linked list
            e.printStackTrace();
            return result;

        }

        return result;

    }
}
