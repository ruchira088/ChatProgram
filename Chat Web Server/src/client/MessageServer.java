package client;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import general.Credentials;
import general.Helpers;
import general.Message;
import server.ChatServer;

@MultipartConfig
@WebServlet("/messageServer")
public class MessageServer extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response)
	    throws ServletException, IOException {

	Credentials credentials = Helpers.getAuthenticatedUserFromCookie(p_request);

	String messageContents = p_request.getParameter("message");
	String recipient = p_request.getParameter("recipient");

	Message<String> message = new Message<String>(messageContents, new Date(), recipient);

	ChatServer chatServer = new ChatServer();

	try {
	    boolean success = chatServer.sendMessage(credentials.getUsername(), credentials.getToken(), message);
	    p_response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_UNAUTHORIZED);

	} catch (Exception e) 
	{
	    p_response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

    }

}
