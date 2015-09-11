package server;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import exceptions.UnableToConnectToDatabaseException;
import exceptions.UnauthorisedOperationException;
import exceptions.UsernameAlreadyExistsException;
import general.Message;
import general.SessionToken;
import server.User.UserAttributes;
import server.database.Database;

/**
 * This class contains the underlying logic of the chat server.
 */
public class ChatServer
{
	public static Map<String, SessionToken> s_tokenMap = new HashMap<String, SessionToken>();

	private Database m_database = new Database();

	public String createUser(String p_username, String p_password, Map<UserAttributes, String> p_attributes)
			throws Exception
	{
		if (isExistingUsername(p_username))
		{
			throw new UsernameAlreadyExistsException(p_username);
		}

		User user = new User(p_username, p_password, p_attributes);
		getDatabase().addUser(user);

		return login(p_username, p_password);
	}

	public void addImage(Image p_image) throws SQLException, UnableToConnectToDatabaseException
	{
		getDatabase().addImage(p_image.getId(), p_image.getName(), p_image.getImageData());
	}

	public byte[] getImage(String p_id, String p_name) throws Exception
	{
		return getDatabase().getImage(p_id, p_name);
	}

	/**
	 * Creates a new user and registers the user in the token map. Returns an
	 * unique token for the authenticated user.
	 * 
	 * @param p_username
	 *            Username of the user
	 * @param p_password
	 *            Password of the user
	 * @return Unique token for the authenticated user
	 * @throws Exception
	 */
	public String createUser(String p_username, String p_password) throws Exception
	{
		return createUser(p_username, p_password, new HashMap<UserAttributes, String>());
	}

	/**
	 * Checks whether an user name already exists.
	 * 
	 * @param p_username
	 *            Username of the user
	 * @return <code>true</code> if the user exists, and <code>false</code> if
	 *         otherwise
	 * @throws Exception
	 */
	public boolean isExistingUsername(String p_username) throws Exception
	{
		boolean exists = getTokenMap().get(p_username) == null ? false : true;

		if (!exists)
		{
			exists = getDatabase().isExistingUsername(p_username);
		}

		return exists;
	}

	/**
	 * getTokenMap description
	 * 
	 * @return
	 */
	private static Map<String, SessionToken> getTokenMap()
	{
		return s_tokenMap;
	}

	/**
	 * Register an user in the token map and returns an unique token for the
	 * authenticated user.
	 * 
	 * @param p_username
	 *            Username of the user
	 * @param p_password
	 *            Password of the user
	 * @return Unique token for the authenticated user
	 * @throws Exception
	 */
	public String login(String p_username, String p_password) throws Exception
	{
		String token = null;

		User user = getDatabase().getUser(p_username, p_password);

		if (user != null)
		{
			SessionToken sessionToken = new SessionToken();
			getTokenMap().putIfAbsent(p_username, sessionToken);

			token = getTokenMap().get(p_username).getId();
		}

		return token;
	}

	/**
	 * Sends a message.
	 * 
	 * @param p_senderUsername
	 *            The user name of the sender
	 * @param p_sessionToken
	 *            The unique token of the authenticated sender
	 * @param p_message
	 *            The message being sent
	 * @return <code>true</code> if the message was successfully sent, and
	 *         <code>false</code> if otherwise.
	 * @throws Exception
	 */
	public boolean sendMessage(String p_senderUsername, String p_sessionToken, Message<String> p_message)
			throws Exception
	{
		boolean success = false;
		p_message.setSender(p_senderUsername);

		if (isAuthenticatedUser(p_senderUsername, p_sessionToken))
		{
			success = getDatabase().sendMessage(p_message);
		}

		return success;
	}

	/**
	 * Checks whether the user is authenticated.
	 * 
	 * @param p_username
	 *            User name of the user
	 * @param p_sessionToken
	 *            Unique token of the authenticated user
	 * @return <code>true</code> if the user is authenticated and
	 *         <code>false</code> if otherwise.
	 */
	public static boolean isAuthenticatedUser(String p_username, String p_sessionToken)
	{
		SessionToken sessionToken = new SessionToken(p_sessionToken);

		return sessionToken.equals(getTokenMap().get(p_username));
	}

	public String getUserAttribute(String p_username, String p_sessionToken, UserAttributes p_userAttribute)
			throws Exception
	{
		String attribute = null;

		if (isAuthenticatedUser(p_username, p_sessionToken))
		{
			User user = getDatabase().getUser(p_username);
			attribute = user.getAttribute(p_userAttribute);
		}

		return attribute;
	}

	/**
	 * getDatabase description
	 * 
	 * @return
	 */
	private Database getDatabase()
	{
		return m_database;
	}

	public Set<User> getOnlineUsers()
	{
		HashSet<User> onlineUsers = new HashSet<User>();

		for (String username : getTokenMap().keySet())
		{
			try
			{
				onlineUsers.add(getDatabase().getUser(username));
			} catch (Exception e)
			{

			}
		}

		return onlineUsers;
	}

	public User getUser(String p_username) throws Exception
	{
		return getDatabase().getUser(p_username);
	}

	/**
	 * Gets a {@link LinkedList} of {@link Message} sent for the user.
	 * 
	 * @param p_username
	 *            User name of the user
	 * @param p_sessionToken
	 *            Unique token of the authenticated user
	 * @return {@link LinkedList} of {@link Message} sent to the user
	 * @throws Exception
	 */
	public LinkedList<Message<String>> getMessages(String p_username, String p_sessionToken, Date p_date) throws Exception
	{
		LinkedList<Message<String>> messages = null;

		if (isAuthenticatedUser(p_username, p_sessionToken))
		{
			messages = getDatabase().getMessages(p_username, p_date);
		}

		return messages;
	}

	/**
	 * Gets a {@link LinkedList} of {@link Message} sent by the user.
	 * 
	 * @param p_username
	 *            User name of the user
	 * @param p_sessionToken
	 *            Unique token of the authenticated user
	 * @return {@link LinkedList} of {@link Message} sent by the user.
	 * @throws Exception
	 */
	public LinkedList<Message<String>> getSentMessages(String p_username, String p_sessionToken) throws Exception
	{
		LinkedList<Message<String>> messages = null;

		if (isAuthenticatedUser(p_username, p_sessionToken))
		{
			messages = getDatabase().getSentMessages(p_username, null);
		}

		return messages;
	}

	public static ChatServer getChatServer()
	{
		return new ChatServer();
	}

	/**
	 * Logs out the user.
	 * 
	 * @param p_username
	 *            User name of the user
	 * @param p_session
	 *            Unique token of the authenticated user
	 * @throws UnauthorisedOperationException
	 */
	public void logout(String p_username, String p_session) throws UnauthorisedOperationException
	{
		SessionToken sessionToken = new SessionToken(p_session);

		if (sessionToken.equals(getTokenMap().get(p_username)))
		{
			getTokenMap().remove(p_username);
		} else
		{
			throw new UnauthorisedOperationException();
		}
	}
}
