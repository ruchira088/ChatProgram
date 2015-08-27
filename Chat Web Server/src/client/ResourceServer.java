package client;

import static server.ChatServer.getChatServer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/resource/*")
public class ResourceServer extends HttpServlet
{

   /**
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
   {
      String path = p_request.getPathInfo();
      StringTokenizer stringTokenizer = new StringTokenizer(path, "/");

      boolean isValid = true;

      String id = null;
      String name = null;

      try
      {
         id = stringTokenizer.nextToken();
         name = stringTokenizer.nextToken();
      }
      catch (NoSuchElementException noSuchElementException)
      {
         isValid = false;
      }
      
      String etag = p_request.getHeader("If-None-Match");

      if (isValid)
      {
         try
         {
            byte[] image = getChatServer().getImage(id, name);

            if (image != null)
            {
               byte[] md5byteArray = MessageDigest.getInstance("MD5").digest(image);
               String md5 = new BigInteger(md5byteArray).toString();

               if (md5.equals(etag))
               {
                  p_response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
               }
               else
               {
                  p_response.setHeader("ETag", md5);
                  p_response.setHeader("Cache-Control", "max-age=3600");
                  ServletOutputStream outputStream = p_response.getOutputStream();
                  outputStream.write(image);
                  outputStream.flush();
               }
            }
            else
            {
            }
         }
         catch (Exception exception)
         {

         }
      }

      

   }

   private String getFileExtension(String p_fileName)
   {
      return p_fileName.substring(p_fileName.lastIndexOf(".") + 1, p_fileName.length());
   }

}
