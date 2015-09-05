package client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import general.Constants;
import server.ChatServer;
import server.Image;
import server.User.UserAttributes;

/**
 * 
 */
@MultipartConfig
public class NewUser extends HttpServlet
{
   /**
    * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
   {
      p_request.getRequestDispatcher(Constants.NEW_USER_FORM).include(p_request, p_response);
   }

   /**
    * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
   {
      String username = p_request.getParameter("username");
      String password = p_request.getParameter("password");
      Part profilePic = p_request.getPart("PROFILE_PICTURE");
      Image profileImage = null;

      List<UserAttributes> userAttributes = Arrays.asList(UserAttributes.values());
      HashMap<UserAttributes, String> attributes = new HashMap<UserAttributes, String>();

      for (UserAttributes attribute : userAttributes)
      {
         attributes.put(attribute, p_request.getParameter(attribute.toString()));
      }

      if (profilePic != null)
      {
         byte[] profilePicture = getByteArrayFromInputStream(profilePic.getInputStream());
         String fileName = profilePic.getSubmittedFileName();

         profileImage = new Image(fileName, UUID.randomUUID().toString(), profilePicture);
         attributes.put(UserAttributes.PROFILE_PICTURE_PATH, profileImage.getImagePath());
      }

      ChatServer chatServer = new ChatServer();

      try
      {
         chatServer.createUser(username, password, attributes);

         if (profileImage != null)
         {
            chatServer.addImage(profileImage);
         }
      }
      catch (Exception exception)
      {
      }

      p_request.getRequestDispatcher(Constants.VALIDATOR).forward(p_request, p_response);
   }

   private byte[] getByteArrayFromInputStream(InputStream p_inputStream)
   {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      boolean isEof = false;

      while (!isEof)
      {
         try
         {
            int read = p_inputStream.read();

            if (read != -1)
            {
               outputStream.write(read);
            }
            else
            {
               isEof = true;
            }
         }
         catch (IOException e)
         {
         }
      }

      return outputStream.toByteArray();
   }

}
