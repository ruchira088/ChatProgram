package client;
/**
 * 
 */
public class StringWriter
{
   private StringBuilder m_stringBuilder = new StringBuilder();
   
   public void append(String p_string)
   {
      m_stringBuilder.append(p_string);
      m_stringBuilder.append("\n");
   }
   
   public String toString()
   {
      return m_stringBuilder.toString();
   }

}

