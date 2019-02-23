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
        synonym.setScheme(getIndexScheme(resultSet.getString(3)));
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

    return null;
  }

  public Family getFamily(String name) {

    return null;
  }

  public Genus getGenus(String name) {

    return null;
  }

  private IndexScheme getIndexScheme(String scheme) {
    if (scheme.equals("GB_AND_I")) return IndexScheme.GB_AND_I;
    else if (scheme.equals("FLORA_EUROPAEA")) return IndexScheme.FLORA_EUROPAEA;
    else if (scheme.equals("BENTHAM_HOOKER")) return IndexScheme.BENTHAM_HOOKER;
    else return IndexScheme.OTHER;
  }

}