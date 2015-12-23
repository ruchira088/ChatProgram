package general;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message<T> implements Serializable
{
	/** Serial Version UID */
	private static final long serialVersionUID = 8456390073132426548L;

	private T m_messageContents;

	private Timestamp m_sentTimeStamp;

	private String m_sender;

	private String m_receiver;

	/**
	 * Get messageContents
	 * 
	 * @return the messageContents
	 */
	public T getMessageContents()
	{
		return m_messageContents;
	}

	/**
	 * Get sentDate
	 * 
	 * @return the sentDate
	 */
	public Timestamp getSentTimeStamp()
	{
		return m_sentTimeStamp;
	}

	/**
	 * Get sender
	 * 
	 * @return the sender
	 */
	public String getSender()
	{
		return m_sender;
	}

	public void setSender(String p_sender)
	{
		m_sender = p_sender;
	}

	/**
	 * Get receiver
	 * 
	 * @return the receiver
	 */
	public String getReceiver()
	{
		return m_receiver;
	}

	/**
	 * @see java.lang.Object#hashCode()
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
	 * @see java.lang.Object#equals(java.lang.Object)
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
		}
		else if (!m_messageContents.equals(other.m_messageContents))
			return false;
		if (m_receiver == null)
		{
			if (other.m_receiver != null)
				return false;
		}
		else if (!m_receiver.equals(other.m_receiver))
			return false;
		if (m_sender == null)
		{
			if (other.m_sender != null)
				return false;
		}
		else if (!m_sender.equals(other.m_sender))
			return false;
		if (m_sentTimeStamp == null)
		{
			if (other.m_sentTimeStamp != null)
				return false;
		}
		else if (!m_sentTimeStamp.equals(other.m_sentTimeStamp))
			return false;
		return true;
	}
		
	@Override
	public String toString()
	{
		return "{\"messageContents\" : \"" + m_messageContents + "\", \"time\" : " + getJsonFromTimeStamp(m_sentTimeStamp)
				+ ", \"sender\" : \"" + m_sender + "\", \"receiver\" : \"" + m_receiver + "\"}";
	}
	
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
		
		String Json = "{ \"Year\" : \"" + year + "\", \"Month\" : \"" + month + 
				"\", \"Day\" : \"" + day + "\", \"Hour\" : \"" + hour + "\", \"Minutes\" :\"" +
				minutes + "\", \"Seconds\" : \"" + seconds + "\"}";
		
		return Json;
	}

	public Message(T p_messageContents, Date p_sentDate, String p_receiver)
	{
		this(p_messageContents, new Timestamp(p_sentDate.getTime()), p_receiver);
	}

	public Message(T p_messageContents, Timestamp p_timestamp, String p_receiver)
	{
		m_messageContents = p_messageContents;
		m_receiver = p_receiver;
		m_sentTimeStamp = p_timestamp;;
	}
}
