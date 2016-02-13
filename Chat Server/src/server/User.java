package server;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a user.
 */
public class User implements Comparable<User>
{
	/** The username */
	private String m_username;

	/** The password */
	private String m_password;

	/** User attributes */
	private Map<UserAttributes, String> m_attributes;

	/**
	 * User attributes
	 */
	public enum UserAttributes
	{
		NAME, GENDER, PROFILE_PICTURE_PATH;
	}

	/**
	 * Constructor
	 * 
	 * @param p_username
	 *            The username of the user
	 * @param p_password
	 *            The password of the user
	 */
	public User(String p_username, String p_password)
	{
		this(p_username, p_password, new HashMap<UserAttributes, String>());
	}

	/**
	 * Constructor
	 * 
	 * @param p_username
	 *            The username of the user
	 * @param p_attributes
	 *            The attributes of the user
	 */
	public User(String p_username, Map<UserAttributes, String> p_attributes)
	{
		this(p_username, null, p_attributes);
	}

	/**
	 * Constructor
	 * 
	 * @param p_username
	 *            The username of the user
	 * @param p_password
	 *            The password of the user
	 * @param p_attributes
	 *            The attributes of the user
	 */
	public User(String p_username, String p_password, Map<UserAttributes, String> p_attributes)
	{
		setUsername(p_username);
		setPassword(p_password);
		setAttributes(p_attributes);
	}

	/**
	 * Sets user attributes
	 * 
	 * @param p_attributes
	 *            The attributes to set
	 */
	public void setAttributes(Map<UserAttributes, String> p_attributes)
	{
		m_attributes = p_attributes;
	}

	/**
	 * Gets the attributes of the user.
	 * 
	 * @return The attributes of the user
	 */
	public Map<UserAttributes, String> getAttributes()
	{
		return m_attributes;
	}

	/**
	 * Sets an attribute of the user.
	 * 
	 * @param p_attribute
	 *            The attribute to set.
	 * @param p_value
	 *            The value of the attribute
	 */
	public void setAttribute(UserAttributes p_attribute, String p_value)
	{
		m_attributes.put(p_attribute, p_value);
	}

	/**
	 * Gets an attribute of the user.
	 * 
	 * @param p_userAttribute
	 *            The attribute to get.
	 * 
	 * @return The value of the attribute
	 */
	public String getAttribute(UserAttributes p_userAttribute)
	{
		String attribute = null;

		if (getAttributes() != null)
		{
			attribute = getAttributes().get(p_userAttribute);
		}
		return attribute;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername()
	{
		return m_username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param p_username
	 *            The username to set
	 */
	public void setUsername(String p_username)
	{
		m_username = p_username;
	}

	/**
	 * Gets the password.
	 * 
	 * @return The password
	 */
	public String getPassword()
	{
		return m_password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param p_password
	 *            The password to set
	 */
	public void setPassword(String p_password)
	{
		m_password = p_password;
	}

	/**
	 * Gets the name of the user.
	 * 
	 * @return The name of the user
	 */
	public String getName()
	{
		String name;

		if (getAttributes() != null && getAttributes().containsKey(UserAttributes.NAME))
		{
			name = getAttributes().get(UserAttributes.NAME);
		} else
		{
			name = getUsername();
		}
		return name;
	}

	/**
	 * Sets the name of the user
	 * 
	 * @param p_name
	 *            The name to set
	 */
	public void setName(String p_name)
	{
		getAttributes().put(UserAttributes.NAME, p_name);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(User p_user)
	{
		return getName().compareToIgnoreCase(p_user.getName());
	}

}
