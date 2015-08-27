package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

/**
 * 
 */
public class DatabaseManager
{
   public static void initializeTables()
   {
      DatabaseTable[] databaseTables = {new UsersTable(), new MessagesTable(), new ImagesTable()};
      
      for (DatabaseTable databaseTable : databaseTables)
      {
         try
         {
            if(!hasTable(databaseTable.getName()))
            {
               createTable(databaseTable.getCreationStatement());
            }
         }
         catch (Exception e)
         {
            
         }
      }      
   }
   
   private static Connection getConnection() throws ClassNotFoundException, SQLException
   {
      // Register the DB driver class
      Class.forName(Database.DB_DRIVER);

      // Get a connection from the database
      return DriverManager.getConnection(Database.DB_URL, Database.DB_USERNAME, Database.DB_PASSWORD);
   }
   
   private static void createTable(String p_statement) throws Exception
   {
      PreparedStatement prepareStatement = getConnection().prepareStatement(p_statement);
      prepareStatement.execute();
   }

   private static boolean hasTable(String p_tableName) throws Exception
   {
      String query = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ''CHAT'' AND TABLE_NAME = ''{0}''";
      PreparedStatement prepareStatement = getConnection().prepareStatement(MessageFormat.format(query, new Object[] {p_tableName}));
      ResultSet resultSet = prepareStatement.executeQuery();

      return resultSet.next();
   }

   private interface DatabaseTable
   {
      public String getCreationStatement();

      public String getName();
   }

   private static class UsersTable implements DatabaseTable
   {
      /**
       * @see server.database.Database.DatabaseTable#createDatabaseTable()
       */
      @Override
      public String getCreationStatement()
      {
         String statement = "CREATE TABLE `chat`.`USERS_TABLE` ( `COUNT` INT NOT NULL AUTO_INCREMENT , `USERNAME` VARCHAR(200) NOT NULL"
            + " , `PASSWORD` VARCHAR(200) NOT NULL , `ATTRIBUTES` BLOB NOT NULL , PRIMARY KEY (`COUNT`) ) ENGINE = InnoDB;";

         return statement;
      }

      /**
       * @see server.database.Database.DatabaseTable#getName()
       */
      @Override
      public String getName()
      {
         return "USERS_TABLE";
      }

   }

   private static class MessagesTable implements DatabaseTable
   {
      /**
       * @see server.database.Database.DatabaseTable#getCreationStatement()
       */
      @Override
      public String getCreationStatement()
      {
         String statement = "CREATE TABLE `chat`.`MESSAGES_TABLE` ( `COUNT` INT NOT NULL AUTO_INCREMENT , `TIMESTAMP` TIMESTAMP NOT NULL ,"
            + " `SENDER` VARCHAR(200) NOT NULL , `RECEIVER` VARCHAR(200) NOT NULL , `MESSAGE` TEXT NOT NULL , PRIMARY KEY (`COUNT`) ) ENGINE = InnoDB;";

         return statement;
      }

      /**
       * @see server.database.Database.DatabaseTable#getName()
       */
      @Override
      public String getName()
      {
         // TODO Auto-generated method stub
         return "MESSAGES_TABLE";
      }
   }

   private static class ImagesTable implements DatabaseTable
   {

      /**
       * @see server.database.DatabaseManager.DatabaseTable#getCreationStatement()
       */
      @Override
      public String getCreationStatement()
      {
         String statement = "CREATE TABLE `chat`.`IMAGES_TABLE` ( `COUNT` INT NOT NULL AUTO_INCREMENT , `ID` VARCHAR(200) NOT NULL , `NAME` "
            + "VARCHAR(200) NOT NULL , `DATA` MEDIUMBLOB NOT NULL , PRIMARY KEY (`COUNT`) ) ENGINE = InnoDB;";
         
         return statement;
      }

      /**
       * @see server.database.DatabaseManager.DatabaseTable#getName()
       */
      @Override
      public String getName()
      {
         // TODO Auto-generated method stub
         return "IMAGES_TABLE";
      }

   }

}
