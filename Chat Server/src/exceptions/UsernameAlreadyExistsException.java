package exceptions;

public class UsernameAlreadyExistsException extends Exception
{
	/** Serial version UID */
	private static final long serialVersionUID = 8738350848929457576L;

	public UsernameAlreadyExistsException(String p_username)
   {
      super(p_username + " already exists.");
   }
}

