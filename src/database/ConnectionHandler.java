package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionHandler {
  private static final ConnectionHandler INSTANCE = new ConnectionHandler();
  private static Properties connectionProperties = new Properties();
  private Connection connection;

  private ConnectionHandler() {
    // Loads the connection authentication properties.
    try (InputStream input = new FileInputStream(
        "config.properties")) {
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
      + connectionProperties.getProperty("database")
      // + "?verifyServerCertificate=false"
      + "&useSSL=true"
      + "&requireSSL=true";

    // Creates the database connection.
    try { 
      connection =
        DriverManager.getConnection(url,
                                  connectionProperties.getProperty("user"), 
                                  connectionProperties.getProperty("password"));
    } catch (SQLException e) {
      System.err.println("Could not establish connection with MySQL database.");
      e.printStackTrace();
    }
  }

  public static ConnectionHandler getConnectionHandler() {
    return INSTANCE;
  }

  public Connection getConnection() {
    return connection;
  }

  public void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      System.err.println("Error when closing database connection.");
      e.printStackTrace();
    }
  }

}