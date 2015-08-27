package general;

import java.io.Serializable;
import java.util.UUID;

public class SessionToken implements Serializable
{
   /** Serial Version UID */
   private static final long serialVersionUID = 6664715201223933974L;
   
   private final String m_id;
   
   public SessionToken(String p_id)
   {
      m_id = p_id;
   }
   
   public SessionToken()
   {
      this(UUID.randomUUID().toString());
   }

   /**
    * Get id
    * @return the id
    */
   public String getId()
   {
      return m_id;
   }

   /**
    * @see java.lang.Object#hashCode()
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

