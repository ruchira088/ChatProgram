package client.messaging;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;

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
public class MessagingServer extends HttpServlet
{
	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 4221158312136361424L;

	// TODO Think about concurrent logins (different users) with same the cookie space. What are the implications ??
	@Override
	protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response)
			throws ServletException, IOException
	{
		String messageContents = p_request.getParameter("message");
		String recipient = p_request.getParameter("recipient");

		performMessageOperation(p_request, p_response, new SendMessageOperation(messageContents, recipient));
	}

	@Override
	protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response)
			throws ServletException, IOException
	{
		String username = p_request.getParameter("username");
		String sender = p_request.getParameter("sender");
		boolean update = p_request.getParameter("refresh") == null ? false : Boolean.valueOf(p_request.getParameter("refresh"));

		Date date = update ? new Date() : null;		
		
		Credentials credentials = Helpers.getCredentialsFromCookies(p_request);

		if (username.equals(credentials.getUsername()))
		{
			performMessageOperation(p_request, p_response, new RetrieveMessagesOperation(sender, date));
		}
		else
		{
			p_response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}

	}

	private void performMessageOperation(HttpServletRequest p_request, HttpServletResponse p_response,
			MessagingOperation p_messagingOperation)
	{
		Credentials credentials = Helpers.getAuthenticatedUserFromCookie(p_request);

		try
		{
			if (credentials != null)
			{
				boolean success = p_messagingOperation.performOperatrion(credentials, p_response);
				p_response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_UNAUTHORIZED);
			}
			else
			{
				p_response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		catch (Exception exception)
		{
			p_response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	}

	public class SendMessageOperation implements MessagingOperation
	{
		private Message<String> m_message = null;

		@Override
		public boolean performOperatrion(Credentials p_credentials, HttpServletResponse p_response) throws Exception
		{
			ChatServer chatServer = new ChatServer();

			return chatServer.sendMessage(p_credentials.getUsername(), p_credentials.getToken(), m_message);
		}

		public SendMessageOperation(String p_message, String p_recipient)
		{
			m_message = new Message<String>(p_message, new Date(), p_recipient);
		}
	}

	public class RetrieveMessagesOperation implements MessagingOperation
	{
		private Date m_date = null;
		
		private String m_sender = null;
		
		@Override
		public boolean performOperatrion(Credentials p_credentials, HttpServletResponse p_response) throws Exception
		{
			ChatServer chatServer = new ChatServer();

			LinkedList<Message<String>> messages = chatServer.getMessages(p_credentials.getUsername(),
					p_credentials.getToken(), m_sender, m_date);
						
			PrintWriter writer = p_response.getWriter();					
			writer.println(messages);
			
			writer.flush();
			writer.close();

			return messages != null;
		}
		
		public RetrieveMessagesOperation(String p_sender, Date p_date)
		{
			m_date = p_date;
			m_sender = p_sender;
		}
	}

}
