package general;

import java.io.Serializable;
import java.util.UUID;

/** 
 * This class represents a session token.
 * 
 */
public class SessionToken implements Serializable
{
   /** Serial Version UID */
   private static final long serialVersionUID = 6664715201223933974L;
   
   /** ID of the token */
   private final String m_id;
   
   /**
    * Constructor
    * 
    * @param p_id
    * 	ID of the session
    */
   public SessionToken(String p_id)
   {
      m_id = p_id;
   }
   
   /**
    * Constructor
    */
   public SessionToken()
   {
      this(UUID.randomUUID().toString());
   }

   /**
    * Gets the id
    * 
    * @return the id
    */
   public String getId()
   {
      return m_id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
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
      SessionToken other = (SessionToken) obj;
      if (m_id == null)
      {
         if (other.m_id != null)
            return false;
      }
      else if (!m_id.equals(other.m_id))
         return false;
      return true;
   }

}

