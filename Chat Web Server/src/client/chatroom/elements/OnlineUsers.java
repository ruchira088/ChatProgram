package client.chatroom.elements;

import java.util.Set;
import java.util.TreeSet;

import client.StringWriter;
import server.ChatServer;
import server.User;
import server.User.UserAttributes;

/**
 * 
 */
public class OnlineUsers {
    private ChatServer m_chatServer = new ChatServer();

    private Set<User> getOnlineUsers() {
	Set<User> onlineUsers = getChatServer().getOnlineUsers();

	TreeSet<User> treeSet = new TreeSet<>(onlineUsers);

	return treeSet;
    }

    /**
     * getChatServer description
     * 
     * @return
     */
    private ChatServer getChatServer() {
	return m_chatServer;
    }

    public String getTableMarkup() {
	StringWriter stringWriter = new StringWriter();

	stringWriter.append("<table class='table' id='onlineUserTable'>");
	stringWriter.append("<thead><tr><th>Online Users</th><th></th></tr></thead>");
	stringWriter.append("<tbody>");
	for (User user : getOnlineUsers()) {
	    stringWriter.append("<tr data-user-name='" + user.getUsername() + "'><td>" + user.getName()
		    + "</td><td><img class='onlineUserProfilePictures img-circle img-responsive' src='resource/"
		    + user.getAttribute(UserAttributes.PROFILE_PICTURE_PATH) + "'></td></tr>");
	}

	stringWriter.append("</tbody>");
	stringWriter.append("</table>");
	return stringWriter.toString();
    }

}
