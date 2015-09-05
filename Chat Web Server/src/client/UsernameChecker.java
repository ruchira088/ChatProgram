package client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import general.Constants;
import server.ChatServer;

/**
 * 
 */
@MultipartConfig
@WebServlet("/usernameChecker")
public class UsernameChecker extends HttpServlet
{

   /**
    * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
   {      
      boolean usernameExists = true;
      
      ChatServer chatServer = new ChatServer();
      
      try
      {
         usernameExists = chatServer.isExistingUsername(p_request.getParameter("username"));
      }
      catch (Exception exception)
      {
         
      }
      
      if(usernameExists)
      {
         p_response.setStatus(Constants.USERNAME_ALREADY_EXISTS_HTTP_STATUS_CODE);
      } else 
      {
         p_response.setStatus(HttpServletResponse.SC_OK);
      }
      
   }
   
}

