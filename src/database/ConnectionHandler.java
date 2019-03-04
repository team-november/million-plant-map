package database;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * The singleton class that creates the {@link Connection} with the 
 * `herbarium_index` database and provides the means to execute queries on it.
 */
public final class ConnectionHandler {
  private static final ConnectionHandler INSTANCE = new ConnectionHandler();
  private static Properties connectionProperties;
  private Connection connection;
  private Session session;
  private final int LOCAL_PORT = 3306;
  private final int REMOTE_PORT = 3306; // Default MySQL port
  private final int SSH_PORT = 22;

  /**
   * Instantiates the connection with the database server.
   *
   * Sets up the SSH tunnel to access the remote database server,
   * as specified in config.properties file, and configures port
   * forwarding so that the application assumes that the database
   * is hosted locally on the local MySQL port.
   * 
   * Registers a new MySQL JDBC driver and logs into the database
   * using the credentials provided in the config.properties file.
   * The properties should be stored in the project root directory.
   */
  private ConnectionHandler() {
    // Loads the connection authentication properties.
    try {
      File jarFile = new File(ConnectionHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
      String inputFilePath = jarFile.getParent() + "/config.properties";
      InputStream input = new FileInputStream(new File(inputFilePath));
      connectionProperties = new Properties();
      connectionProperties.load(input);
    } catch (Exception e ) {
      System.err.println("Could not load the properties file.");
      e.printStackTrace();
      return;
    }

    // Sets up SSH tunnelling.
    final String REMOTE_HOST = connectionProperties.getProperty("remote_host");
    int assignedPort = LOCAL_PORT; // by default

    try {
      JSch jsch = new JSch();

      session = jsch.getSession(
              connectionProperties.getProperty("user"),
              REMOTE_HOST,
              SSH_PORT);
      session.setPassword(connectionProperties.getProperty("ssh_password"));

      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);

      session.connect();

      // Creates the tunnel through port forwarding.
      // Instructs Jsch to send data received from localhost:LOCAL_PORT to
      // REMOTE_HOST:REMOTE_PORT;
      // assignedPort is the port assigned by Jsch for use,
      // it may not always be the same as LOCAL_PORT.

      assignedPort = session.setPortForwardingL(LOCAL_PORT,
              "localhost", REMOTE_PORT);

    } catch (JSchException e) {
      System.err.println("Could not create the SSH tunnel");
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

    // Sets connection parameters.
    String url = "jdbc:mysql://localhost:"
      + assignedPort + "/"
      + connectionProperties.getProperty("database")
      + "?autoReconnect=true"
      + "&useSSL=false";

    // Creates the database connection.
    try { 
      connection =
        DriverManager.getConnection(url,
                connectionProperties.getProperty("user"),
                connectionProperties.getProperty("db_password"));
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
      if (session != null && session.isConnected()) {
        session.delPortForwardingL(LOCAL_PORT);
        session.disconnect();
      }
      if(connection != null){
        connection.close();
      }
    } catch (SQLException e) {
      System.err.println("Error when closing database connection.");
      e.printStackTrace();
    } catch (JSchException e) {
      System.err.println("Error when closing the SSH tunnel.");
      e.printStackTrace();
    }
  }

}