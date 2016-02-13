package exceptions;

/**
 * The exception when the username already exists.
 */
public class UsernameAlreadyExistsException extends Exception
{
	/** Serial version UID */
	private static final long serialVersionUID = 8738350848929457576L;

    /**
     * Constructor
     *
     * @param p_username 
     * 	The username
     */
	public UsernameAlreadyExistsException(String p_username)
   {
      super(p_username + " already exists.");
   }
}

