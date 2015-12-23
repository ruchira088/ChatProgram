package exceptions;

/**
 * Exception when the server is unable to connect to the database
 * 
 */
public class UnableToConnectToDatabaseException extends Exception 
{
	/** Serial version UID */
	private static final long serialVersionUID = 1703300439067522197L;

	public UnableToConnectToDatabaseException(String p_dbUrl) {
		super("Unable to establish connection with " + p_dbUrl);
	}
}
