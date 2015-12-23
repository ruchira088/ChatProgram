package general;

public class SqlQueries
{
   private static final String USER_TABLE = "userTable";
   
   private static final String MESSAGE_TABLE = "messages";
   
   public static final String INSERT_USER = "INSERT INTO " + USER_TABLE + " (username, password) VALUES (''{0}'', ''{1}'');";
   
   public static final String INSERT_USER_1 = "INSERT INTO USER_TABLE (NAME, USERNAME, PASSWORD, ATTRIBUTES, BYTE_ATTRIBUTES) VALUES (?, ?, ?, ?, ?)";
   
   public static final String GET_USER = "SELECT * FROM " + USER_TABLE + " WHERE username=''{0}'' AND password=''{1}'';";
   
   public static final String USERNAME_EXISTS = "SELECT * FROM " + USER_TABLE + " WHERE username=''{0}'';";
   
   public static final String GET_MESSAGES = "SELECT * FROM " + MESSAGE_TABLE + " WHERE RECEIVER=''{0}'' AND DATE > Now() - INTERVAL 1 DAY;";
   
   public static final String GET_SENT_MESSAGES = "SELECT * FROM " + MESSAGE_TABLE + " WHERE SENDER=''{0}'' AND DATE > Now() - INTERVAL 1 DAY;";
   
}

