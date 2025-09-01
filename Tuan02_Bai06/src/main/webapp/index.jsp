<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Chat</title>
    <link rel="stylesheet" href="styles/main.css">
</head>
<body>
<h2>WebSocket Chat</h2>
<div id="chat-box"></div>
<input type="text" id="message" placeholder="Type a message..."/>
<button onclick="sendMessage()">Send</button>

<script src="scripts/websocket.js"></script>
</body>
</html>
