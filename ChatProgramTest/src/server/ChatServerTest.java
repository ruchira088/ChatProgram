package server;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import exceptions.UsernameAlreadyExistsException;
import general.Message;
import general.SqlQueries;

public class ChatServerTest
{
   private static final String USERNAME_1 = "MyUser_" + getRandomNumber();
   
   private static final String PASSWORD_1 = "MyPassword_" + getRandomNumber();
   
   private static final String USERNAME_2 = "Glebe_" + getRandomNumber();
   
   private static final String PASSWORD_2 = "Sydney_" + getRandomNumber();
   
   private static final String MESSAGE_1 = "TestMessage_" + getRandomNumber();
   
   private ChatServer m_chatServer = new ChatServer();
   
   @Test
   public void StringTest()
   {
      String query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ''CHAT'' AND TABLE_NAME = ''{0}''";
      System.out.println(MessageFormat.format(query, new Object[] {"USER_TABLE"}));
   }

   @Test
   public void authenticateUser()
   {
      String sessionToken = null;
      
      try
      {
         sessionToken = getChatServer().createUser(USERNAME_1, PASSWORD_1);
      }
      catch (UsernameAlreadyExistsException usernameAlreadyExistsException)
      {
        try
      {
         sessionToken = getChatServer().login(USERNAME_1, PASSWORD_1);
      }
        catch (Exception exception)
        {}
      }
      catch (Exception exception)
      {}
        
      Assert.assertNotNull("A session token was assigned.", sessionToken);

   }

   @Test
   public void sendAndReceiveMessages() throws Exception
   {
      String token_1 = createOrLoginAsUser(USERNAME_1, PASSWORD_1);
      String token_2 = createOrLoginAsUser(USERNAME_2, PASSWORD_2);      
      
      Message<String> message = new Message<String>(MESSAGE_1, new Date(), USERNAME_1);
      message.setSender(USERNAME_2);
      getChatServer().sendMessage(USERNAME_2, token_2, message);
      
      List<Message<String>> messages = getChatServer().getMessages(USERNAME_1, token_1, null, null);
      
      System.out.println("getMessages() : " + messages);
      System.out.println("getSentMessages() : " + getChatServer().getSentMessages(USERNAME_2, token_2));
      
      Assert.assertTrue("The message was sent and received successfully.", messages.contains(message));
      
   }

   private ChatServer getChatServer()
   {
      return m_chatServer;
   }
   
   private static int getRandomNumber()
   {
      Random random = new Random();
      return random.nextInt(Integer.MAX_VALUE);
      
   }
   
   private String createOrLoginAsUser(String p_username, String p_password)
   {
      String sessionToken = null;
      
      try
      {
         sessionToken = getChatServer().createUser(p_username, p_password);
      }
      catch (UsernameAlreadyExistsException usernameAlreadyExistsException)
      {
        try
      {
         sessionToken = getChatServer().login(p_username, p_password);
      }
        catch (Exception exception)
        {}
      }
      catch (Exception exception)
      {}
      
      return sessionToken;
   }
}
