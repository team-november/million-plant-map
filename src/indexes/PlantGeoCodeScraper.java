package indexes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;


public class PlantGeoCodeScraper {
    //main method to run the webscrapper
    public static void main(String[] args) throws Exception {
        File plantsNew = new File("resources/plantsGeoCodesTemp.csv");
        if (plantsNew.exists()) {
            plantsNew.delete();
        }
        //collect the information into file 'plant1'
        for (int i = 0; i < 540000; i = i + 100) {
            webScrape(i, i + 100);
        }
        //if a previous file exists, delete it
        File plantsOld = new File("resources/plantsGeoCodes.csv");
        if (plantsOld.exists()) {
            plantsOld.delete();
        }
        //rename the file to replace the old file
        plantsNew.renameTo(plantsOld);
    }

    //method to webscrape the distribution from WCSP of plants with indices from start (inclusive) to end (exclusive) and put into file 'plants1'
    private static void webScrape(int start, int end) {
        int current = start;
        int last = end;
        try (PrintWriter writer = new PrintWriter(new FileWriter("resources/plantsGeoCodesTemp.csv", true))) {
            for (int i = start; i < end; i++) {
                System.out.println(i);
                current = i;
                String url = "http://wcsp.science.kew.org/namedetail.do?name_id=" + i;
                Document doc = Jsoup.connect(url).get();
                Elements e = doc.getElementsByTag("div");
                //if database unable to process request, try again
                if (e.get(1).getElementsByTag("p").text().equals("The database was unable to process your request.  Return to the Checklist")) {
                    i--;
                    continue;
                }
                //if too little information on page, then no entry for this indice
                if (e.size() < 7) {
                    continue;
                }
                //get relevant information from the web page to gather information
                Elements f = e.get(6).getElementsByTag("th");
                Elements a = e.get(6).getElementsByTag("h2").get(0).getElementsByTag("i");
                //collate the name of the current species
                StringBuilder name = new StringBuilder();
                for (Element as : a) {
                    name.append(as.text());
                    name.append(" ");
                }
                String plantName = name.toString().trim();
                String th = f.get(0).text();
                //check the tag is distribution (i.e. it is not a synonym)
                if (th.equals("Distribution:")) {
                    //get the geo codes of the current species
                    Elements g = e.get(6).getElementsByTag("td");
                    String td = g.get(0).text();
                    int j = 0;
                    for (Character c : td.toCharArray()) {
                        if (Character.isDigit(c)) {
                            break;
                        }
                        j++;
                    }
                    String geoCodes = td.substring(j, td.length()).trim().replace(')', ' ').replace('(', ' ');
                    //write the name and geo codes to the csv file
                    writer.write(plantName + "," + geoCodes + "\n");
                }
            }
        }
        //if there is an exception, continue
        catch (Exception e) {
            webScrape(current, last);
        }
    }
}

