package server;
/**
 * 
 */
public class Image
{
   private byte[] m_imageData;
   
   private String m_id;
   
   private String m_name;

   /**
    * Get imageData
    * @return the imageData
    */
   public byte[] getImageData()
   {
      return m_imageData;
   }

   /**
    * Get id
    * @return the id
    */
   public String getId()
   {
      return m_id;
   }
   
   public String getImagePath()
   {
      return m_id + "/" + m_name;
   }
   

   /**
    * Get name
    * @return the name
    */
   public String getName()
   {
      return m_name;
   }
   
   public Image(String p_name, String p_id, byte[] p_imageData)
   {
      m_name = p_name;
      m_id = p_id;
      m_imageData = p_imageData;
   }
}

