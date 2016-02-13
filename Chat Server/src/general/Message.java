package general;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a message.
 */
public class Message<T> implements Serializable
{
	/** Serial Version UID */
	private static final long serialVersionUID = 8456390073132426548L;

	/** Contents of the message */
	private T m_messageContents;

	/** Timestamp of the message */
	private Timestamp m_sentTimeStamp;

	/** Sender */
	private String m_sender;

	/** Receiver */
	private String m_receiver;

	/**
	 * Constructor
	 * 
	 * @param p_messageContents
	 *            Message contents
	 * @param p_sentDate
	 *            Sent date
	 * @param p_receiver
	 *            Receiver for the message
	 */
	public Message(T p_messageContents, Date p_sentDate, String p_receiver)
	{
		this(p_messageContents, new Timestamp(p_sentDate.getTime()), p_receiver);
	}

	/**
	 * Constructor
	 * 
	 * @param p_messageContents
	 *            Message contents
	 * @param p_timestamp
	 *            {@link Timestamp} of the sent date
	 * @param p_receiver
	 *            Receiver for the message
	 */
	public Message(T p_messageContents, Timestamp p_timestamp, String p_receiver)
	{
		m_messageContents = p_messageContents;
		m_receiver = p_receiver;
		m_sentTimeStamp = p_timestamp;
		;
	}

	/**
	 * Gets the message contents.
	 * 
	 * @return The message contents
	 */
	public T getMessageContents()
	{
		return m_messageContents;
	}

	/**
	 * Gets the sent date.
	 * 
	 * @return The sent date
	 */
	public Timestamp getSentTimeStamp()
	{
		return m_sentTimeStamp;
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public String getSender()
	{
		return m_sender;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param p_sender
	 *            The sender
	 */
	public void setSender(String p_sender)
	{
		m_sender = p_sender;
	}

	/**
	 * Gets the receiver.
	 * 
	 * @return The receiver
	 */
	public String getReceiver()
	{
		return m_receiver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_messageContents == null) ? 0 : m_messageContents.hashCode());
		result = prime * result + ((m_receiver == null) ? 0 : m_receiver.hashCode());
		result = prime * result + ((m_sender == null) ? 0 : m_sender.hashCode());
		result = prime * result + ((m_sentTimeStamp == null) ? 0 : m_sentTimeStamp.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message<?> other = (Message<?>) obj;
		if (m_messageContents == null)
		{
			if (other.m_messageContents != null)
				return false;
		} else if (!m_messageContents.equals(other.m_messageContents))
			return false;
		if (m_receiver == null)
		{
			if (other.m_receiver != null)
				return false;
		} else if (!m_receiver.equals(other.m_receiver))
			return false;
		if (m_sender == null)
		{
			if (other.m_sender != null)
				return false;
		} else if (!m_sender.equals(other.m_sender))
			return false;
		if (m_sentTimeStamp == null)
		{
			if (other.m_sentTimeStamp != null)
				return false;
		} else if (!m_sentTimeStamp.equals(other.m_sentTimeStamp))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "{\"messageContents\" : \"" + m_messageContents + "\", \"time\" : "
				+ getJsonFromTimeStamp(m_sentTimeStamp) + ", \"sender\" : \"" + m_sender + "\", \"receiver\" : \""
				+ m_receiver + "\"}";
	}

	/**
	 * Converts the passed-in {@link Timestamp} to a {@link String} representing
	 * a JSON object.
	 * 
	 * @param p_timestamp
	 *            {@link Timestamp} object
	 * @return JSON representation of the {@link Timestamp}
	 */
	private String getJsonFromTimeStamp(Timestamp p_timestamp)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(p_timestamp.getTime());

		int year = calendar.get(Calendar.YEAR);
		String month = new SimpleDateFormat("MMM").format(p_timestamp);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);

		String Json = "{ \"Year\" : \"" + year + "\", \"Month\" : \"" + month + "\", \"Day\" : \"" + day
				+ "\", \"Hour\" : \"" + hour + "\", \"Minutes\" :\"" + minutes + "\", \"Seconds\" : \"" + seconds
				+ "\"}";

		return Json;
	}
}
