package client;

import static general.Constants.LOGIN_FORM;
import static general.Constants.PROJECT_NAME;
import static server.ChatServer.isAuthenticatedUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import general.Constants;
import general.Credentials;
import general.Helpers;

public class LoginPage extends HttpServlet
{

   /**
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
   {
      if(Helpers.getAuthenticatedUserFromCookie(p_request) != null)
      {         
         p_request.getRequestDispatcher("jsp/Chatroom.jsp").forward(p_request, p_response);
    //     p_response.sendRedirect(PROJECT_NAME + Constants.CHAT_ROOM);
      } 
      else
      {
         p_request.getRequestDispatcher(LOGIN_FORM).forward(p_request, p_response);
        // p_response.sendRedirect(PROJECT_NAME + LOGIN_FORM);
      } 
      
      
//      String username = null;
//      String token = null;
//      
//      List<Cookie> cookies = Collections.emptyList();
//      
//      if(p_request.getCookies() != null)
//      {
//         cookies = Arrays.asList(p_request.getCookies());         
//      }
//      
//      
//
//      for (Cookie cookie : cookies)
//      {
//         String name = cookie.getName();
//         
//         switch (name)
//         {
//         case COOKIE_TOKEN:
//            token = cookie.getValue();
//            break;
//
//         case COOKIE_USER:
//            username = cookie.getValue();
//            break;
//         default:
//            break;
//         }
//      }
//
//      if(username == null || token == null)
//      {
//         p_response.sendRedirect(PROJECT_NAME + LOGIN_FORM);
//      } 
//      else
//      {
//         if(isAuthenticatedUser(username, token))
//         {
//            
//         } else
//         {
//            
//         }
//      }
   }

}
