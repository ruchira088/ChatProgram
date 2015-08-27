package client.chatroom.elements;

import java.util.Set;
import java.util.TreeSet;

import client.StringWriter;
import server.ChatServer;
import server.User;

/**
 * 
 */
public class OnlineUsers
{
   private ChatServer m_chatServer = new ChatServer();
   
   private Set<User> getOnlineUsers()
   {
      Set<User> onlineUsers = getChatServer().getOnlineUsers();
      
      TreeSet<User> treeSet = new TreeSet<>(onlineUsers);
      
      return treeSet;
   }

   /**
    * getChatServer description
    * @return
    */
   private ChatServer getChatServer()
   {
      return m_chatServer;
   }
   
   
   public String getTableMarkup()
   {
      StringWriter stringWriter = new StringWriter();
      
      stringWriter.append("<table class='table' id='onlineUserTable'>");
      stringWriter.append("<tr><th>Online Users</th></tr>");
      
      for(User user: getOnlineUsers())
      {
         stringWriter.append("<tr><td id='" + user.getUsername() +"'>" + user.getName() + "</td></tr>");
      }
      
      stringWriter.append("</table>");
      
      stringWriter.append("<script>");
      stringWriter.append("var data = document.getElementsByTagName('td');");
      stringWriter.append("for( var i = 0; i < data.length; i++){");
      stringWriter.append("data[i].addEventListener('click', doSomething);}");
//      stringWriter.append("function doSomething(event){");
//      stringWriter.append("alert(event.srcElement.innerHTML);}");
      stringWriter.append("</script>");
      return stringWriter.toString();
   }
   
}

