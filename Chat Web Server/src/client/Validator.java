package client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import general.Constants;
import server.ChatServer;

public class Validator extends HttpServlet
{
   /** Serial Version UID */
   private static final long serialVersionUID = 1952600717614130415L;

   /**
    * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
   {
      String username = p_request.getParameter("username");
      String password = p_request.getParameter("password");
      boolean validate = Boolean.valueOf(p_request.getParameter("validate"));
      
      ChatServer chatServer = new ChatServer();
      String token = null;
      
      try
      {
         token = chatServer.login(username, password);
      }
      catch (Exception e)
      {
      }
      
      if(validate)
      {
         if(token != null)
         {
            p_response.setStatus(HttpServletResponse.SC_OK);
         } else
         {
            p_response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
         }
         
      } else
      {
         if(token != null)
         {
            Cookie cookieToken = new Cookie(Constants.COOKIE_TOKEN, token);
            cookieToken.setMaxAge(Constants.COOKIE_EXPIRY_TIME);
            p_response.addCookie(cookieToken);
            
            Cookie cookieUser = new Cookie(Constants.COOKIE_USER, username);
            cookieUser.setMaxAge(Constants.COOKIE_EXPIRY_TIME);
            p_response.addCookie(cookieUser);
            
            p_response.sendRedirect(Constants.PROJECT_NAME);
            
         } else
         {
            invalidCredentials(p_response);
         }
      }
      
     
   }
   
   private void invalidCredentials(HttpServletResponse p_response)
   {
      try
      {
         PrintWriter writer = p_response.getWriter();
         writer.write("<p>Invalid credentials.</p>");
         writer.flush();
      }
      catch (IOException ioException)
      {
      }
   }
   
   
   

}

