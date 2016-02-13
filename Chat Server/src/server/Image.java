package server;

/**
 * This class represents an image.
 */
public class Image
{
	/** The image data */
	private byte[] m_imageData;

	/** The ID of the image */
	private String m_id;

	/** The name of the image */
	private String m_name;

	/**
	 * Gets the image data.
	 * 
	 * @return The image data
	 */
	public byte[] getImageData()
	{
		return m_imageData;
	}

	/**
	 * Gets the ID of the image.
	 * 
	 * @return The id of the image
	 */
	public String getId()
	{
		return m_id;
	}

	/**
	 * Gets the path of the image.
	 * 
	 * @return The path of the image
	 */
	public String getImagePath()
	{
		return m_id + "/" + m_name;
	}

	/**
	 * Gets the name of the image.
	 * 
	 * @return The name of the image
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * Constructor
	 * 
	 * @param p_name
	 *            Name of the image
	 * @param p_id
	 *            ID of the image
	 * @param p_imageData
	 *            The image data
	 */
	public Image(String p_name, String p_id, byte[] p_imageData)
	{
		m_name = p_name;
		m_id = p_id;
		m_imageData = p_imageData;
	}
}
