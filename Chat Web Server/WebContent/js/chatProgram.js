/**
 * 
 */

var g_uploadFile;

function drop(event, element) {
   var file = event.dataTransfer.files[0];
   setPreviewImageAnsSetAsUploadFile(event.dataTransfer.files[0]);
   event.preventDefault();
}

function clickedOnMessageBox(element) {
   var initialised = element.className;

   if (initialised == "new") 
   {
      element.innerHTML = "";
      element.setAttribute("class", "initialised");
   }

}

function setUploadingFile(file) {
   g_uploadFile = file;
}

function newUser() {
   var username = document.getElementById("username");

   var formData = new FormData();
   formData.append(username.name, username.value);

   var request = new XMLHttpRequest();
   request.open("POST", "/My_Chat_Program/usernameChecker");
   request.onreadystatechange = function() {
      if (request.readyState == 4) {
         if (request.status == 200) {
            userForm(username);
         } else {
            var usernameAlreadyTaken = document.getElementById("usernameAlreadyTaken");
            usernameAlreadyTaken.removeAttribute("style");
         }
      }
   }
   request.send(formData);
}

function userForm(username) {
   var name = document.getElementById("NAME");
   var password = document.getElementById("password");
   var userForm = new FormData();

   userForm.append(name.id, name.value);
   userForm.append(username.id, username.value);
   userForm.append(password.id, password.value);

   if (g_uploadFile != null) {
      userForm.append("PROFILE_PICTURE", g_uploadFile, g_uploadFile.name);
   }

   var request = new XMLHttpRequest();
   request.onreadystatechange = function() {
      if (request.readyState == 4 && request.status == 200) {
         //document.getElementById("userForm").submit();
         window.location.replace("/My_Chat_Program/chatroom");
      }
   }
   request.open("POST", "/My_Chat_Program/newUser");
   request.send(userForm);

}

function setPreviewImageAnsSetAsUploadFile(file) {
   var reader = new FileReader();
   setUploadingFile(file)
   reader.onload = function(event) {
      var previewImage = document.getElementById("previewImage");
      previewImage.src = reader.result;
   }

   reader.readAsDataURL(file);
}

function dragOver(event) {
   event.preventDefault();
   event.dataTransfer.dropEffect = 'copy';
}

function doSomething(event) {
   var username = event.srcElement.getAttribute("id");
   alert("Hello " + username);
}

function validateForm() {
   var submitForm = true;
   var enteredUsername = document.getElementById("username").value;
   var enteredPassword = document.getElementById("password").value;

   var noUsername = document.getElementById("noUsername");
   var noPassword = document.getElementById("noPassword");
   var invalidCredentials = document.getElementById("invalidCredentials");
   hideElements(noUsername, noPassword, invalidCredentials);

   if (enteredUsername == "") {
      noUsername.removeAttribute("style");
      submitForm = false;
   }

   if (enteredPassword == "") {
      noPassword.removeAttribute("style");
      submitForm = false;
   }

   if (submitForm) {
      var xmlHttpRequest = new XMLHttpRequest();
      xmlHttpRequest.onreadystatechange = function() {
         if (xmlHttpRequest.readyState == 4) {
            if (xmlHttpRequest.status == 200) {
               document.getElementById("loginForm").submit();
            }
            if (xmlHttpRequest.status == 401) {
               invalidCredentials.removeAttribute("style");
            }
         }
      }

      xmlHttpRequest.open("POST", "/My_Chat_Program/validator", true);
      xmlHttpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      xmlHttpRequest.send("username=" + enteredUsername + "&password=" + enteredPassword + "&validate=true");
   }
}

function hideElements() {
   for (var i = 0; i < arguments.length; i++) {
      if (arguments[i] != null) {
         arguments[i].setAttribute("style", "display: none");
      }
   }
}
