package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseHandler {
  private Connection  connection = ConnectionHandler.getConnectionHandler()
                                                    .getConnection();

  public void insertSynonym(Synonym synonym) {
    String query = "INSERT INTO synonyms"
      + " (species_name, family_name, index_scheme, family_number, "
      + " genus_number, is_accepted, is_basionym, note)" 
      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, synonym.getName().toLowerCase());
      ps.setString(2, synonym.getScheme().toString().toLowerCase());
      ps.setString(3, synonym.getFamilyName().toLowerCase());
      ps.setString(4, synonym.getFamilyNumber().toLowerCase());
      ps.setString(5, synonym.getGenusNumber().toLowerCase());
      ps.setBoolean(6, synonym.isAccepted());
      ps.setBoolean(7, synonym.isBasionym());
      ps.setString(8, synonym.getNote());
      ps.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Could not execute insertSynonym query.");
      e.printStackTrace();
    }
  }

  public ArrayList<Synonym> getSynonyms(String name) {
    ArrayList<Synonym> synonyms = new ArrayList<>(); 

    String query = "SELECT "
      + " (species_name, family_name, index_scheme, family_number, "
      + " genus_number, is_accepted, is_basionym, note)"
      + " FROM synonyms" 
      + " WHERE species_name = ?";
    
    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, name);
      ResultSet resultSet = ps.executeQuery();

      // Creates a Synonym object for each row in the ResultSet.
      while (resultSet.next()) {
        Synonym synonym = new Synonym();
        synonym.setName(resultSet.getString(1));
        synonym.setFamilyName(resultSet.getString(2));
        synonym.setScheme(stringToIndexScheme(resultSet.getString(3)));
        synonym.setFamilyNumber(resultSet.getString(4));
        synonym.setGenusNumber(resultSet.getString(5));
        synonym.setAccepted(resultSet.getBoolean(6));
        synonym.setBasionym(resultSet.getBoolean(7));
        synonym.setNote(resultSet.getString(8));
        synonyms.add(synonym);
      }

    } catch (SQLException e) {
      System.err.println("Could not execute getSynonym query.");
      e.printStackTrace();
    }
    return synonyms;
  }

  public Synonym getFirstSynonym(String name) {
    ArrayList<Synonym> synonyms = getSynonyms(name);
    return (synonyms != null) ? synonyms.get(0) : null;
  }

  public Location getLocation(String code) {
    Location location = null;

    String query = "SELECT "
      + " (code, code_definition)"
      + " FROM locations" 
      + " WHERE code = ?";
    
    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, code);
      ResultSet resultSet = ps.executeQuery();

      // Assuming there is a single interpretation to a given geo code.
      if (resultSet.first()) {
        location = new Location(resultSet.getString(1),
                                resultSet.getString(2));
      }

    } catch (SQLException e) {
      System.err.println("Could not execute getLocation query.");
      e.printStackTrace();
    }

    return location;
  }

  public ArrayList<Family> getFamilies(String name) {
    ArrayList<Family> families = new ArrayList<>();
    
    families.addAll(getFamiliesForScheme(name, IndexScheme.GB_AND_I));
    families.addAll(getFamiliesForScheme(name, IndexScheme.FLORA_EUROPAEA));
    families.addAll(getFamiliesForScheme(name, IndexScheme.BENTHAM_HOOKER));

    return families;
  }

  private ArrayList<Family> getFamiliesForScheme(String name, 
                                                 IndexScheme scheme) {
    ArrayList<Family> families = new ArrayList<>();
    
    String tableName;
    if (scheme == IndexScheme.GB_AND_I) tableName = "families_gbi";
    else if (scheme == IndexScheme.BENTHAM_HOOKER) tableName = "families_bh";
    else if (scheme == IndexScheme.FLORA_EUROPAEA) tableName = "families_fe";
    else return families;

    String query = "SELECT "
      + " (family_name, family_number)"
      + " FROM " + tableName 
      + " WHERE family_name = ?";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, name);

      ResultSet resultSet = ps.executeQuery();

      // Creates a Family object for each row in the ResultSet.
      while (resultSet.next()) {
        Family family = new Family(scheme,
                                   resultSet.getString(1),
                                   resultSet.getString(2));
        families.add(family);
      }

    } catch (SQLException e) {
      System.err.println("Could not execute getFamilies query.");
      e.printStackTrace();
    }

    return families;
  }

  public ArrayList<Genus> getGenera(String name) {
    ArrayList<Genus> genera = new ArrayList<>();
    
    genera.addAll(getGeneraForScheme(name, IndexScheme.GB_AND_I));
    genera.addAll(getGeneraForScheme(name, IndexScheme.FLORA_EUROPAEA));
    genera.addAll(getGeneraForScheme(name, IndexScheme.BENTHAM_HOOKER));

    return genera;
  }

  private ArrayList<Genus> getGeneraForScheme(String name,
                                              IndexScheme scheme) {
    ArrayList<Genus> genera = new ArrayList<>();

    String tableName;
    if (scheme == IndexScheme.GB_AND_I) tableName = "genera_gbi";
    else if (scheme == IndexScheme.BENTHAM_HOOKER) tableName = "genera_bh";
    else if (scheme == IndexScheme.FLORA_EUROPAEA) tableName = "genera_fe";
    else return genera;

    String query = "SELECT "
      + " (genus_name, family_number, genus_number)"
      + " FROM " + tableName 
      + " WHERE genus_name = ?";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, name);

      ResultSet resultSet = ps.executeQuery();

      // Creates a Family object for each row in the ResultSet.
      while (resultSet.next()) {
        Genus genus = new Genus(scheme,
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3));
        genera.add(genus);
      }

    } catch (SQLException e) {
      System.err.println("Could not execute getGenera query.");
      e.printStackTrace();
    }
    
    return genera;
  }

  private IndexScheme stringToIndexScheme(String scheme) {
    if (scheme.equals("GB_AND_I")) return IndexScheme.GB_AND_I;
    else if (scheme.equals("FLORA_EUROPAEA")) return IndexScheme.FLORA_EUROPAEA;
    else if (scheme.equals("BENTHAM_HOOKER")) return IndexScheme.BENTHAM_HOOKER;
    else return IndexScheme.OTHER;
  }

}