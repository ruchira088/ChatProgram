package client.chatroom;

import static general.Constants.LOGIN_FORM;
import static general.Constants.PROJECT_NAME;
import static server.ChatServer.isAuthenticatedUser;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import client.StringWriter;
import client.chatroom.elements.OnlineUsers;
import general.Constants;
import general.Credentials;
import general.Helpers;
import server.ChatServer;
import server.User;
import server.User.UserAttributes;

public class Chatroom extends HttpServlet
{

   /**
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
   {
      Credentials credentials = Helpers.getCredentialsFromCookies(p_request);

      if (credentials != null && isAuthenticatedUser(credentials.getUsername(), credentials.getToken()))
      {
         ChatServer chatServer = new ChatServer();
         OnlineUsers onlineUsers = new OnlineUsers();
         
         User user = null;
         
         try
         {
            user = chatServer.getUser(credentials.getUsername());
         }
         catch (Exception e)
         {
         }
         
         PrintWriter writer = p_response.getWriter();

         StringWriter stringWriter = new StringWriter();
         
         stringWriter.append("<head>");
         stringWriter.append("<title>My Chat Program</title>");
         stringWriter.append("<link rel='stylesheet' href='css/style.css'>");
         stringWriter.append("<script src='js/chatProgram.js'></script>");
         stringWriter.append("</head>");
         stringWriter.append("<body onload='addListenersToUserTable(document.getElementById(\"onlineUserTable\"))'><h2>Welcome " + user.getUsername() + "</h2><p><a href='/Chat_Web_Server/logout'>logout</a></p>");
         stringWriter.append("<p><img id='profilePicture' src='" + Constants.PROJECT_NAME + Constants.RESOURCE_SERVER + user.getAttribute(UserAttributes.PROFILE_PICTURE_PATH) + "' alt=''</p>");
         stringWriter.append("<p>name : " + user.getAttribute(UserAttributes.NAME) + "</p>");
         stringWriter.append("<p>gender : " + user.getAttribute(UserAttributes.GENDER) + "</p>");
         stringWriter.append(onlineUsers.getTableMarkup());
         stringWriter.append("</body>");
         writer.append(stringWriter.toString());
         writer.flush();
      }
      else
      {
         p_response.sendRedirect(PROJECT_NAME + LOGIN_FORM);
      }

      //      
      //      
      //      PrintWriter writer = p_response.getWriter();
      //      
      //      Enumeration<String> enumeration = p_request.getAttributeNames();
      //      
      //      
      //      if(enumeration.hasMoreElements())
      //      {
      //         writer.print("<p>" + enumeration.nextElement() + "</p>");         
      //      } else
      //      {
      //         writer.print("<p>No attributes</p>");         
      //      }
      //      
      //      Enumeration<String> names = p_request.getSession().getAttributeNames();
      //      
      //      writer.print("<h1>Successful</h1>");
      //      
      //      while(names.hasMoreElements())
      //      {
      //         writer.print("<p>" + names.nextElement() + "</p>");         
      //      }
      //      
      //      List<Cookie> cookies = Arrays.asList(p_request.getCookies());
      //      
      //      for(Cookie cookie: cookies)
      //      {
      //         writer.println("<p>" + cookie.getName() + " - " + cookie.getValue() + "</p>");
      //         
      //      }
      //      
      //      writer.flush();
   }

}
