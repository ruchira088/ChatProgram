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
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import exceptions.UnableToConnectToDatabaseException;
import general.Message;
import server.User;
import server.User.UserAttributes;
import server.database.DatabaseQuery.Condition;
import server.database.DatabaseQuery.Table;
import server.database.DatabaseQuery.Type;

/**
 * This class represents the database.
 */
public class Database
{
	/** Database driver class */
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
	 * The interface specifying an attribute which can be searched in the
	 * database.
	 */
	public interface SearchableAttribute
	{
		/**
		 * Gets the attribute name
		 * 
		 * @return The attribute name
		 */
		public String getAttribute();

		/**
		 * Checks whether the attribute value is a {@link String} value.
		 * 
		 * @return {@code true} if the attribute value is a {@link String}, and
		 *         {@code false} if otherwise.
		 */
		public boolean isStringValue();
	}

	/**
	 * Columns of the "USER_TABLE" which holds data about the users.
	 */
	public enum UserTableColumns implements SearchableAttribute
	{
		USERNAME, PASSWORD, ATTRIBUTES;

		/** Indicates whether the value is a {@link String} value */
		private boolean m_isStringValue;

		/**
		 * Constructor
		 */
		private UserTableColumns()
		{
			this(true);
		}

		/**
		 * Constructor
		 * 
		 * @param p_isStringValue
		 *            Indicates whether the value is a {@link String} value
		 */
		private UserTableColumns(boolean p_isStringValue)
		{
			m_isStringValue = p_isStringValue;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getAttribute()
		{
			return this.name();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStringValue()
		{
			return m_isStringValue;
		}
	}

	/**
	 * Columns of the "MESSAGES_TABLE" which holds data about the messages.
	 */
	public enum MessageTableColumns implements SearchableAttribute
	{
		TIMESTAMP(false), SENDER, RECEIVER, MESSAGE;

		/** Indicates whether the value is a {@link String} value */
		private boolean m_isStringValue;

		/**
		 * Constructor
		 */
		private MessageTableColumns()
		{
			this(true);
		}

		/**
		 * Constructor
		 * 
		 * @param p_isStringValue
		 *            Indicates whether the value is a {@link String} value
		 */
		private MessageTableColumns(boolean p_isStringValue)
		{
			m_isStringValue = p_isStringValue;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getAttribute()
		{
			return this.name();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStringValue()
		{
			return m_isStringValue;
		}
	}

	/**
	 * Gets a connection to the database.
	 * 
	 * @return Connection to the database.
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
			} catch (Exception exception)
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
	 *            The {@link User}
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
	 *            Username of the {@link User}
	 * @param p_password
	 *            Password of the {@link User}
	 * @return The {@link User} which has the corresponding username and
	 *         password
	 * @throws Exception
	 */
	public User getUser(String p_username, String p_password) throws Exception
	{
		PreparedStatement prepareStatement = getConnection()
				.prepareStatement("SELECT * FROM USERS_TABLE WHERE USERNAME = ? AND PASSWORD = ?");
		prepareStatement.setString(1, p_username);
		prepareStatement.setString(2, p_password);
		ResultSet resultSet = prepareStatement.executeQuery();

		return transformResultSetToUser(resultSet);
	}

	/**
	 * Gets an user from the database
	 * 
	 * @param p_username
	 *            Username of the {@link User}
	 * @return The {@link User} which has the corresponding username
	 * @throws Exception
	 */
	public User getUser(String p_username) throws Exception
	{
		PreparedStatement prepareStatement = getConnection()
				.prepareStatement("SELECT * FROM USERS_TABLE WHERE USERNAME = ?");
		prepareStatement.setString(1, p_username);
		ResultSet resultSet = prepareStatement.executeQuery();

		return transformResultSetToUser(resultSet);
	}

	/**
	 * Adds an image into the database.
	 * 
	 * @param p_id
	 *            ID of the image
	 * @param p_name
	 *            Name of the image
	 * @param p_image
	 *            Byte array containing the image
	 * @throws SQLException
	 * @throws UnableToConnectToDatabaseException
	 */
	public void addImage(String p_id, String p_name, byte[] p_image)
			throws SQLException, UnableToConnectToDatabaseException
	{
		try
		{
			PreparedStatement preparedStatement = getConnection()
					.prepareStatement("INSERT INTO IMAGES_TABLE (ID, NAME, DATA) VALUES (?, ?, ?)");
			preparedStatement.setString(1, p_id);
			preparedStatement.setString(2, p_name);
			preparedStatement.setBlob(3, new ByteArrayInputStream(p_image));
			preparedStatement.execute();
		} catch (Exception exception)
		{

		}
	}

	/**
	 * Gets an image from the database.
	 * 
	 * @param p_id
	 *            ID of the image
	 * @param p_name
	 *            Name of the image
	 * @return Byte array containing the image
	 * @throws Exception
	 */
	public byte[] getImage(String p_id, String p_name) throws Exception
	{
		PreparedStatement preparedStatement = getConnection()
				.prepareStatement("SELECT DATA FROM IMAGES_TABLE WHERE ID = ? AND NAME = ?");
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

	/**
	 * Transforms the result set into a {@link User} object.
	 * 
	 * @param p_resultSet
	 *            The result set
	 * @return The transformed {@link User} object
	 * @throws Exception
	 */
	private User transformResultSetToUser(ResultSet p_resultSet) throws Exception
	{
		User user = null;

		if (p_resultSet.next())
		{
			String username = p_resultSet.getString(UserTableColumns.USERNAME.toString());
			Blob attributesBlob = p_resultSet.getBlob(UserTableColumns.ATTRIBUTES.toString());

			Map<UserAttributes, String> attributes = null;
			if (attributesBlob != null)
			{
				attributes = (Map<UserAttributes, String>) new ObjectInputStream(attributesBlob.getBinaryStream())
						.readObject();
			}

			user = new User(username, attributes);
		}

		return user;
	}

	/**
	 * Checks if an username already exists in the database.
	 * 
	 * @param p_username
	 *            The username
	 * @return <code>true</code> if the username exists in the database, and
	 *         <code>false</code> if otherwise.
	 * @throws SQLException
	 * @throws UnableToConnectToDatabaseException
	 */
	public boolean isExistingUsername(String p_username) throws SQLException, UnableToConnectToDatabaseException
	{
		boolean userExists = false;

		PreparedStatement prepareStatement = getConnection()
				.prepareStatement("SELECT USERNAME FROM USERS_TABLE WHERE USERNAME = ?");
		prepareStatement.setString(1, p_username);
		ResultSet resultSet = prepareStatement.executeQuery();

		if (resultSet.next())
		{
			userExists = true;
		}

		return userExists;
	}

	/**
	 * Adds a message into the message table
	 * 
	 * @param p_message
	 *            The message
	 * 
	 * @return {@code true} if the message was successfully added, and
	 *         {@code false} if otherwise.
	 */
	public boolean addMessage(Message<String> p_message)
	{
		boolean isSuccess = false;
		try
		{
			PreparedStatement preparedStatement = getConnection().prepareStatement(
					"INSERT INTO MESSAGES_TABLE (TIMESTAMP, SENDER, RECEIVER, MESSAGE) VALUES (?, ?, ?, ?)");
			preparedStatement.setTimestamp(1, p_message.getSentTimeStamp());
			preparedStatement.setString(2, p_message.getSender());
			preparedStatement.setString(3, p_message.getReceiver());
			preparedStatement.setString(4, p_message.getMessageContents());

			preparedStatement.execute();
			preparedStatement.close();
			isSuccess = true;
		} catch (Exception exception)
		{

		}

		return isSuccess;

	}

	/**
	 * Fetches exchanged messages between 2 users after the passed-in date.
	 * 
	 * @param p_user_1
	 *            Username of user 1
	 * @param p_user_2
	 *            Username of user 2
	 * @param p_date
	 *            The date
	 * @return A {@link LinkedList} of messages exchanged between the 2 users
	 *         ordered by sent time.
	 * @throws Exception
	 */
	public LinkedList<Message<String>> getIncomingAndOutgoingMessages(String p_user_1, String p_user_2, Date p_date)
			throws Exception
	{
		DatabaseQuery databaseQuery_1 = DatabaseQueryCreator.setTable(Table.MESSAGES).setType(Type.SELECT).where()
				.setSelector(MessageTableColumns.RECEIVER, Condition.EQUALS, p_user_1).And()
				.setSelector(MessageTableColumns.TIMESTAMP, Condition.GREATER_THAN, p_date).And()
				.setSelector(MessageTableColumns.SENDER, Condition.EQUALS, p_user_2).getQuery();

		DatabaseQuery databaseQuery_2 = DatabaseQueryCreator.setTable(Table.MESSAGES).setType(Type.SELECT).where()
				.setSelector(MessageTableColumns.RECEIVER, Condition.EQUALS, p_user_2).And()
				.setSelector(MessageTableColumns.TIMESTAMP, Condition.GREATER_THAN, p_date).And()
				.setSelector(MessageTableColumns.SENDER, Condition.EQUALS, p_user_1).getQuery();

		String query = databaseQuery_1 + " UNION " + databaseQuery_2 + " ORDER BY "
				+ MessageTableColumns.TIMESTAMP.name();

		PreparedStatement prepareStatement = getConnection().prepareStatement(query);

		if (p_date != null)
		{
			Timestamp timestamp = new Timestamp(p_date.getTime());
			prepareStatement.setTimestamp(1, timestamp);
			prepareStatement.setTimestamp(2, timestamp);
		}

		ResultSet resultSet = prepareStatement.executeQuery();

		return convertResultSetToMessageList(resultSet);

	}

	/**
	 * Gets incoming messages for the receiver from sender after the passed-in
	 * date.
	 * 
	 * @param p_receiver
	 *            The username of the receiver
	 * @param p_sender
	 *            The username of the sender
	 * @param p_date
	 *            The date
	 * @return A {@link LinkedList} of messages sent by the sender to the
	 *         receiver
	 * @throws Exception
	 */
	public LinkedList<Message<String>> getIncomingMessages(String p_receiver, String p_sender, Date p_date)
			throws Exception
	{
		DatabaseQuery databaseQuery = DatabaseQueryCreator.setTable(Table.MESSAGES).setType(Type.SELECT).where()
				.setSelector(MessageTableColumns.RECEIVER, Condition.EQUALS, p_receiver).And()
				.setSelector(MessageTableColumns.TIMESTAMP, Condition.GREATER_THAN, p_date).And()
				.setSelector(MessageTableColumns.SENDER, Condition.EQUALS, p_sender).getQuery();

		PreparedStatement prepareStatement = getConnection().prepareStatement(databaseQuery.toString());

		if (p_date != null)
		{
			prepareStatement.setTimestamp(1, new Timestamp(p_date.getTime()));
		}

		ResultSet resultSet = prepareStatement.executeQuery();

		return convertResultSetToMessageList(resultSet);
	}

	/**
	 * Fetches messages sent by a user.
	 * 
	 * @param p_sender
	 *            The username
	 * @return A {@link LinkedList} of messages sent by the user
	 * @throws Exception
	 */
	public LinkedList<Message<String>> getSentMessages(String p_sender) throws Exception
	{
		PreparedStatement prepareStatement = getConnection()
				.prepareStatement("SELECT * FROM MESSAGES_TABLE WHERE SENDER = ?");
		prepareStatement.setString(1, p_sender);
		ResultSet resultSet = prepareStatement.executeQuery();

		return convertResultSetToMessageList(resultSet);

	}

	/**
	 * A helper method which converts a {@link ResultSet} of {@link Message}s
	 * into a {@link LinkedList} of {@link Message}s.
	 * 
	 * @param p_resultSet
	 *            The {@link ResultSet} of {@link Message}s
	 * @return The {@link LinkedList} of {@link Message}s
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
	 *            The {@link Object}
	 * @return The {@link InputStream}
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
