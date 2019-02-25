package FrontEnd;

import api.Species;

import java.util.List;

public class DataCollectedTest {
    private static boolean testFamily(List<Species> synonims){
        for(int i=0;i<synonims.size();i++){
            if(!synonims.get(i).getFamily().equals("Orchidaceae")){
                return false;
            }
        }
        return true;
    }
    private static boolean testSpecies(List<Species> synonims){
        if(!synonims.get(0).getScientificName().equals("Vanda cristata Lindl.")){
            return false;
        }
        if(!synonims.get(1).getScientificName().equals("Aerides cristata (Wall. ex Lindl.) Wall.")){
            return false;
        }
        if(!synonims.get(2).getScientificName().equals("Aerides cristata (Wall. ex Lindl.) Wall. ex Hook.f.")){
            return false;
        }
        if(!synonims.get(3).getScientificName().equals("Luisia striata (Rchb.f.) Kraenzl.")){
            return false;
        }
        if(!synonims.get(4).getScientificName().equals("Trudelia cristata (Lindl.) Senghas")){
            return false;
        }
        if(!synonims.get(5).getScientificName().equals("Trudelia cristata (Wall. ex Lindl.) Senghas ex Roeth")){
            return false;
        }
        return synonims.get(6).getScientificName().equals("Vanda striata Rchb.f.");
    }
    private static boolean testAccepted(List<Species> synonims){
        boolean oneAccepted = false;
        for(int i=0;i<synonims.size();i++){
            if(!synonims.get(i).isSynonym() && !oneAccepted){
                oneAccepted = true;
            }else if(!synonims.get(i).isSynonym()){
                return false;
            }
        }
        return true;
    }


    // Try to call function for vanda cristata and compare with expected output
    public static void main(String[] args) {
        DataCollected dc = new DataCollected();
        List<Species> synonims = dc.dataToPrint("vanda cristata");
        if(!testFamily(synonims) || !testSpecies(synonims) || !testAccepted(synonims)){
            System.out.println("The function is returning an error for the input vanda cristata");
        }else{
            System.out.println("The function dataToPrint in DataCollected works as expected");
        }
    }
}
