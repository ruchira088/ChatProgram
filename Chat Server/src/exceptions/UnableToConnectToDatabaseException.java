package exceptions;

public class UnableToConnectToDatabaseException extends Exception
{
   public UnableToConnectToDatabaseException(String p_dbUrl)
   {
      super("Unable to establish connection with " + p_dbUrl);
   }
}

