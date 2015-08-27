<%@page import="server.User.UserAttributes"%>
<%@page import="general.Constants"%>
<%@page import="server.User"%>
<%@page import="client.chatroom.elements.OnlineUsers"%>
<%@page import="server.ChatServer"%>
<%@page import="general.Credentials"%>
<%@page import="general.Helpers"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/chatProgram.js"></script>
<link rel="stylesheet" href="css/style.css">
<title>Chatroom</title>
</head>
<body>
	<%
	   Credentials credentials = Helpers.getCredentialsFromCookies(request);

	   if (credentials != null && ChatServer.isAuthenticatedUser(credentials.getUsername(), credentials.getToken()))
	   {
	      ChatServer chatServer = new ChatServer();
	      OnlineUsers onlineUsers = new OnlineUsers();

	      User user = null;

	      try
	      {
	         user = chatServer.getUser(credentials.getUsername());
	      }
	      catch (Exception exception)
	      {

	      }

	      if (user != null)
	      {
	%>
	<h2>
		Welcome
		<%=user.getName()%></h2>
	<p>
		<a href="/My_Chat_Program/logout">logout</a>
	</p>
	<p>
		<img id="profilePicture"
			src="<%=Constants.PROJECT_NAME + Constants.RESOURCE_SERVER + user.getAttribute(UserAttributes.PROFILE_PICTURE_PATH)%>">
	</p>
	<%=onlineUsers.getTableMarkup()%>

	<textarea id="messageTerminal" onclick="clickedOnMessageBox(this)" class="new">Enter a message</textarea>

	<%
	   }
	   }
	   else
	   {
	      response.sendRedirect(Constants.PROJECT_NAME + Constants.LOGIN_FORM);
	   }
	%>
</body>
</html>