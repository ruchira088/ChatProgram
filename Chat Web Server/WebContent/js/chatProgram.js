/**
 * 
 */

var g_uploadFile;

function initialize()
{
	addListenersToUserTable(document.getElementById("onlineUserTable"));
}

function setReceiver(userRow, data)
{
	for (var i = 0; i < data.length; i++)
	{
		data[i].removeAttribute("data-selectedUser");
	}

	userRow.setAttribute("data-selectedUser", "true");
}

function addListenersToUserTable(table)
{
	var data = table.querySelectorAll("tbody tr[data-user-name]");

	for (var i = 0; i < data.length; i++)
	{
		function addListener()
		{
			var userRow = data[i];
			var name = userRow.getAttribute("data-user-name");

			userRow.addEventListener("click", function()
			{
				setReceiver(userRow, data);
				clearMessageTable();
				getMessages(getUsername(), name);
				console.log(name);
			});
		}

		addListener();
	}
}

function getSelectedOnlineUser()
{
	var username = null;

	var selectedUser = document
			.querySelector("#onlineUserTable [data-selectedUser]");

	if (selectedUser)
	{
		username = selectedUser.getAttribute("data-user-name");
	}

	return username;
}

function drop(event, element)
{
	var file = event.dataTransfer.files[0];
	setPreviewImageAndSetAsUploadFile(event.dataTransfer.files[0]);
	event.preventDefault();
}

function setUploadingFile(file)
{
	g_uploadFile = file;
}

function newUser()
{
	var username = document.getElementById("username");

	var formData = new FormData();
	formData.append(username.name, username.value);

	var request = new XMLHttpRequest();
	request.open("POST", "/Chat_Web_Server/usernameChecker");
	request.onreadystatechange = function()
	{
		if (request.readyState == 4)
		{
			if (request.status == 200)
			{
				userForm(username);
			} else
			{
				var usernameAlreadyTaken = document
						.getElementById("usernameAlreadyTaken");
				usernameAlreadyTaken.removeAttribute("style");
			}
		}
	}
	request.send(formData);
}

function sendMessage()
{
	var messageBox = document.getElementById("messageTerminal");
	var message = messageBox.value;

	var formData = new FormData();
	formData.append("message", message);

	var getSelectedUser = getSelectedOnlineUser();

	if (getSelectedUser)
	{
		formData.append("recipient", getSelectedUser);

		var messageRequest = new XMLHttpRequest();
		messageRequest.onreadystatechange = function()
		{
			if (messageRequest.readyState == 4 && messageRequest.status == 200)
			{
				var messageHistory = document.getElementById("messageHistory");
				messageHistory.innerHTML = message + "\n"
						+ messageHistory.innerHTML;
				messageBox.value = "";
			}
		}
		messageRequest.open("POST", "/Chat_Web_Server/messageServer");
		messageRequest.send(formData);
	} else
	{
		alert("Please select a recipient.");
	}
}

function refreshMessages()
{
	var username = getUsername();
	var selectedOnlineUser = getSelectedOnlineUser();
	console.log("Starting refresh - current time " + new Date());
	console.log("refreshMessages: " + username + ", " + selectedOnlineUser);
	
	getMessages(username, selectedOnlineUser, true);	
}

function getMessages(username, selectedOnlineUser, refresh)
{
	var request = new XMLHttpRequest();

	request.onreadystatechange = function()
	{
		if (request.readyState == 4 && request.status == 200)
		{
			displayMessages(request.responseText);
		}
	}

	request.open("GET", "/Chat_Web_Server/messageServer?username=" + username
			+ (selectedOnlineUser ? "&sender=" + selectedOnlineUser : "")
			+ (refresh ? "&refresh=" + refresh : ""));
	request.send();

	console.log("Fetching Messages.");
}

function getUsername()
{
	var username = document.getElementById("username").getAttribute(
			"data-username");

	return username;
}

function displayMessages(response)
{
	var messages = JSON.parse(response);

//	var displayedMessagesLength = getDisplayedMessages().length;
//
//	if (displayedMessagesLength != 0)
//	{
//		var temp = messages;
//		messages = messages.splice(displayedMessagesLength, messages.length);
//	}

	for (var i = 0; i < messages.length; i++)
	{
		insertIntoMessageTable(messages[i]);
	}
	
	console.log(messages);
	console.log("scheduling refresh in 1 second - current time " + new Date());
	refreshMessages();
	//window.setTimeout(refreshMessages, 1000);
}

function getDisplayedMessages()
{
	var messageElements = document.querySelectorAll("#inbox tbody tr");

	var messages = [];

	for (var i = 0; i < messageElements.length; i++)
	{
		messages[i] = new Message(messageElements[i]);
	}

	return messages;
}

function Message(messageElement)
{
	this.getValue = function(className)
	{
		return messageElement.querySelector("." + className).innerHTML;
	}

	this.sender = this.getValue("sender");
	this.time = this.getValue("time");
	this.message = this.getValue("message");

}

function clearMessageTable()
{
	var messagestable = document.getElementById("inbox");
	var tableBody = document.createElement("tbody");

	messagestable.replaceChild(tableBody, messagestable
			.getElementsByTagName("tbody")[0]);
}

function insertIntoMessageTable(message)
{
	var senderData = document.createElement("td");
	senderData.setAttribute("class", "sender");
	senderData.innerHTML = message.sender;

	var timeData = document.createElement("td");
	timeData.setAttribute("class", "time");
	timeData.innerHTML = getStringFromTime(message.time);

	var messageData = document.createElement("td");
	messageData.setAttribute("class", "message");
	messageData.innerHTML = message.messageContents;

	var tableRow = document.createElement("tr");
	tableRow.appendChild(senderData);
	tableRow.appendChild(timeData);
	tableRow.appendChild(messageData);

	var tableBody = document.querySelector("#inbox tbody");

	tableBody.appendChild(tableRow);
	}

function getStringFromTime(timeValue)
{
	return timeValue.Day + " " + timeValue.Month + " " + timeValue.Year + " - "
			+ timeValue.Hour + ":" + timeValue.Minutes + ":"
			+ timeValue.Seconds;
}

function userForm(username)
{
	var name = document.getElementById("NAME");
	var password = document.getElementById("password");
	var userForm = new FormData();

	userForm.append(name.id, name.value);
	userForm.append(username.id, username.value);
	userForm.append(password.id, password.value);

	if (g_uploadFile != null)
	{
		userForm.append("PROFILE_PICTURE", g_uploadFile, g_uploadFile.name);
	}

	var request = new XMLHttpRequest();
	request.onreadystatechange = function()
	{
		if (request.readyState == 4 && request.status == 200)
		{
			// document.getElementById("userForm").submit();
			window.location.replace("/Chat_Web_Server");
		}
	}
	request.open("POST", "/Chat_Web_Server/newUser");
	request.send(userForm);

}

function setPreviewImageAndSetAsUploadFile(file)
{
	var reader = new FileReader();
	setUploadingFile(file)
	reader.onload = function(event)
	{
		var previewImage = document.getElementById("previewImage");
		previewImage.src = reader.result;
	}

	reader.readAsDataURL(file);
}

function dragOver(event)
{
	event.preventDefault();
	event.dataTransfer.dropEffect = 'copy';
}

function validateForm()
{
	var submitForm = true;
	var enteredUsername = document.getElementById("username").value;
	var enteredPassword = document.getElementById("password").value;

	var noUsername = document.getElementById("noUsername");
	var noPassword = document.getElementById("noPassword");
	var invalidCredentials = document.getElementById("invalidCredentials");
	hideElements(noUsername, noPassword, invalidCredentials);

	if (enteredUsername == "")
	{
		noUsername.removeAttribute("style");
		submitForm = false;
	}

	if (enteredPassword == "")
	{
		noPassword.removeAttribute("style");
		submitForm = false;
	}

	if (submitForm)
	{
		var xmlHttpRequest = new XMLHttpRequest();
		xmlHttpRequest.onreadystatechange = function()
		{
			if (xmlHttpRequest.readyState == 4)
			{
				if (xmlHttpRequest.status == 200)
				{
					document.getElementById("loginForm").submit();
				}
				if (xmlHttpRequest.status == 401)
				{
					invalidCredentials.removeAttribute("style");
				}
			}
		}

		xmlHttpRequest.open("POST", "/Chat_Web_Server/validator", true);
		xmlHttpRequest.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		xmlHttpRequest.send("username=" + enteredUsername + "&password="
				+ enteredPassword + "&validate=true");
	}
}

function hideElements()
{
	for (var i = 0; i < arguments.length; i++)
	{
		if (arguments[i] != null)
		{
			arguments[i].setAttribute("style", "display: none");
		}
	}
}
