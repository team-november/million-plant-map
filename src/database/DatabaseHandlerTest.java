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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DatabaseHandlerTest {
  private Random random = new Random();
  private static DatabaseHandler databaseHandler;
  private Synonym testSynonym;

  @BeforeClass
  /** Sets up the connection with the `herbarium_index` database. */
  public static void setUpTestClass() {
    databaseHandler = DatabaseHandler.getInstance();
  }

  @AfterClass
  /**
   * Closes the connection with the `herbarium_index` database.
   * Deletes all leftover records containing test data.
   */
  public static void tearDownTestClass() {
    // This handles the same connection as the DatabaseHandler but
    // allows custom queries for test purposes.
    Connection connection =
            ConnectionHandler.getConnectionHandler().getConnection();

    String deleteQuery = "DELETE FROM synonyms"
            + " WHERE species_name REGEXP ? ";

    try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
      ps.setString(1, "^testName_[0-9]+$");
      ps.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Could not execute the cleanup query.");
      e.printStackTrace();
    }

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
    databaseHandler.insertSynonym(testSynonym);

    // Check if insertion is correct.
    Synonym insertedSynonym =
            databaseHandler.getFirstSynonymByName(testSynonym.getName());

    assertTrue(testSynonym.equals(insertedSynonym));
  }

  @Test
  public void insertSynonym_insertDuplicateEntry_onlyOneInserted() {
    databaseHandler.insertSynonym(testSynonym);
    databaseHandler.insertSynonym(testSynonym);

    ArrayList<Synonym> insertedSynonyms =
            databaseHandler.getAllSynonymsByName(testSynonym.getName());

    assertEquals(1, insertedSynonyms.size());
  }

  @Test
  public void insertSynonym_insertNullEntry_insertionDoesNotSucceed() {
    // TODO implementation.
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
    // TODO implementation.
  }

  @Test
  public void getLocationByCode_codeDoesNotExist_noLocationReturned() {
    // TODO implementation.
  }

  @Test
  public void getFamiliesByFamilyName_familyNameExists_correctRecordReturned() {
    // TODO implementation.
  }

  @Test
  public void getFamiliesByFamilyName_familyNameDoesNotExist_noFamiliesReturned() {
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
}