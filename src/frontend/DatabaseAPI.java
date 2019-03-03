package frontend;

import api.Species;
import database.DatabaseHandler;
import database.IndexScheme;
import database.Synonym;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;

public class DatabaseAPI {

    private static DatabaseHandler databaseHandler;

    public DatabaseAPI() {
        databaseHandler = DatabaseHandler.getInstance();
    }

    public void updateEntry(SpeciesItem speciesItem){

        String codes = speciesItem.getCodes().get().getText();

        Synonym synonym = new Synonym(speciesItem.getScientificString(),
                speciesItem.getFamily().toString(), parseScheme(codes), parseFamily(codes), parseGenus(codes),
                !speciesItem.isSynonym(), speciesItem.isBasionym(), speciesItem.getNotes().toString());


        String newCode = speciesItem.getCode();
        synonym.setScheme(parseScheme(newCode));
        synonym.setFamilyNumber(parseFamily(newCode));
        synonym.setGenusNumber(parseGenus(codes));
        
        synonym.setNote(speciesItem.getNote());


        //update entry based on species if it exists, otherwise create it
        // The database handler already checks for duplicates, so just pass straight to
        // insert synonym function
        boolean success = databaseHandler.insertSynonym(synonym);
    }


    public void deleteEntry(SpeciesItem speciesItem, String oldCode, String oldNote){
        //delete entry based on species if it exists

        // get the species from the database
        Species species = speciesItem.getSpeciesObject();
        species.setNote(oldNote);
        species.setCodes(oldCode);

        // get the name from the species object
        String name = species.getScientificName();

        // get database synonym that matches that name
        Synonym synonym = databaseHandler.getFirstSynonymByName(name);

        // delete the synonym, if it existed
        if(synonym != null){
            databaseHandler.deleteSynonym(synonym);
        }

    }

    public Species getEntry(String scientificName){
        //get Synonym from database and convert to species object

        Species speciesResult;

        // Fetch the first synonym name from the table
        Synonym synonym = databaseHandler.getFirstSynonymByName(scientificName);

        // If the synonym is not a null object create the Species object from its fields
        if(synonym != null){
            speciesResult = Species.convertSynonymToSpecies(synonym);
            return speciesResult;
        } else {

            return null;
        }

    }

    private static Synonym speciesToSynonym(SpeciesItem speciesItem) {
        String codes = speciesItem.getCodes().get().getText();

        return new Synonym(speciesItem.getCanonicalName().toString(),
                speciesItem.getFamily().toString(), parseScheme(codes), parseFamily(codes), parseGenus(codes),
                !speciesItem.isSynonym(), speciesItem.isBasionym(), "Testing");
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
        try {
            if (parseScheme(codes) == IndexScheme.OTHER) {
                return "";
            } else {
                return codes.split(":")[1].split("/")[1];
            }
        } catch (IndexOutOfBoundsException e){
            return "";
        }
    }

}
