
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
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/chatProgram.js"></script>
<link rel="stylesheet" href="css/style.css">
<title>Chatroom</title>
</head>
<body
	onload='addListenersToUserTable(document.getElementById("onlineUserTable"))'>
	<div class="container">
		<%
			Credentials credentials = Helpers.getAuthenticatedUserFromCookie(request);

			if (credentials  != null) {
				ChatServer chatServer = new ChatServer();
				OnlineUsers onlineUsers = new OnlineUsers();

				User user = null;

				try {
					user = chatServer.getUser(credentials.getUsername());
				} catch (Exception exception) {

				}

				if (user != null) {
		%>
		<h2>
			Hi
			<%=user.getName()%>,
		</h2>
		<p>
			<a href="/Chat_Web_Server/logout">logout</a>
		</p>
		<p>
			<img id="profilePicture" class="img-circle img-responsive"
				src="<%=Constants.PROJECT_NAME + Constants.RESOURCE_SERVER
							+ user.getAttribute(UserAttributes.PROFILE_PICTURE_PATH)%>
        ">
		</p>
		<div class="table-responsive">
			<%=onlineUsers.getTableMarkup()%>
		</div>
		<div>
			<textarea id="messageTerminal" placeholder="Enter a message"></textarea> <button id="sendBtn" onclick="sendMessage()">Send</button>
		</div>
		<div>
			<textarea cols="2" id="messageHistory">
			
		</textarea>
		</div>
		<%
			}
			} else {
				response.sendRedirect(Constants.PROJECT_NAME + Constants.LOGIN_FORM);
			}
		%>
	</div>
</body>
</html>