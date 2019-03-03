package frontend;

import api.Species;
import database.DatabaseHandler;
import database.IndexScheme;
import database.Synonym;

public class DatabaseAPI {

    private static DatabaseHandler databaseHandler;

    public DatabaseAPI() {
        databaseHandler = DatabaseHandler.getInstance();
    }

    public void updateEntry(SpeciesItem speciesItem){
        String codes = speciesItem.getCodes().get().getText();

        Synonym synonym = new Synonym(speciesItem.getCanonicalName().toString(),
                speciesItem.getFamily().toString(), parseScheme(codes), parseFamily(codes), parseGenus(codes),
                !speciesItem.isSynonym(), speciesItem.isBasionym(), "Testing");

        //update entry based on species if it exists, otherwise create it
        // The database handler already checks for duplicates, so just pass straight to
        // insert synonym function
        databaseHandler.insertSynonym(synonym);
    }

    public void deleteEntry(SpeciesItem speciesItem){
        //delete entry based on species if it exists

        // get the species from the database
        Species species = speciesItem.getSpeciesObject();

        // get the name from the species object
        String name = species.getScientificName();

        // get database synonym that matches that name
        Synonym synonym = databaseHandler.getFirstSynonymByName(name);

        // delete the synonym, if it existed
        if(synonym != null){
            databaseHandler.deleteSynonym(synonym);
        }

    }

    public Species getEntry(String query){
        //get Synonym from database and convert to species object

        Species speciesResult;

        // Fetch the first synonym name from the table
        Synonym synonym = databaseHandler.getFirstSynonymByName(query);

        // If the synonym is not a null object create the Species object from its fields
        if(synonym != null){
            speciesResult = Species.convertSynonymToSpecies(synonym);
            return speciesResult;
        }

        return null;

    }


    private static IndexScheme parseScheme(String codes) {
        String[] split = codes.split(":");
        if(split.length > 2){
            return IndexScheme.OTHER;
        }else{
            switch (split[0]){
                case "FE": return IndexScheme.FLORA_EUROPAEA;
                case "BH": return IndexScheme.BENTHAM_HOOKER;
                case "FOGBI": return IndexScheme.GB_AND_I;
                default: return IndexScheme.OTHER;
            }
        }
    }

    private static String parseFamily(String codes) {
        if(parseScheme(codes)==IndexScheme.OTHER){
            return codes;
        }else{
            return codes.split(":")[1].split("/")[0];
        }
    }

    private static String parseGenus(String codes) {
        if(parseScheme(codes)==IndexScheme.OTHER){
            return null;
        }else{
            return codes.split(":")[1].split("/")[1];
        }
    }

}
