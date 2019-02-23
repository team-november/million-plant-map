package FrontEnd;

import api.Species;

import java.util.List;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;





public class CSVConverter {

    public void printCSV(String plantName){
        //create a file csv called like the plantName
        try (PrintWriter writer = new PrintWriter(new File(plantName+".csv"))) {


            //print what each column is

            //print data taken from dataToPrint method
            DataCollected dataCollected = new DataCollected();
            List<Species> toPrint= dataCollected.DataToPrint(plantName);
            StringBuilder sb = new StringBuilder();
            sb.append("Scientific Name");
            sb.append(',');
            sb.append("Species");
            sb.append(',');
            sb.append("Genus");
            sb.append(',');
            sb.append("Family");
            sb.append(',');
            sb.append("BHcode");
            sb.append(',');
            sb.append("FEcode");
            sb.append(',');
            sb.append("Author");
            sb.append(',');
            sb.append("Is Accepted");
            sb.append('\n');

            for(int i=0;i<toPrint.size();i++){

                sb.append(toPrint.get(i).getScientificName());
                sb.append(',');
                sb.append(toPrint.get(i).getSpecies());
                sb.append(',');
                sb.append(toPrint.get(i).getGenus());
                sb.append(',');
                sb.append(toPrint.get(i).getFamily());
                sb.append(',');
                sb.append(toPrint.get(i).getBHcode());
                sb.append(',');
                sb.append(toPrint.get(i).getFEcode());
                sb.append(',');
                sb.append(toPrint.get(i).getAuthorship());
                sb.append(',');
                if(!toPrint.get(i).isSynonym()){
                    sb.append("Yes");
                }else{
                    sb.append("No");
                }
                sb.append('\n');
            }



            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
