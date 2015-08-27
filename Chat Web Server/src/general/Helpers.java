package general;

import static general.Constants.COOKIE_TOKEN;
import static general.Constants.COOKIE_USER;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Helpers
{
   public static Credentials getCredentialsFromCookies(HttpServletRequest p_request)
   {
      Credentials credentials = null;   
      String username = null;
      String token = null;
      
      List<Cookie> cookies = Collections.emptyList();

      if (p_request.getCookies() != null)
      {
         cookies = Arrays.asList(p_request.getCookies());
      }

      for (Cookie cookie : cookies)
      {
         String name = cookie.getName();

         switch (name)
         {
         case COOKIE_TOKEN:
            token = cookie.getValue();
            break;

         case COOKIE_USER:
            username = cookie.getValue();
            break;
         default:
            break;
         }
      }
      
      
      if(username != null && token != null)
      {
         credentials = new Credentials(username, token);
      }
      
      return credentials;
   }

}
