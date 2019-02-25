package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseHandlerTest {
  private static DatabaseHandler databaseHandler;
  private static Connection connection;
  private Random random = new Random();

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
    clearSynonymTables();
    clearFamilyTables();
    clearGenusTables();

    databaseHandler.close(); // simultaneously closes the connection.
  }

  @Before
  /** Creates pseudo-random test values for testing records. */
  public void setUp() {
    clearSynonymTables();
    clearFamilyTables();
    clearGenusTables();
  }

  @Test
  public void insertSynonym_correctInsertedValue() {
    Synonym testSynonym = generateTestSynonym();
    assertTrue(databaseHandler.insertSynonym(testSynonym));

    // Check if insertion is correct.
    Synonym insertedSynonym =
            databaseHandler.getFirstSynonymByName(testSynonym.getName());

    assertTrue(testSynonym.equals(insertedSynonym));
  }

  @Test
  public void insertSynonym_insertDuplicateEntry_onlyOneInserted() {
    Synonym testSynonym = generateTestSynonym();
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
    Synonym testSynonym = generateTestSynonym();
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
    Synonym testSynonym = generateTestSynonym();
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
    Synonym testSynonym = generateTestSynonym();
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
    Synonym testSynonym = generateTestSynonym();
    databaseHandler.insertSynonym(testSynonym);

    // Tear down the added synonym.
    databaseHandler.deleteSynonym(testSynonym);

    // Check if the synonym was deleted.
    assertNull(databaseHandler.getFirstSynonymByName(testSynonym.getName()));
  }

  @Test
  public void deleteSynonym_getAllSynonyms_returnsEmpty() {
    Synonym testSynonym = generateTestSynonym();
    databaseHandler.insertSynonym(testSynonym);

    // Tear down the added synonym.
    databaseHandler.deleteSynonym(testSynonym);

    // Check if the synonym was deleted.
    assertEquals(0, databaseHandler.getAllSynonymsByName(
            testSynonym.getName()).size());
  }

  @Test
  public void getFamiliesByFamilyName_familyNameExistsForOneScheme_correctRecordReturned() {
    clearFamilyTables();
    Family expectedFamily = generateTestFamily();

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
  public void getFamiliesByFamilyName_familyNameExistsInMultipleSchemes_correctRecordsReturned() {
    Family[] testFamilies = generateTestFamiliesForSchemes();

    // Insert test families into the database
    for (Family family : testFamilies) {
      String query = "INSERT INTO "
        + Family.getFamilyTableForScheme(family.getScheme())
        + " (family_name, family_number) "
        + " VALUES (?, ?)";

      try (PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, family.getFamilyName());
        ps.setString(2, family.getFamilyNumber());
        ps.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    // Execute DatabaseHandler query.
    ArrayList<Family> resultList =
      databaseHandler.getFamiliesByFamilyName(testFamilies[0].getFamilyName());
    assertEquals(testFamilies.length, resultList.size());
    for (Family actual : resultList) {
      assertTrue(containsFamily(testFamilies, actual));
    }
  }

  @Test
  public void getFamiliesByFamilyName_familyNameDoesNotExist_emptyRecordReturned() {
    Family testFamily = generateTestFamily();

    ArrayList<Family> resultList =
      databaseHandler.getFamiliesByFamilyName(testFamily.getFamilyName());
    assertEquals(0, resultList.size());
  }

  @Test
  public void getGeneraByGenusName_genusNameExistsForOneScheme_correctRecordReturned() {
    clearGenusTables();
    Genus expectedGenus = generateTestGenus();

    String query = "INSERT INTO "
            + Family.getFamilyTableForScheme(expectedGenus.getScheme())
            + " (genus_name, family_number, genus_number) "
            + " VALUES (?, ?, ?)";

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, expectedGenus.getGenusName());
      ps.setString(2, expectedGenus.getFamilyNumber());
      ps.setString(3, expectedGenus.getGenusNumber());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Execute DatabaseHandler query.
    ArrayList<Genus> resultList =
      databaseHandler.getGeneraByGenusName(expectedGenus.getGenusName());
    assertEquals(1, resultList.size());
    assertTrue(expectedGenus.equals(resultList.get(0)));
  }

  @Test
  public void getGeneraByGenusName_genusNameExistsForMultipleSchemes_correctRecordsReturned() {
    Genus[] testGenera = generateTestGeneraForSchemes();

    // Insert test families into the database
    for (Genus genus : testGenera) {
      String query = "INSERT INTO "
        + Family.getFamilyTableForScheme(genus.getScheme())
        + " (genus_name, family_number, genus_number) "
        + " VALUES (?, ?, ?)";

      try (PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, genus.getGenusName());
        ps.setString(2, genus.getFamilyNumber());
        ps.setString(3, genus.getGenusNumber());
        ps.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    // Execute DatabaseHandler query.
    ArrayList<Genus> resultList =
      databaseHandler.getGeneraByGenusName(testGenera[0].getGenusName());
    assertEquals(testGenera.length, resultList.size());
    for (Genus actual : resultList) {
      assertTrue(containsGenus(testGenera, actual));
    }
  }

  @Test
  public void getGeneraByGenusName_genusNameDoesNotExist_emptyRecordReturned() {
    Genus testGenus = generateTestGenus();

    ArrayList<Genus> resultList =
      databaseHandler.getGeneraByGenusName(testGenus.getGenusName());
    assertEquals(0, resultList.size());
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
            .values()[random.nextInt(IndexScheme.values().length)-1];
    String testFamilyName = "testFamilyName_" + random.nextInt(65536);
    String testFamilyNumber = "testFamilyNumber_" + random.nextInt(65536);

    return new Family(testIndexScheme, testFamilyName, testFamilyNumber);
  }

  private Family[] generateTestFamiliesForSchemes() {
    String testFamilyName = "testFamilyName_" + random.nextInt(65536);

    int length = IndexScheme.values().length - 1; // Excludes IndexScheme.OTHER
    String[] testFamilyNumbers = new String[length];
    Family[] testFamilies = new Family[length];

    int i = 0;
    for (IndexScheme scheme : IndexScheme.values()) {
      if (scheme != IndexScheme.OTHER && i < length) {
        testFamilyNumbers[i] = "testFamilyNumber_" + random.nextInt(65536);
        testFamilies[i] = new Family(scheme, testFamilyName, testFamilyNumbers[i]);
        i++;
      }
    }

    return testFamilies;
  }

  private Genus generateTestGenus() {
    IndexScheme testIndexScheme = IndexScheme
            .values()[random.nextInt(IndexScheme.values().length)-1];
    String testGenusName = "testGenusName_" + random.nextInt(65536);
    String testFamilyNumber = "testFamilyNumber_" + random.nextInt(65536);
    String testGenusNumber = "testGenusNumber_" + random.nextInt(65536);

    return new Genus(
            testIndexScheme,
            testGenusName,
            testFamilyNumber,
            testGenusNumber);
  }

  private Genus[] generateTestGeneraForSchemes() {
    String testGenusName = "testGenusName_" + random.nextInt(65536);

    int length = IndexScheme.values().length - 1; // Excludes IndexScheme.OTHER
    String[] testFamilyNumbers = new String[length];
    String[] testGenusNumbers = new String[length];
    Genus[] testGenera = new Genus[length];

    int i = 0;
    for (IndexScheme scheme : IndexScheme.values()) {
      if (scheme != IndexScheme.OTHER && i < length) {
        testGenusNumbers[i] = "testGenusNumber_" + random.nextInt(65536);
        testFamilyNumbers[i] = "testFamilyNumber_" + random.nextInt(65536);
        testGenera[i] = new Genus(
          scheme, 
          testGenusName, 
          testFamilyNumbers[i], 
          testGenusNumbers[i]);
        i++;
      }
    }

    return testGenera;
  }

  private boolean containsFamily(Family[] expecteds, Family actual) {
    for (Family expected : expecteds) {
      if (expected.equals(actual)) return true;
    }

    return false;
  }

  private boolean containsGenus(Genus[] expecteds, Genus actual) {
    for (Genus expected : expecteds) {
      if (expected.equals(actual)) return true;
    }

    return false;
  }

  private static void clearSynonymTables() {
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

  private static void clearGenusTables() {
    for (IndexScheme scheme : IndexScheme.values()) {
      if (scheme != IndexScheme.OTHER) {
        String query = "DELETE FROM "
                + Genus.getGenusTableForScheme(scheme)
                + " WHERE genus_name REGEXP ? ";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
          ps.setString(1, "^testGenusName_[0-9]+$");
          ps.executeUpdate();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}