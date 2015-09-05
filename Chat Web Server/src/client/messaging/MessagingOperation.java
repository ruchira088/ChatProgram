package client.messaging;

import javax.servlet.http.HttpServletResponse;

import general.Credentials;

public interface MessagingOperation
{
	public boolean performOperatrion(Credentials p_credentials, HttpServletResponse p_response) throws Exception;
}
