package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Provides the methods for interacting with the `herbarium_index` database.
 * 
 * Includes queries to insert and retrieve {@link Synonym} records as well as
 * retrieving the family/genus numbers information.
 * 
 * Implements the Singleton pattern, meaning that only one instance can exist
 * in the application.
 */
public final class DatabaseHandler {
  private static final DatabaseHandler INSTANCE = new DatabaseHandler();
  private static Connection connection = 
    ConnectionHandler.getConnectionHandler().getConnection();

  /** Private constructor for Singleton pattern implementation */
  private DatabaseHandler() {}

  /** Public access method to the Singleton DatabaseHandler instance. */
  public static DatabaseHandler getInstance() {
    return INSTANCE;
  }

  /**
   * Inserts the prepared Synonym as a record in the herbarium database.
   * 
   * A Synonym corresponds to a row in the `synonyms` table of the
   * `herbarium_index`.
   * 
   * @param synonym the Synonym to be inserted.
   * @return true if insertion successful or there already exists a
   * duplicate entry
   */
  public boolean insertSynonym(Synonym synonym) {
    if (synonym == null || !synonym.isValid()) return false;

    // Check for existing duplicates.
    boolean duplicates = true;
    String query =  "SELECT COUNT(*) FROM synonyms"
            + " WHERE species_name = ? "
            + " AND family_name = ?"
            //+ " AND index_scheme = ?"
            //+ " AND family_number = ?"
            //+ " AND genus_number = ?"
            + " AND is_accepted = ?"
            + " AND is_basionym = ?"
            + " AND note = ?";

    try(PreparedStatement ps = connection.prepareStatement(query)) {
      prepareSynonymDuplicateStatement(ps, synonym);
      ResultSet resultSet = ps.executeQuery();
      if (resultSet.next())
        duplicates = (resultSet.getInt(1) >= 1);
    } catch (SQLException e) {
      System.err.println("Could not execute insertSynonym query.");
      e.printStackTrace();
      return false;
    }

    if (!duplicates) {
      query = "INSERT INTO synonyms"
              + " (species_name, family_name, index_scheme, family_number, "
              + " genus_number, is_accepted, is_basionym, note)"
              + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

      try (PreparedStatement ps = connection.prepareStatement(query)) {
        prepareSynonymStatement(ps, synonym);
        ps.executeUpdate();
      } catch (SQLException e) {
        System.err.println("Could not execute insertSynonym query.");
        e.printStackTrace();
        return false;
      }
    }

    return true;
  }

  /**
   * Deletes the records matching given Synonym from `herbarium_index`.
   * @param synonym the Synonym to be deleted.
   */
  public void deleteSynonym(Synonym synonym) {
    String query = "DELETE FROM synonyms"
            + " WHERE species_name = ? "
            + " AND family_name = ?"
            + " AND index_scheme = ?"
            + " AND family_number = ?"
            + " AND genus_number = ?"
            + " AND is_accepted = ?"
            + " AND is_basionym = ?"
            + " AND note = ?";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      prepareSynonymStatement(ps, synonym);
      ps.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Could not execute deleteSynonym query.");
      e.printStackTrace();
    }
  }

  /**
   * Prepares the parameters for the `synonym` table queries in
   * `herbarium_index` - just for checking duplicates
   *
   * @param ps the statement to be prepared with {@link Synonym} data
   * @param synonym the {@link Synonym} data
   */
  private void prepareSynonymDuplicateStatement(PreparedStatement ps, Synonym synonym)
          throws SQLException {
    ps.setString(1, synonym.getName().trim());
    ps.setString(2, synonym.getFamilyName().trim());
    //ps.setString(3, synonym.getScheme().toString().trim());
    //ps.setString(4, synonym.getFamilyNumber().trim());
    //ps.setString(5, synonym.getGenusNumber().trim());
    ps.setBoolean(3, synonym.isAccepted());
    ps.setBoolean(4, synonym.isBasionym());
    ps.setString(5, synonym.getNote().trim());

  }

  /** 
   * Prepares the parameters for the `synonym` table queries in
   * `herbarium_index`.
   * 
   * @param ps the statement to be prepared with {@link Synonym} data
   * @param synonym the {@link Synonym} data
   */
  private void prepareSynonymStatement(PreparedStatement ps, Synonym synonym)
          throws SQLException {
    ps.setString(1, synonym.getName().trim());
    ps.setString(2, synonym.getFamilyName().trim());
    ps.setString(3, synonym.getScheme().toString().trim());
    ps.setString(4, synonym.getFamilyNumber().trim());
    ps.setString(5, synonym.getGenusNumber().trim());
    ps.setBoolean(6, synonym.isAccepted());
    ps.setBoolean(7, synonym.isBasionym());
    ps.setString(8, synonym.getNote().trim());

  }

  /**
   * Retrieves all records associated with a given species name.
   * 
   * This method queries the `synonyms` table of `herbarium_index`
   * and returns the records that have a matching `species_name`
   * attribute. The rows are converted to Synonym objects.
   *  
   * @param name the species name.
   * @return the list of synonym records matching this species name.
   */
  public ArrayList<Synonym> getAllSynonymsByName(String name) {
    ArrayList<Synonym> synonyms = new ArrayList<>(); 

    String query = "SELECT "
      + " species_name, family_name, index_scheme, family_number, "
      + " genus_number, is_accepted, is_basionym, note"
      + " FROM synonyms" 
      + " WHERE species_name = ?";
    
    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, name.trim());
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

  /**
   * Retrieves the first record matching the given species name.
   * 
   * See {@link #getAllSynonymsByName(String) getAllSyonymsByName} method.
   * 
   * @param name the species name.
   * @return the first synonym record matching this species name.
   */
  public Synonym getFirstSynonymByName(String name) {
    ArrayList<Synonym> synonyms = getAllSynonymsByName(name);
    return (synonyms.size() > 0) ? synonyms.get(0) : null;
  }

  /**
   * Returns the family numbers for given family name, and an empty list
   * when there are no matching records.
   * 
   * This queries the `families_*` tables in `herbarium_index`
   * for every indexing scheme and returns the correspoding family
   * numbers, wrapped in a {@link Family} object.
   * 
   * The schemes that are queried are provided by the {@link IndexScheme}
   * type (and usually excludes {@link IndexScheme#OTHER}).
   * 
   * @param name name of the family
   * @return the list of {@link Family} records containing family numbers.
   */
  public ArrayList<Family> getFamiliesByFamilyName(String name) {
    ArrayList<Family> families = new ArrayList<>();
    
    for (IndexScheme scheme : IndexScheme.values()) {
      if (scheme != IndexScheme.OTHER) {
        families.addAll(getFamiliesByScheme(name, scheme));
      }
    }

    return families;
  }

  /**
   * Returns the family number of a given name in a given indexing scheme.
   * 
   * This queries the appropriate `families_*` table in `herbarium_index`
   * that contains family numbers for the requested indexing scheme 
   * (see {@link IndexScheme}).
   * 
   * @param name name of the family
   * @param scheme the indexing scheme under which the family number is needed
   * @return the list of {@link Family} records containing family numbers. 
   * Returns null for the {@link IndexScheme#OTHER} scheme which is reserverd
   * for the `synonyms` table.
   */
  private ArrayList<Family> getFamiliesByScheme(String name, 
                                                IndexScheme scheme) {
    ArrayList<Family> families = new ArrayList<>();
    
    String tableName = Family.getFamilyTableForScheme(scheme);
    if (tableName == null) return null;

    String query = "SELECT "
      + " family_name, family_number"
      + " FROM " + tableName 
      + " WHERE family_name = ?";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, name.trim());

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

  /**
   * Returns the genus numbers of a given name for supported indexing schemes.
   * 
   * This queries all `genera_*` tables in `herbarium_index`
   * that contain genus numbers for the supported indexing schemes 
   * (see {@link IndexScheme}).
   * 
   * @param name name of the genus
   * @return the list of {@link Genus} records containing genus numbers.
   */
  public ArrayList<Genus> getGeneraByGenusName(String name) {
    ArrayList<Genus> genera = new ArrayList<>();
    
    for (IndexScheme scheme : IndexScheme.values()) {
      if (scheme != IndexScheme.OTHER) {
        genera.addAll(getGeneraByScheme(name, scheme));
      }
    }

    return genera;
  }

  /**
   * Returns the genus number of the genus under a given indexing scheme.
   * 
   * This queries the appropriate `genera_*` table in `herbarium_index`
   * that contains genus numbers for the requested indexing scheme 
   * (see {@link IndexScheme}).
   * 
   * @param name name of the genus
   * @param scheme the indexing scheme under which the genus number is needed
   * @return the list of {@link Genus} records containing genus numbers. 
   * Returns null for the {@link IndexScheme#OTHER} scheme which is reserved
   * for the `synonyms` table.
   */
  private ArrayList<Genus> getGeneraByScheme(String name,
                                             IndexScheme scheme) {
    ArrayList<Genus> genera = new ArrayList<>();

    String tableName = Genus.getGenusTableForScheme(scheme);
    if (tableName == null) return null;

    String query = "SELECT "
      + " genus_name, family_number, genus_number"
      + " FROM " + tableName 
      + " WHERE genus_name = ?";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, name.trim());

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
  
  /**
   * Converts the given string into the corresponding {@link IndexScheme}.
   * 
   * @param scheme the name of scheme
   * @return the {@link IndexScheme} type
   */
  private IndexScheme stringToIndexScheme(String scheme) {
    if (scheme.equalsIgnoreCase("GB_AND_I")) 
      return IndexScheme.GB_AND_I;
    else if (scheme.equalsIgnoreCase("FLORA_EUROPAEA")) 
      return IndexScheme.FLORA_EUROPAEA;
    else if (scheme.equalsIgnoreCase("BENTHAM_HOOKER")) 
      return IndexScheme.BENTHAM_HOOKER;
    else 
      return IndexScheme.OTHER;
  }

  /** Closes the handler resources. */
  public void close() {
    ConnectionHandler.getConnectionHandler().close();
  }

}