package frontend;

import java.util.LinkedList;

public class PersistentList {

    private static String filepath = "queries.txt";

    public static void updateFile(LinkedList<String> queries){
        //TODO: create or update file with new list
    }

    public static LinkedList<String> retrieveFile(){
        //TODO: return list of queries from file or emptyList
        LinkedList result = new LinkedList();
        return result;
    }

}
