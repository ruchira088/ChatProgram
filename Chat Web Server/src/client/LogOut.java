package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.UnauthorisedOperationException;
import general.Constants;
import general.Credentials;
import general.Helpers;
import server.ChatServer;

public class LogOut extends HttpServlet
{
   /**
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
   {
      Credentials credentials = Helpers.getCredentialsFromCookies(p_request);
      ChatServer chatServer = new ChatServer();

      if(credentials != null)
      {
         try
         {
            chatServer.logout(credentials.getUsername(), credentials.getToken());
            removeCookies(p_request, p_response);
         }
         catch (UnauthorisedOperationException unauthorisedOperationException)
         {
            PrintWriter writer = p_response.getWriter();
            writer.print("<body><p>Something went wrong.</p></body>");
         }
         
         p_response.sendRedirect(Constants.PROJECT_NAME + Constants.LOG_OUT + "?user=" + credentials.getUsername());
      }
      else
      {
         // TODO Add a message to indicate that the user has already logged out.
      }
      
      
      
//      URL url = new URL("http://localhost:8080/My_Chat_Program/off.jsp");
//      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//      connection.setRequestMethod("POST");
//      connection.setDoOutput(true);
//      OutputStream outputStream = connection.getOutputStream();
//      outputStream.write("username=hello".getBytes());
//      outputStream.flush();
//
//      connection.connect();
//      Scanner scanner = new Scanner(connection.getInputStream());           
//      PrintWriter printWriter = p_response.getWriter();
//      
//     while(scanner.hasNextLine())
//     {
//        printWriter.append(scanner.nextLine());  
//     }
//    
//     scanner.close();
//     printWriter.flush();

   }
   
   private void removeCookies(HttpServletRequest p_request, HttpServletResponse p_response)
   {
      List<Cookie> cookies = Arrays.asList(p_request.getCookies());
      
      for(Cookie cookie : cookies)
      {
         cookie.setMaxAge(0);
         cookie.setValue(null);
         p_response.addCookie(cookie);
      }
      
   }

}
