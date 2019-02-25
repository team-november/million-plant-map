package database;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class DatabaseHandlerTest {
  private static DatabaseHandler databaseHandler;
  private static Connection connection;
  private Random random = new Random();
  private Synonym testSynonym;

  @BeforeClass
  /** Sets up the connection with the `herbarium_index` database. */
  public static void setUpTestClass() {
    databaseHandler = DatabaseHandler.getInstance();

    // This handles the same connection as the DatabaseHandler but
    // allows custom queries for test purposes.
    connection =
            ConnectionHandler.getConnectionHandler().getConnection();
  }

  @AfterClass
  /**
   * Closes the connection with the `herbarium_index` database.
   * Deletes all leftover records containing test data.
   */
  public static void tearDownTestClass() {
    String query = "DELETE FROM synonyms"
            + " WHERE species_name REGEXP ? ";

    // Delete any leftover synonyms.
    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, "^testName_[0-9]+$");
      ps.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Could not execute the cleanup query.");
      e.printStackTrace();
    }

    // Delete code/definition pairs from locations.
    query = "DELETE FROM locations"
            + " WHERE code REGEXP ? ";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, "^testGeographicCode_[0-9]+$");
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    clearFamilyTables();

    databaseHandler.close(); // simultaneously closes the connection.
  }

  @Before
  /** Creates pseudo-random test values for testing records. */
  public void setUp() {
    // Use a set of random test values.
    testSynonym = generateTestSynonym();
  }

  @Test
  public void insertSynonym_correctInsertedValue() {
    assertTrue(databaseHandler.insertSynonym(testSynonym));

    // Check if insertion is correct.
    Synonym insertedSynonym =
            databaseHandler.getFirstSynonymByName(testSynonym.getName());

    assertTrue(testSynonym.equals(insertedSynonym));
  }

  @Test
  public void insertSynonym_insertDuplicateEntry_onlyOneInserted() {
    assertTrue(databaseHandler.insertSynonym(testSynonym));
    assertTrue(databaseHandler.insertSynonym(testSynonym));

    ArrayList<Synonym> insertedSynonyms =
            databaseHandler.getAllSynonymsByName(testSynonym.getName());

    assertEquals(1, insertedSynonyms.size());
  }

  @Test
  public void insertSynonym_insertNullEntry_insertionDoesNotSucceed() {
    assertFalse(databaseHandler.insertSynonym(null));
  }

  @Test
  public void insertSynonym_insertInvalidEntry_speciesNameNull_insertionDoesNotSucceed() {
    Synonym invalidSynonym = new Synonym(
            null,
            testSynonym.getFamilyName(),
            testSynonym.getScheme(),
            testSynonym.getFamilyNumber(),
            testSynonym.getGenusNumber(),
            testSynonym.isAccepted(),
            testSynonym.isBasionym(),
            testSynonym.getNote());

    assertFalse(databaseHandler.insertSynonym(invalidSynonym));
  }

  @Test
  public void insertSynonym_insertInvalidEntry_familyNameNull_insertionDoesNotSucceed() {
    Synonym invalidSynonym = new Synonym(
            testSynonym.getName(),
            null,
            testSynonym.getScheme(),
            testSynonym.getFamilyNumber(),
            testSynonym.getGenusNumber(),
            testSynonym.isAccepted(),
            testSynonym.isBasionym(),
            testSynonym.getNote());

    assertFalse(databaseHandler.insertSynonym(invalidSynonym));
  }

  @Test
  public void insertSynonym_insertInvalidEntry_familyNumberNull_insertionDoesNotSucceed() {
    Synonym invalidSynonym = new Synonym(
            testSynonym.getName(),
            testSynonym.getFamilyName(),
            testSynonym.getScheme(),
            null,
            testSynonym.getGenusNumber(),
            testSynonym.isAccepted(),
            testSynonym.isBasionym(),
            testSynonym.getNote());

    assertFalse(databaseHandler.insertSynonym(invalidSynonym));
  }

  @Test
  public void deleteSynonym_getFirstSynonym_returnsNull() {
    databaseHandler.insertSynonym(testSynonym);

    // Tear down the added synonym.
    databaseHandler.deleteSynonym(testSynonym);

    // Check if the synonym was deleted.
    assertNull(databaseHandler.getFirstSynonymByName(testSynonym.getName()));
  }

  @Test
  public void deleteSynonym_getAllSynonyms_returnsEmpty() {
    databaseHandler.insertSynonym(testSynonym);

    // Tear down the added synonym.
    databaseHandler.deleteSynonym(testSynonym);

    // Check if the synonym was deleted.
    assertEquals(0, databaseHandler.getAllSynonymsByName(
            testSynonym.getName()).size());
  }

  @Test
  public void getLocationByCode_codeExists_correctLocationReturned() {
    // Insert test code/definition) pair.
    Location expectedLocation = generateTestLocation();

    String query = "INSERT INTO locations"
            + " (code, code_definition) "
            + " VALUES (?, ?)";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, expectedLocation.getCode());
      ps.setString(2, expectedLocation.getDefinition());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Execute DatabaseHandler query.
    Location actualLocation =
            databaseHandler.getLocationByCode(expectedLocation.getCode());
    assertTrue(expectedLocation.equals(actualLocation));
  }

  @Test
  public void getLocationByCode_codeDoesNotExist_noLocationReturned() {
    // Delete test code/definition) pair to make sure it does not exist.
    Location expectedLocation = generateTestLocation();

    String query = "DELETE FROM locations"
            + " WHERE code = ?";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, expectedLocation.getCode());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Execute DatabaseHandler query.
    Location actualLocation =
            databaseHandler.getLocationByCode(expectedLocation.getCode());
    assertNull(actualLocation);
  }

  @Test
  public void getFamiliesByFamilyName_familyNameExists_correctRecordsReturned() {
    // TODO implementation.
    Family expectedFamily = generateTestFamily();

    clearFamilyTables();

    String query = "INSERT INTO "
            + Family.getFamilyTableForScheme(expectedFamily.getScheme())
            + " (family_name, family_number) "
            + " VALUES (?, ?)";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, expectedFamily.getFamilyName());
      ps.setString(2, expectedFamily.getFamilyNumber());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Execute DatabaseHandler query.
    ArrayList<Family> resultList =
            databaseHandler.getFamiliesByFamilyName(expectedFamily.getFamilyName());
    assertEquals(1, resultList.size());
    assertTrue(expectedFamily.equals(resultList.get(0)));
  }

  @Test
  public void getFamiliesByFamilyName_familyNameDoesNotExist_noRecordsReturned() {
    // TODO implementation.
  }

  @Test
  public void getGeneraByGenusName_genusNameExists_correctRecordsReturned() {
    // TODO implementation.
  }

  @Test
  public void getGeneraByGenusName_genusNameDoesNotExist_noRecordsReturned() {
    // TODO implementation.
  }


  @Test
  public void getLocationsByGenusName_genusNameAndLocationExist_correctLocationsReturned() {
    // TODO implementation.
  }

  @Test
  public void getLocationsByGenusName_locationDoesNotExist_noRecordsReturned() {
    // TODO implementation.
  }

  @Test
  public void getLocationsByGenusName_genusDoesNotExist_noRecordsReturned() {
    // TODO implementation.
  }

  @Test
  public void getLocationsBySynonymName_synonymNameAndLocationExist_correctLocationsReturned() {
    // TODO implementation.
  }

  @Test
  public void getLocationsBySynonymName_synonymNameDoesNotExist_noRecordsReturned() {
    // TODO implementation.
  }

  @Test
  public void getLocationsBySynonymName_LocationDoesNotExist_noRecordsReturned() {
    // TODO implementation.
  }

  private Synonym generateTestSynonym() {
    // Use a set of random test values.
    String testSynonymName = "testName_" + random.nextInt(65536);
    String testFamilyName = "testFamilyName_" + random.nextInt(65536);
    IndexScheme testIndexScheme = IndexScheme
            .values()[random.nextInt(IndexScheme.values().length)];
    String testFamilyNumber = "testFamilyNumber_" + random.nextInt(65536);
    String testGenusNumber = "testGenusNumber_" + random.nextInt(65536);
    boolean testIsAccepted = (random.nextInt(2) == 1);
    boolean testIsBasionym = (random.nextInt(2) == 1);
    String testNote = "testNote_" + random.nextInt(65536);

    return new Synonym(
            testSynonymName,
            testFamilyName,
            testIndexScheme,
            testFamilyNumber,
            testGenusNumber,
            testIsAccepted,
            testIsBasionym,
            testNote);
  }

  private Family generateTestFamily() {
    IndexScheme testIndexScheme = IndexScheme
            .values()[random.nextInt(IndexScheme.values().length)];

    return generateTestFamilyForScheme(testIndexScheme);
  }

  private Family generateTestFamilyForScheme(IndexScheme scheme) {
    IndexScheme testIndexScheme = scheme;
    String testFamilyName = "testFamilyName_" + random.nextInt(65536);
    String testFamilyNumber = "testFamilyNumber_" + random.nextInt(65536);

    return new Family(
            testIndexScheme,
            testFamilyName,
            testFamilyNumber);
  }

  private Genus generateTestGenus() {
    IndexScheme testIndexScheme = IndexScheme
            .values()[random.nextInt(IndexScheme.values().length)];
    String testGenusName = "testGenusName_" + random.nextInt(65536);
    String testFamilyNumber = "testFamilyNumber_" + random.nextInt(65536);
    String testGenusNumber = "testGenusNumber_" + random.nextInt(65536);

    return new Genus(
            testIndexScheme,
            testGenusName,
            testFamilyNumber,
            testGenusNumber);
  }

  private Location generateTestLocation() {
    String testCode = "testGeographicCode_" + random.nextInt(65536);
    String testDefinition = "testCodeDefinition_" + random.nextInt(65536);

    return new Location(testCode, testDefinition);
  }

  private static void clearFamilyTables() {
    for (IndexScheme scheme : IndexScheme.values()) {
      if (scheme != IndexScheme.OTHER) {
        String query = "DELETE FROM "
                + Family.getFamilyTableForScheme(scheme)
                + " WHERE family_name REGEXP ? ";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
          ps.setString(1, "^testFamilyName_[0-9]+$");
          ps.executeUpdate();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

  }
}