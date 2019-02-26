package indexes;

import java.io.File;
import java.io.IOException;

public class Tests {

    public static void main(String[] args) throws IOException {

        /*
        File curDir = new File(System.getProperty("user.dir"));
        getAllFiles(curDir);

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        */

        GeoCodeFetcher fetcher = new GeoCodeFetcher();
        String[] codes = new String[]{"DEN", "FIN", "FOR", "30", "10", "cheese"};
        String[] converted = fetcher.fetchCodesList(codes);
        printArr(codes);
        printArr(converted);
        System.out.println(fetcher.fetchCodesSingleString(codes));

        BHIndexFetcher fetcher2 = new BHIndexFetcher();
        String[] codes2 = new String[]{"ranunculaceae", "oleaceae", "barbeuiaceae", "diapensiaceae", "10", "cheese"};
        String[] converted2 = fetcher2.fetchCodes(codes2);
        printArr(codes2);
        printArr(converted2);

        FEIndexFetcher fetcher3 = new FEIndexFetcher();
        String[] codes3 = new String[]{"aconitum l.", "aegilops l.", "wahlenbergia schrader ex roth", "diapensiaceae", "10", "cheese"};
        String[] converted3 = fetcher3.fetchCodes(codes3);
        printArr(codes3);
        printArr(converted3);

        String[][] result = IndexFetcher.fetchAll(codes2, codes3, codes);
        printArr(result[0]);
        printArr(result[1]);
        printArr(result[2]);

    }

    public static void printArr(String[] str){
        for(String s : str){
            System.out.print(s + ", ");
        }
        System.out.println();
    }

    private static void getAllFiles(File curDir) {

        File[] filesList = curDir.listFiles();
        for(File f : filesList){
            if(f.isDirectory())
                getAllFiles(f);
            if(f.isFile()){
                System.out.println(f.getName());
            }
        }
    }
}
