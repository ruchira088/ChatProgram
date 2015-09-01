package server.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map;

import exceptions.UnableToConnectToDatabaseException;
import general.Message;
import server.User;
import server.User.UserAttributes;

/**
 * 
 */
public class Database
{
   /** Database driver class*/
   static final String DB_DRIVER = "com.mysql.jdbc.Driver";

   /** Database URL */
   static final String DB_URL = "jdbc:mysql://localhost:3306/chat";

   /** Database Credentials */
   static final String DB_USERNAME = "root";

   static final String DB_PASSWORD = "";

   /** The connection to the database */
   private Connection m_connection;

   /**
    * If the database tables don't exist, create the tables.
    */
   static
   {
      DatabaseManager.initializeTables();
   }

   /** 
    * Columns of the "USER_TABLE" which holds data about the users.
    */
   public enum UserTableColumns {
      USERNAME, PASSWORD, ATTRIBUTES;
   }

   /**
    * Columns of the "MESSAGES_TABLE" which holds data about the messages.
    */
   public enum MessageTableColumns {
      TIMESTAMP, SENDER, RECEIVER, MESSAGE;
   }

   /**
    * Gets a connection to the database.
    * 
    * @return
    *   Connection to the database.
    * @throws UnableToConnectToDatabaseException
    */
   private Connection getConnection() throws UnableToConnectToDatabaseException
   {
      if (m_connection == null)
      {
         try
         {
            // Register the DB driver class
            Class.forName(DB_DRIVER);

            // Get a connection from the database
            m_connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
         }
         catch (Exception exception)
         {
            throw new UnableToConnectToDatabaseException(DB_URL);
         }
      }

      return m_connection;
   }

   /**
    * Adds an user to the database.
    * 
    * @param p_user
    *   The {@link User}
    * @throws Exception
    */
   public void addUser(User p_user) throws Exception
   {
      PreparedStatement prepareStatement = getConnection()
         .prepareStatement("INSERT INTO USERS_TABLE (USERNAME, PASSWORD, ATTRIBUTES) VALUES (?, ?, ?);");

      prepareStatement.setString(1, p_user.getUsername());
      prepareStatement.setString(2, p_user.getPassword());

      prepareStatement.setBlob(3, getInputStreamFromObject(p_user.getAttributes()));

      prepareStatement.execute();
      prepareStatement.close();
   }

   /**
    * Gets an user from the database.
    * 
    * @param p_username
    *   Username of the {@link User}
    * @param p_password
    *   Password of the {@link User}
    * @return
    *   The {@link User} which has the corresponding username and password
    * @throws Exception
    */
   public User getUser(String p_username, String p_password) throws Exception
   {
      PreparedStatement prepareStatement = getConnection()
         .prepareStatement("SELECT * FROM USERS_TABLE WHERE USERNAME = ? AND PASSWORD = ?");
      prepareStatement.setString(1, p_username);
      prepareStatement.setString(2, p_password);
      ResultSet resultSet = prepareStatement.executeQuery();

      return covertResultSetToUser(resultSet);
   }

   /**
    * Gets an user from the database
    * 
    * @param p_username
    *   Username of the {@link User}
    * @return
    *   The {@link User} which has the corresponding username
    * @throws Exception
    */
   public User getUser(String p_username) throws Exception
   {
      PreparedStatement prepareStatement = getConnection().prepareStatement("SELECT * FROM USERS_TABLE WHERE USERNAME = ?");
      prepareStatement.setString(1, p_username);
      ResultSet resultSet = prepareStatement.executeQuery();

      return covertResultSetToUser(resultSet);
   }

   public void addImage(String p_id, String p_name, byte[] p_image) throws SQLException, UnableToConnectToDatabaseException
   {
      try
      {
         PreparedStatement preparedStatement = getConnection()
            .prepareStatement("INSERT INTO IMAGES_TABLE (ID, NAME, DATA) VALUES (?, ?, ?)");
         preparedStatement.setString(1, p_id);
         preparedStatement.setString(2, p_name);
         preparedStatement.setBlob(3, new ByteArrayInputStream(p_image));
         preparedStatement.execute();
      }
      catch (Exception exception)
      {

      }
   }

   public byte[] getImage(String p_id, String p_name) throws Exception
   {
      PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT DATA FROM IMAGES_TABLE WHERE ID = ? AND NAME = ?");
      preparedStatement.setString(1, p_id);
      preparedStatement.setString(2, p_name);
      ResultSet resultSet = preparedStatement.executeQuery();

      byte[] image = null;

      if (resultSet.next())
      {
         Blob blob = resultSet.getBlob("DATA");
         image = blob.getBytes(1, (int) blob.length());

      }

      return image;

   }

   private User covertResultSetToUser(ResultSet p_resultSet) throws Exception
   {
      User user = null;

      if (p_resultSet.next())
      {
         String username = p_resultSet.getString(UserTableColumns.USERNAME.toString());
         Blob attributesBlob = p_resultSet.getBlob(UserTableColumns.ATTRIBUTES.toString());

         Map<UserAttributes, String> attributes = null;
         if (attributesBlob != null)
         {
            attributes = (Map<UserAttributes, String>) new ObjectInputStream(attributesBlob.getBinaryStream()).readObject();
         }

         user = new User(username, attributes);
      }

      return user;
   }

   /**
    * Checks if an username already exists in the database.
    * 
    * @param p_username
    *   The username
    * @return
    *   <code>true</code> if the username exists in the database, and <code>false</code> if otherwise.
    * @throws SQLException
    * @throws UnableToConnectToDatabaseException
    */
   public boolean isExistingUsername(String p_username) throws SQLException, UnableToConnectToDatabaseException
   {
      boolean userExists = false;

      PreparedStatement prepareStatement = getConnection().prepareStatement("SELECT USERNAME FROM USERS_TABLE WHERE USERNAME = ?");
      prepareStatement.setString(1, p_username);
      ResultSet resultSet = prepareStatement.executeQuery();

      if (resultSet.next())
      {
         userExists = true;
      }

      return userExists;
   }

   public boolean sendMessage(Message<String> p_message)
   {
       boolean isSuccess = false;
       try
       {
	   PreparedStatement preparedStatement = getConnection()
		   .prepareStatement("INSERT INTO MESSAGES_TABLE (TIMESTAMP, SENDER, RECEIVER, MESSAGE) VALUES (?, ?, ?, ?)");
	   preparedStatement.setTimestamp(1, p_message.getSentTimeStamp());
	   preparedStatement.setString(2, p_message.getSender());
	   preparedStatement.setString(3, p_message.getReceiver());
	   preparedStatement.setString(4, p_message.getMessageContents());
	   
	   preparedStatement.execute();
	   preparedStatement.close();	
	   isSuccess = true;
       }
       catch (Exception exception)
       {
	   
       }
       
       return isSuccess;

   }

   public LinkedList<Message<String>> getMessages(String p_receiver, Timestamp p_timestamp) throws Exception
   {
      PreparedStatement prepareStatement = getConnection().prepareStatement("SELECT * FROM MESSAGES_TABLE WHERE RECEIVER = ?");
      prepareStatement.setString(1, p_receiver);
      ResultSet resultSet = prepareStatement.executeQuery();

      return convertResultSetToMessageList(resultSet);

   }

   public LinkedList<Message<String>> getSentMessages(String p_sender, Timestamp p_timestamp) throws Exception
   {
      PreparedStatement prepareStatement = getConnection().prepareStatement("SELECT * FROM MESSAGES_TABLE WHERE SENDER = ?");
      prepareStatement.setString(1, p_sender);
      ResultSet resultSet = prepareStatement.executeQuery();

      return convertResultSetToMessageList(resultSet);

   }

   /**
    * A helper method which converts a {@link ResultSet} of {@link Message}s into a {@link LinkedList} of {@link Message}s.
    * 
    * @param p_resultSet
    *   The {@link ResultSet} of {@link Message}s
    * @return
    *   The {@link LinkedList} of {@link Message}s
    * @throws Exception
    */
   private LinkedList<Message<String>> convertResultSetToMessageList(ResultSet p_resultSet) throws Exception
   {
      LinkedList<Message<String>> messages = new LinkedList<Message<String>>();

      while (p_resultSet.next())
      {
         Timestamp timestamp = p_resultSet.getTimestamp(MessageTableColumns.TIMESTAMP.toString());
         String sender = p_resultSet.getString(MessageTableColumns.SENDER.toString());
         String receiver = p_resultSet.getString(MessageTableColumns.RECEIVER.toString());
         String messageContents = p_resultSet.getString(MessageTableColumns.MESSAGE.toString());

         Message<String> message = new Message<String>(messageContents, timestamp, receiver);
         message.setSender(sender);

         messages.add(message);
      }

      return messages;
   }

   /**
    * Gets an {@link InputStream} from an {@link Object}.
    * 
    * @param p_object
    *   The {@link Object}
    * @return
    *   The {@link InputStream}
    * @throws IOException
    */
   private InputStream getInputStreamFromObject(Object p_object) throws IOException
   {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(p_object);
      objectOutputStream.flush();
      byteArrayOutputStream.flush();
      objectOutputStream.close();
      byteArrayOutputStream.close();

      return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
   }
}
