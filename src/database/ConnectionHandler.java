package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The singleton class that creates the {@link Connection} with the 
 * `herbarium_index` database and provides the means to execute queries on it.
 */
public final class ConnectionHandler {
  private static final ConnectionHandler INSTANCE = new ConnectionHandler();
  private static Properties connectionProperties;
  private Connection connection;

  /**
   * Instantiates the connection with the database server.
   * 
   * Registers a new MySQL JDBC driver and logs into the database
   * using the credentials provided in the config.properties file.
   * The properties should be stored in the project resource root
   * directory.
   */
  private ConnectionHandler() {
    // Loads the connection authentication properties.
    try (InputStream input = new FileInputStream(
        "config.properties")) {
      connectionProperties = new Properties();
      connectionProperties.load(input);
    } catch (IOException e) {
      System.err.println("Could not load the properties file.");
      e.printStackTrace();
    }

    // Registers the MySQL JDBC Driver.
    try {
      // The new classname for MySQL Connector/J 8.0.
      Class.forName("com.mysql.cj.jdbc.Driver"); 
    } catch (ClassNotFoundException e) {
      System.err.println("Could not find MySQL Driver class.");
      e.printStackTrace();
    }

    // Sets connection parameters from Properties.
    String url = "jdbc:mysql://" 
      + connectionProperties.getProperty("hostname") + "/"
      + connectionProperties.getProperty("database");

    // Creates the database connection.
    try { 
      connection =
        DriverManager.getConnection(url,
                connectionProperties.getProperty("user"),
                connectionProperties.getProperty("password"));
    } catch (SQLException e) {
      System.err.println(
              "Could not establish a connection with the MySQL database.");
      e.printStackTrace();
    }
  }

  /**
   * Returns the static instance of the ConnectionHandler that is global
   * across the application.
   * 
   * @return the singleton ConnectionHandler instance
   */
  public static ConnectionHandler getConnectionHandler() {
    return INSTANCE;
  }

  /**
   * Returns the {@link Connection} to the `herbarium_index` database.
   * 
   * @return the singleton Connection instance
   */
  public Connection getConnection() {
    return connection;
  }

  /**
   * Closes the {@link Connection} owned by this ConnectionHandler.
   */
  public void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      System.err.println("Error when closing database connection.");
      e.printStackTrace();
    }
  }

}