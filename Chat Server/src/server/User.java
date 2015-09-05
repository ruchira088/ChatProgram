package server;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class User implements Comparable<User>
{
   private String m_username;

   private String m_password;
   
   private Map<UserAttributes, String> m_attributes;

   public enum UserAttributes
   {
      NAME, GENDER, PROFILE_PICTURE_PATH;
   }   

   public User(String p_username, String p_password)
   {
      this(p_username, p_password, new HashMap<UserAttributes, String>());
   }
   
   public User(String p_username, Map<UserAttributes, String> p_attributes)
   {
      this(p_username, null, p_attributes);
   }
   
   public User(String p_username, String p_password, Map<UserAttributes, String> p_attributes)
   {
      setUsername(p_username);
      setPassword(p_password);
      setAttributes(p_attributes);
   }

  
   
   /**
    * Set attributes
    * @param p_attributes the attributes to set
    */
   public void setAttributes(Map<UserAttributes, String> p_attributes)
   {
      m_attributes = p_attributes;
   }


   public Map<UserAttributes, String> getAttributes()
   {
      return m_attributes;
   }

   public void setAttribute(UserAttributes p_attribute, String p_value)
   {   
      m_attributes.put(p_attribute, p_value);
   }
   
   public String getAttribute(UserAttributes p_userAttribute)
   {
      String attribute = null;
      
      if(getAttributes() != null)
      {
         attribute = getAttributes().get(p_userAttribute);
      }
      return attribute;
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
    * Set username
    * @param p_username the username to set
    */
   public void setUsername(String p_username)
   {
      m_username = p_username;
   }

   /**
    * Get password
    * @return the password
    */
   public String getPassword()
   {
      return m_password;
   }

   /**
    * Set password
    * @param p_password the password to set
    */
   public void setPassword(String p_password)
   {
      m_password = p_password;
   }

   /**
    * Get name
    * @return the name
    */
   public String getName()
   {
      String name;
      
      if(getAttributes() != null && getAttributes().containsKey(UserAttributes.NAME))
      {
         name = getAttributes().get(UserAttributes.NAME);
      } else 
      {
         name = getUsername();
      }
      return name;
   }

   /**
    * Set name
    * @param p_name the name to set
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
