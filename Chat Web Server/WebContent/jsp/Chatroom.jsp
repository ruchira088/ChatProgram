
<%@page import="java.util.Set"%>
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
<body onload='initialize()'>
	<div class="container">
		<%
			Credentials credentials = Helpers.getAuthenticatedUserFromCookie(request);

			if (credentials != null)
			{
				ChatServer chatServer = new ChatServer();

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
		<h2 id='userGreeting'>
			Hi
			<%=user.getName()%>,
		</h2>
		<span id="username" data-username="<%=user.getName()%>"></span>
		<div class="content">
			<table id="topArea">
				<tr>
					<td class="leftColumn">
						<p>
							<a href="/Chat_Web_Server/logout">logout</a>
						</p>
						<p>
							<img id="profilePicture" class="img-circle img-responsive"
								src="<%=Constants.PROJECT_NAME + Constants.RESOURCE_SERVER
							+ user.getAttribute(UserAttributes.PROFILE_PICTURE_PATH)%>">
						</p>

						<div>
							<textarea id="messageTerminal" placeholder="Enter a message"></textarea>
							<br />
							<button id="sendBtn" onclick="sendMessage()">Send</button>
						</div>
						<div>
							<textarea id="messageHistory"></textarea>
						</div>
						<div>
							<input type="button"
								onclick="getMessages('<%=user.getName()%>', getSelectedOnlineUser())"
								value="Get Messages">
						</div>
					</td>
					<td class="rightColumn">
						<div class="table-responsive">
							<table class='table' id='onlineUserTable'>
								<thead>
									<tr>
										<th colspan="2">Online Users</th>
									</tr>
								</thead>
								<tbody>
									<%
										Set<User> onlineUsers = chatServer.getOnlineUsers();

												for (User onlineUser : onlineUsers)
												{
									%>
									<tr data-user-name='<%=onlineUser.getUsername()%>'>
										<td><%=onlineUser.getName()%></td>
										<td><img
											class='onlineUserProfilePictures img-circle img-responsive'
											src='resource/<%=onlineUser.getAttribute(UserAttributes.PROFILE_PICTURE_PATH)%>'></td>
									</tr>
									<%
										}
									%>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</table>
			<div class="fullColumn">
				<table id='inbox'>
					<thead>
						<tr>
							<th class="sender">Sender</th>
							<th class="time">Time</th>
							<th class="message">Message</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>

			</div>
			<%
				}
				}
				else
				{
					response.sendRedirect(Constants.PROJECT_NAME + Constants.LOGIN_FORM);
				}
			%>
		</div>
	</div>
</body>
</html>