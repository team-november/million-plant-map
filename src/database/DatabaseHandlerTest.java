package database;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DatabaseHandlerTest {
  private Random random = new Random();
  private static DatabaseHandler databaseHandler;
  private Synonym testSynonym;

  @BeforeClass
  /** Sets up the connection with the `herbarium_index` database. */
  public static void setUpClass() {
    databaseHandler = DatabaseHandler.getInstance();
  }

  @AfterClass
  /** Closes the connection with the `herbarium_index` database. */
  public static void tearDownClass() {
     databaseHandler.close();
  }

  @Before
  /** Creates pseudo-random test values for testing records. */
  public void setUp() {
    // Use a set of random test values.
    testSynonym = generateTestSynonym();
  }

  @Test
  public void insertSynonym() {
    databaseHandler.insertSynonym(testSynonym);

    // Check if insertion is correct.
    Synonym insertedSynonym =
            databaseHandler.getFirstSynonymByName(testSynonym.getName());

    assertTrue(testSynonym.equals(insertedSynonym));

    // Tear down the added synonym.
    databaseHandler.deleteSynonym(testSynonym);
  }

  @Test
  public void deleteSynonym_getFirstSynonym_returnsNull() {
    databaseHandler.insertSynonym(testSynonym);

    // Tear down the added synonym.
    databaseHandler.deleteSynonym(testSynonym);

    // Check if the synonym was deleted.
    assertNull(databaseHandler.getFirstSynonymByName(testSynonym.getName()));
  }

//  @Test
//  public void getLocationByCode() {
//  }
//
//  @Test
//  public void getFamiliesByFamilyName() {
//  }
//
//  @Test
//  public void getGeneraByGenusName() {
//  }
//
//  @Test
//  public void getLocationsByGenusName() {
//  }
//
//  @Test
//  public void getLocationsByGenus() {
//  }
//
//  @Test
//  public void getLocationsBySynonymName() {
//  }
//
//  @Test
//  public void getLocationsBySynonym() {
//  }

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