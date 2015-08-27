package exceptions;

public class UsernameAlreadyExistsException extends Exception
{
   public UsernameAlreadyExistsException(String p_username)
   {
      super(p_username + " already exists.");
   }
}

