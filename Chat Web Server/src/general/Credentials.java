package general;

public class Credentials
{
   private String m_username;
   
   private String m_token;
   
   
   public Credentials(String p_username, String p_token)
   {
      m_username = p_username;
      m_token = p_token;
   }


   /**
    * Get username
    * @return the username
    */
   public String getUsername()
   {
      return m_username;
   }


   /**
    * Get token
    * @return the token
    */
   public String getToken()
   {
      return m_token;
   }
}

